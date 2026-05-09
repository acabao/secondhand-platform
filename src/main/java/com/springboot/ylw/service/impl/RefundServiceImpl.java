package com.springboot.ylw.service.impl;

import com.springboot.ylw.entity.Message;
import com.springboot.ylw.entity.Order;
import com.springboot.ylw.entity.Payment;
import com.springboot.ylw.entity.Refund;
import com.springboot.ylw.mapper.GoodsMapper;
import com.springboot.ylw.mapper.OrderMapper;
import com.springboot.ylw.mapper.PaymentMapper;
import com.springboot.ylw.mapper.RefundMapper;
import com.springboot.ylw.pay.PaymentGateway;
import com.springboot.ylw.service.MessageService;
import com.springboot.ylw.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RefundServiceImpl implements RefundService {

    /** 卖家响应窗口小时数 */
    private static final int SELLER_RESPOND_HOURS = 48;
    private static final int SYSTEM_USER_ID = 0;

    @Autowired private RefundMapper refundMapper;
    @Autowired private OrderMapper orderMapper;
    @Autowired private PaymentMapper paymentMapper;
    @Autowired private GoodsMapper goodsMapper;
    @Autowired private MessageService messageService;
    @Autowired private List<PaymentGateway> gateways;

    @Value("${payment.gateway:sandbox}")
    private String configuredGateway;

    private PaymentGateway pickGateway(String preferred) {
        Map<String, PaymentGateway> byType = gateways.stream()
                .collect(Collectors.toMap(PaymentGateway::getType, g -> g));
        if (preferred != null && byType.containsKey(preferred)) return byType.get(preferred);
        PaymentGateway fallback = byType.get(configuredGateway);
        if (fallback == null) throw new RuntimeException("未配置可用支付网关: " + configuredGateway);
        return fallback;
    }

    @Override
    @Transactional
    public Refund apply(Integer orderId, Integer buyerId, String reason, String imagesJson) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) throw new RuntimeException("订单不存在");
        if (!order.getBuyerId().equals(buyerId)) throw new RuntimeException("无权操作该订单");
        if (order.getStatus() != 2) throw new RuntimeException("仅已发货订单可申请退款");
        if (reason == null || reason.trim().isEmpty()) throw new RuntimeException("请填写退款原因");

        Refund existing = refundMapper.selectByOrderId(orderId);
        if (existing != null) throw new RuntimeException("该订单已存在退款申请，不能重复发起");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, SELLER_RESPOND_HOURS);

        Refund refund = new Refund();
        refund.setOrderId(order.getId());
        refund.setOrderNo(order.getOrderNo());
        refund.setBuyerId(order.getBuyerId());
        refund.setSellerId(order.getSellerId());
        refund.setAmount(order.getPrice());
        refund.setReason(reason);
        refund.setImages(imagesJson);
        refund.setDeadline(cal.getTime());
        refundMapper.insert(refund);

        // 通知卖家
        notify(order.getSellerId(), order.getGoodsId(), String.format(
                "买家对订单 %s 发起退款申请，金额 ¥%s，请在 48 小时内处理，逾期买家可申请平台仲裁。",
                order.getOrderNo(), order.getPrice().toPlainString()));

        return refundMapper.selectById(refund.getId());
    }

    @Override
    @Transactional
    public Refund sellerAgree(Integer refundId, Integer sellerId) {
        Refund refund = mustGet(refundId);
        if (!refund.getSellerId().equals(sellerId)) throw new RuntimeException("无权操作");
        if (refund.getStatus() != 0) throw new RuntimeException("申请已处理");

        int ok = refundMapper.sellerAgree(refund.getId(), new Date());
        if (ok == 0) throw new RuntimeException("状态已变更，请刷新");

        executeRefund(refund);
        notify(refund.getBuyerId(), null, String.format(
                "卖家已同意您对订单 %s 的退款申请，款项 ¥%s 已原路退回。",
                refund.getOrderNo(), refund.getAmount().toPlainString()));
        return refundMapper.selectById(refund.getId());
    }

    @Override
    @Transactional
    public Refund sellerReject(Integer refundId, Integer sellerId, String reply) {
        Refund refund = mustGet(refundId);
        if (!refund.getSellerId().equals(sellerId)) throw new RuntimeException("无权操作");
        if (refund.getStatus() != 0) throw new RuntimeException("申请已处理");
        if (reply == null || reply.trim().isEmpty()) throw new RuntimeException("请填写拒绝原因");

        int ok = refundMapper.sellerReject(refund.getId(), reply, new Date());
        if (ok == 0) throw new RuntimeException("状态已变更，请刷新");

        notify(refund.getBuyerId(), null, String.format(
                "卖家拒绝了您对订单 %s 的退款申请。拒绝原因：%s。若有异议可申请平台仲裁。",
                refund.getOrderNo(), reply));
        return refundMapper.selectById(refund.getId());
    }

    @Override
    @Transactional
    public Refund applyArbitrate(Integer refundId, Integer buyerId) {
        Refund refund = mustGet(refundId);
        if (!refund.getBuyerId().equals(buyerId)) throw new RuntimeException("无权操作");

        boolean sellerRejected = refund.getStatus() == 2;
        boolean sellerTimedOut = refund.getStatus() == 0 && refund.getDeadline() != null
                && refund.getDeadline().before(new Date());
        if (!sellerRejected && !sellerTimedOut) {
            throw new RuntimeException("当前状态不允许申请仲裁");
        }

        // 超时情形：先把状态从 0 推进到 2（视为默拒），再转 3
        if (sellerTimedOut) {
            refundMapper.sellerReject(refund.getId(), "卖家未在 48 小时内响应（系统自动标记）", new Date());
        }
        int ok = refundMapper.applyArbitrate(refund.getId(), new Date());
        if (ok == 0) throw new RuntimeException("状态已变更，请刷新");

        notify(refund.getSellerId(), null, String.format(
                "买家已对订单 %s 申请平台仲裁，请耐心等待管理员处理。",
                refund.getOrderNo()));
        return refundMapper.selectById(refund.getId());
    }

    @Override
    @Transactional
    public Refund adminAgree(Integer refundId, String note) {
        Refund refund = mustGet(refundId);
        if (refund.getStatus() != 3) throw new RuntimeException("申请不在仲裁中");

        int ok = refundMapper.adminAgree(refund.getId(), note, new Date());
        if (ok == 0) throw new RuntimeException("状态已变更，请刷新");

        executeRefund(refund);
        String noteMsg = note == null || note.isEmpty() ? "" : "（" + note + "）";
        notify(refund.getBuyerId(), null, String.format(
                "平台裁决同意您对订单 %s 的退款申请%s，款项 ¥%s 已原路退回。",
                refund.getOrderNo(), noteMsg, refund.getAmount().toPlainString()));
        notify(refund.getSellerId(), null, String.format(
                "平台裁决同意买家对订单 %s 的退款申请%s，款项已原路退回。",
                refund.getOrderNo(), noteMsg));
        return refundMapper.selectById(refund.getId());
    }

    @Override
    @Transactional
    public Refund adminReject(Integer refundId, String note) {
        Refund refund = mustGet(refundId);
        if (refund.getStatus() != 3) throw new RuntimeException("申请不在仲裁中");

        int ok = refundMapper.adminReject(refund.getId(), note, new Date());
        if (ok == 0) throw new RuntimeException("状态已变更，请刷新");

        String noteMsg = note == null || note.isEmpty() ? "" : "（" + note + "）";
        notify(refund.getBuyerId(), null, String.format(
                "平台裁决驳回您对订单 %s 的退款申请%s，订单将继续正常履约。",
                refund.getOrderNo(), noteMsg));
        notify(refund.getSellerId(), null, String.format(
                "平台裁决驳回买家对订单 %s 的退款申请%s。",
                refund.getOrderNo(), noteMsg));
        return refundMapper.selectById(refund.getId());
    }

    /** 执行实际退款动作：沙箱退款 -> 关单 -> 商品下架 -> 标记 refund 已退款 */
    private void executeRefund(Refund refund) {
        Order order = orderMapper.selectById(refund.getOrderId());
        if (order == null) throw new RuntimeException("订单不存在");
        Payment payment = paymentMapper.selectByOrderId(refund.getOrderId());
        if (payment == null || payment.getStatus() != 1) {
            throw new RuntimeException("支付流水异常，无法退款");
        }

        PaymentGateway gateway = pickGateway(payment.getPayType());
        PaymentGateway.RefundResult result = gateway.refund(order, payment, refund.getAmount());

        paymentMapper.markRefunded(payment.getId(), result.rawResponse);
        orderMapper.closeForRefund(order.getId(), new Date());
        // 商品已售出，不再恢复上架（卖家可重新发布）；若希望恢复可改为 updateStatus(goodsId, 3)
        goodsMapper.updateStatus(order.getGoodsId(), 3);
        refundMapper.markRefunded(refund.getId(), new Date());
    }

    private Refund mustGet(Integer id) {
        Refund r = refundMapper.selectById(id);
        if (r == null) throw new RuntimeException("退款申请不存在");
        return r;
    }

    private void notify(Integer toId, Integer goodsId, String content) {
        Message m = new Message();
        m.setFromId(SYSTEM_USER_ID);
        m.setToId(toId);
        m.setGoodsId(goodsId);
        m.setContent(content);
        messageService.sendMessage(m);
    }

    @Override public Refund getById(Integer id) { return refundMapper.selectById(id); }
    @Override public Refund getByOrderId(Integer orderId) { return refundMapper.selectByOrderId(orderId); }
    @Override public List<Refund> listByBuyer(Integer buyerId) { return refundMapper.selectByBuyer(buyerId); }
    @Override public List<Refund> listBySeller(Integer sellerId) { return refundMapper.selectBySeller(sellerId); }
    @Override public List<Refund> listArbitrating() { return refundMapper.selectArbitrating(); }
    @Override public List<Refund> listAll() { return refundMapper.selectAll(); }
}

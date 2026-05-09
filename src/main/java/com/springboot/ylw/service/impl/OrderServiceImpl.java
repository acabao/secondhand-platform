package com.springboot.ylw.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.springboot.ylw.entity.Goods;
import com.springboot.ylw.entity.Message;
import com.springboot.ylw.entity.Order;
import com.springboot.ylw.entity.Payment;
import com.springboot.ylw.mapper.GoodsMapper;
import com.springboot.ylw.mapper.OrderMapper;
import com.springboot.ylw.mapper.PaymentMapper;
import com.springboot.ylw.service.MessageService;
import com.springboot.ylw.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    /** 支付超时分钟数 */
    private static final int PAYMENT_EXPIRE_MINUTES = 30;
    /** 平台账号 id，用于系统消息的 from_id */
    private static final int SYSTEM_USER_ID = 0;

    @Autowired private OrderMapper orderMapper;
    @Autowired private GoodsMapper goodsMapper;
    @Autowired private PaymentMapper paymentMapper;
    @Autowired private MessageService messageService;

    @Override
    @Transactional
    public Order createOrder(Order order) {
        Goods goods = goodsMapper.selectById(order.getGoodsId());
        if (goods == null) throw new RuntimeException("商品不存在");
        if (goods.getStatus() != 1) throw new RuntimeException("商品不在售，无法购买");
        if (goods.getUserId().equals(order.getBuyerId())) throw new RuntimeException("不能购买自己发布的商品");

        // 默认邮寄，兼容旧前端
        if (order.getDeliveryType() == null) order.setDeliveryType(1);
        if (order.getDeliveryType() == 1) {
            if (order.getAddressId() == null) throw new RuntimeException("邮寄订单需选择收货地址");
            order.setPickupPointId(null);
            order.setMeetTime(null);
        } else if (order.getDeliveryType() == 2) {
            if (order.getPickupPointId() == null) throw new RuntimeException("自提订单需选择自提点");
            if (order.getMeetTime() == null) throw new RuntimeException("自提订单需选择约见时间");
            order.setAddressId(null);
        } else {
            throw new RuntimeException("配送方式不合法");
        }

        order.setOrderNo(DateUtil.format(new Date(), "yyyyMMddHHmmss") + RandomUtil.randomNumbers(4));
        order.setSellerId(goods.getUserId());
        order.setPrice(goods.getPrice());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, PAYMENT_EXPIRE_MINUTES);
        order.setExpireTime(cal.getTime());

        orderMapper.insert(order);
        // 锁定库存：商品状态从"在售"改为"已售出"（二手平台一物一单）
        goodsMapper.updateStatus(order.getGoodsId(), 2);

        return orderMapper.selectById(order.getId());
    }

    @Override
    public Order getById(Integer id) { return orderMapper.selectById(id); }

    @Override
    public List<Order> getMyBuyOrders(Integer buyerId) { return orderMapper.selectByBuyerId(buyerId); }

    @Override
    public List<Order> getMySellOrders(Integer sellerId) { return orderMapper.selectBySellerId(sellerId); }

    @Override
    public List<Order> getAll() { return orderMapper.selectAll(); }

    @Override
    public String updateStatus(Integer id, Integer status) {
        orderMapper.updateStatus(id, status);
        return "订单状态已更新";
    }

    @Override
    @Transactional
    public void markPaid(Integer orderId, String payType) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) throw new RuntimeException("订单不存在");
        if (order.getStatus() != 0) return; // 幂等
        orderMapper.markPaid(orderId, payType, new Date());
        notifySeller(order);
    }

    @Override
    @Transactional
    public String ship(Integer orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) throw new RuntimeException("订单不存在");
        if (order.getStatus() != 1) throw new RuntimeException("订单不在待发货状态");
        orderMapper.markShipped(orderId, new Date());
        // 通知买家
        Message msg = new Message();
        msg.setFromId(SYSTEM_USER_ID);
        msg.setToId(order.getBuyerId());
        msg.setGoodsId(order.getGoodsId());
        msg.setContent("卖家已发货，订单号 " + order.getOrderNo() + "，请留意收货。");
        messageService.sendMessage(msg);
        return "发货成功";
    }

    @Override
    @Transactional
    public String confirmReceive(Integer orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) throw new RuntimeException("订单不存在");
        if (order.getStatus() != 2) throw new RuntimeException("订单不在待收货状态");
        // T+1 结算时间
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        orderMapper.markCompleted(orderId, new Date(), cal.getTime());
        // 通知卖家结算
        Message msg = new Message();
        msg.setFromId(SYSTEM_USER_ID);
        msg.setToId(order.getSellerId());
        msg.setGoodsId(order.getGoodsId());
        msg.setContent(String.format("买家已确认收货，订单 %s 款项 ¥%s 将于 T+1 结算到账。",
                order.getOrderNo(), order.getPrice().toPlainString()));
        messageService.sendMessage(msg);
        return "确认收货成功";
    }

    @Override
    @Transactional
    public String cancel(Integer orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) throw new RuntimeException("订单不存在");
        if (order.getStatus() != 0) throw new RuntimeException("只有待付款订单可以取消");
        orderMapper.markClosed(orderId, new Date());
        Payment payment = paymentMapper.selectByOrderId(orderId);
        if (payment != null && payment.getStatus() == 0) {
            paymentMapper.markClosed(payment.getId());
        }
        // 恢复商品到在售
        goodsMapper.updateStatus(order.getGoodsId(), 1);
        return "订单已取消";
    }

    @Override
    @Transactional
    public int closeExpired() {
        List<Order> expired = orderMapper.selectExpired(new Date());
        int closed = 0;
        for (Order o : expired) {
            int ok = orderMapper.markClosed(o.getId(), new Date());
            if (ok > 0) {
                Payment p = paymentMapper.selectByOrderId(o.getId());
                if (p != null && p.getStatus() == 0) paymentMapper.markClosed(p.getId());
                goodsMapper.updateStatus(o.getGoodsId(), 1);
                // 通知买家订单因超时关闭
                Message msg = new Message();
                msg.setFromId(SYSTEM_USER_ID);
                msg.setToId(o.getBuyerId());
                msg.setGoodsId(o.getGoodsId());
                msg.setContent("订单 " + o.getOrderNo() + " 因超过 30 分钟未支付已自动关闭。");
                messageService.sendMessage(msg);
                closed++;
            }
        }
        return closed;
    }

    private void notifySeller(Order order) {
        Message msg = new Message();
        msg.setFromId(SYSTEM_USER_ID);
        msg.setToId(order.getSellerId());
        msg.setGoodsId(order.getGoodsId());
        msg.setContent(String.format("您的商品已被买家付款，订单 %s，金额 ¥%s，请尽快发货。",
                order.getOrderNo(), order.getPrice().toPlainString()));
        messageService.sendMessage(msg);
    }
}

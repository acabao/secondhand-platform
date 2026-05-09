package com.springboot.ylw.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.springboot.ylw.entity.Order;
import com.springboot.ylw.entity.Payment;
import com.springboot.ylw.mapper.OrderMapper;
import com.springboot.ylw.mapper.PaymentMapper;
import com.springboot.ylw.pay.PaymentGateway;
import com.springboot.ylw.service.OrderService;
import com.springboot.ylw.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired private PaymentMapper paymentMapper;
    @Autowired private OrderMapper orderMapper;
    @Autowired private OrderService orderService;
    @Autowired private List<PaymentGateway> gateways;

    @Value("${payment.gateway:sandbox}")
    private String configuredGateway;

    private PaymentGateway pickGateway(String preferred) {
        Map<String, PaymentGateway> byType = gateways.stream()
                .collect(Collectors.toMap(PaymentGateway::getType, g -> g));
        if (preferred != null && byType.containsKey(preferred)) {
            return byType.get(preferred);
        }
        PaymentGateway fallback = byType.get(configuredGateway);
        if (fallback == null) throw new RuntimeException("未配置可用支付网关: " + configuredGateway);
        return fallback;
    }

    @Override
    @Transactional
    public Payment createPrepay(Integer orderId, String payType) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) throw new RuntimeException("订单不存在");
        if (order.getStatus() != 0) throw new RuntimeException("订单状态不允许支付");
        if (order.getExpireTime() != null && order.getExpireTime().before(new Date()))
            throw new RuntimeException("订单已超时关闭，请重新下单");

        // 复用已有未支付流水
        Payment existing = paymentMapper.selectByOrderId(orderId);
        if (existing != null && existing.getStatus() == 0) return existing;

        PaymentGateway gateway = pickGateway(payType);
        PaymentGateway.PrepayResult res = gateway.createPrepay(order);

        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setOrderNo(order.getOrderNo());
        payment.setPayType(gateway.getType());
        payment.setAmount(order.getPrice());
        payment.setQrPayload(res.qrPayload);
        paymentMapper.insert(payment);
        return payment;
    }

    @Override
    @Transactional
    public String sandboxNotify(String orderNo) {
        Payment payment = paymentMapper.selectByOrderNo(orderNo);
        if (payment == null) throw new RuntimeException("支付流水不存在");
        if (payment.getStatus() == 1) return "已支付，无需重复";
        if (payment.getStatus() != 0) throw new RuntimeException("支付单状态异常");

        String tradeNo = "SBX" + System.currentTimeMillis() + RandomUtil.randomNumbers(4);
        paymentMapper.markPaid(payment.getId(), tradeNo, new Date(), "{\"mode\":\"sandbox\",\"ok\":true}");
        orderService.markPaid(payment.getOrderId(), payment.getPayType());
        return "支付成功";
    }

    @Override
    public Payment getByOrderId(Integer orderId) {
        return paymentMapper.selectByOrderId(orderId);
    }
}

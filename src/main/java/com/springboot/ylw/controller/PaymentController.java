package com.springboot.ylw.controller;

import com.springboot.ylw.common.Result;
import com.springboot.ylw.entity.Payment;
import com.springboot.ylw.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Value("${payment.gateway:sandbox}")
    private String gateway;

    /** 下单后调用：为订单创建待支付流水，返回二维码payload */
    @PostMapping("/create")
    public Result<?> create(@RequestBody Map<String, Object> body) {
        try {
            Integer orderId = (Integer) body.get("orderId");
            String payType = (String) body.getOrDefault("payType", null);
            Payment payment = paymentService.createPrepay(orderId, payType);
            Map<String, Object> data = new HashMap<>();
            data.put("paymentId", payment.getId());
            data.put("orderNo", payment.getOrderNo());
            data.put("payType", payment.getPayType());
            data.put("amount", payment.getAmount());
            data.put("qrPayload", payment.getQrPayload());
            data.put("status", payment.getStatus());
            data.put("gatewayMode", gateway);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /** 前端轮询支付状态 */
    @GetMapping("/status/{orderId}")
    public Result<?> status(@PathVariable Integer orderId) {
        Payment p = paymentService.getByOrderId(orderId);
        if (p == null) return Result.success(null);
        Map<String, Object> data = new HashMap<>();
        data.put("status", p.getStatus());
        data.put("payType", p.getPayType());
        data.put("tradeNo", p.getTradeNo());
        data.put("payTime", p.getPayTime());
        return Result.success(data);
    }

    /** 沙箱模拟支付成功：由前端"模拟支付"按钮调用 */
    @PostMapping("/sandbox/notify")
    public Result<?> sandboxNotify(@RequestBody Map<String, String> body) {
        try {
            return Result.success(paymentService.sandboxNotify(body.get("orderNo")));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

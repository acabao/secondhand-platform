package com.springboot.ylw.pay;

import cn.hutool.core.util.RandomUtil;
import com.springboot.ylw.entity.Order;
import com.springboot.ylw.entity.Payment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 沙箱支付网关：不对接任何真实支付渠道。
 * 下单后返回一个自定义 payload 字符串，前端据此渲染假二维码；
 * "支付成功"通过 /payment/sandbox/notify 接口由买家点击触发。
 */
@Component
public class SandboxGateway implements PaymentGateway {

    @Override
    public String getType() {
        return "sandbox";
    }

    @Override
    public PrepayResult createPrepay(Order order) {
        // 沙箱 payload 格式：SBX|订单号|金额|时间戳
        String payload = String.format("SBX|%s|%s|%d",
                order.getOrderNo(),
                order.getPrice().toPlainString(),
                System.currentTimeMillis());
        return new PrepayResult(payload, null);
    }

    @Override
    public RefundResult refund(Order order, Payment payment, BigDecimal amount) {
        String refundNo = "SBXR" + System.currentTimeMillis() + RandomUtil.randomNumbers(4);
        String raw = String.format("{\"mode\":\"sandbox\",\"ok\":true,\"refund\":\"%s\",\"amount\":\"%s\"}",
                refundNo, amount.toPlainString());
        return new RefundResult(refundNo, raw);
    }
}

package com.springboot.ylw.pay;

import com.springboot.ylw.entity.Order;
import com.springboot.ylw.entity.Payment;

import java.math.BigDecimal;

/**
 * 支付网关抽象。
 * 沙箱实现与真实支付宝/微信实现可共存，由配置 payment.gateway 选择。
 */
public interface PaymentGateway {

    /** 网关标识，对应配置 payment.gateway 的值（sandbox/alipay/wechat） */
    String getType();

    /**
     * 创建预支付单，返回可供前端渲染二维码或跳转的 payload。
     * 真实网关：调用 alipay.trade.precreate / wxpay.unifiedOrder
     * 沙箱：返回自定义字符串，前端渲染成假二维码
     */
    PrepayResult createPrepay(Order order);

    /** 主动查询第三方支付状态（真实网关用，沙箱直接返回 false 依赖手动通知） */
    default boolean queryPaid(String orderNo) { return false; }

    /**
     * 原路退款。
     * 真实网关：调用 alipay.trade.refund / wxpay.refund
     * 沙箱：返回伪退款流水号，由上层将 payment.status 置 3
     */
    RefundResult refund(Order order, Payment payment, BigDecimal amount);

    class PrepayResult {
        /** 二维码内容或 H5 跳转链接 */
        public final String qrPayload;
        /** 第三方 trade_no（沙箱阶段可置空） */
        public final String tradeNo;

        public PrepayResult(String qrPayload, String tradeNo) {
            this.qrPayload = qrPayload;
            this.tradeNo = tradeNo;
        }
    }

    class RefundResult {
        /** 退款流水号 */
        public final String refundNo;
        /** 网关原始响应（日志/对账用） */
        public final String rawResponse;

        public RefundResult(String refundNo, String rawResponse) {
            this.refundNo = refundNo;
            this.rawResponse = rawResponse;
        }
    }
}

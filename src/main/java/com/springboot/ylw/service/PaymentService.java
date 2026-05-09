package com.springboot.ylw.service;

import com.springboot.ylw.entity.Payment;

public interface PaymentService {
    /** 为订单创建（或复用）一笔待支付流水，返回给前端用于渲染二维码 */
    Payment createPrepay(Integer orderId, String payType);

    /** 沙箱模拟支付成功通知（真实场景由网关 POST 回调） */
    String sandboxNotify(String orderNo);

    /** 查询订单最新一笔支付 */
    Payment getByOrderId(Integer orderId);
}

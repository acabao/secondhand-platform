package com.springboot.ylw.service;

import com.springboot.ylw.entity.Refund;

import java.util.List;

public interface RefundService {

    /** 买家发起申请（订单需在已发货状态，且无进行中的申请） */
    Refund apply(Integer orderId, Integer buyerId, String reason, String imagesJson);

    /** 卖家同意退款（触发沙箱退款 + 关单 + 商品下架） */
    Refund sellerAgree(Integer refundId, Integer sellerId);

    /** 卖家拒绝退款（必须填写理由，后续仅允许仲裁） */
    Refund sellerReject(Integer refundId, Integer sellerId, String reply);

    /** 买家对"卖家拒绝"申请管理员仲裁（也适用于卖家48h未响应） */
    Refund applyArbitrate(Integer refundId, Integer buyerId);

    /** 管理员裁决同意（触发沙箱退款） */
    Refund adminAgree(Integer refundId, String note);

    /** 管理员裁决拒绝（维持卖家拒绝，订单继续正常流转） */
    Refund adminReject(Integer refundId, String note);

    Refund getById(Integer id);
    Refund getByOrderId(Integer orderId);
    List<Refund> listByBuyer(Integer buyerId);
    List<Refund> listBySeller(Integer sellerId);
    List<Refund> listArbitrating();
    List<Refund> listAll();
}

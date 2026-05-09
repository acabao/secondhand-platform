package com.springboot.ylw.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Refund {
    private Integer id;
    private Integer orderId;
    private String orderNo;
    private Integer buyerId;
    private Integer sellerId;
    private BigDecimal amount;
    private String reason;
    /** 图片URL JSON 数组字符串 */
    private String images;
    /** 0待卖家处理 1卖家同意 2卖家拒绝 3买家申请仲裁 4仲裁同意 5仲裁拒绝 6已退款 */
    private Integer status;
    private String sellerReply;
    private String adminNote;
    private Date applyTime;
    private Date sellerRespondTime;
    private Date arbitrateTime;
    private Date adminRespondTime;
    private Date refundTime;
    /** 卖家响应截止时间 applyTime + 48h */
    private Date deadline;

    // 非数据库字段，用于前端展示聚合
    private String goodsTitle;
    private String goodsImage;
    private String buyerName;
    private String sellerName;
}

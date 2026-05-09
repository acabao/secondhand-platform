package com.springboot.ylw.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Payment {
    private Integer id;
    private Integer orderId;
    private String orderNo;
    /** alipay / wechat / sandbox */
    private String payType;
    private BigDecimal amount;
    private String tradeNo;
    private String qrPayload;
    /** 0待支付 1已支付 2已关闭 3已退款 */
    private Integer status;
    private String rawResponse;
    private Date createTime;
    private Date payTime;
}

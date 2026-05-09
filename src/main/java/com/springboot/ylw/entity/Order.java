package com.springboot.ylw.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order {
    private Integer id;
    private String orderNo;
    private Integer goodsId;
    private Integer buyerId;
    private Integer sellerId;
    private BigDecimal price;
    private Integer addressId;
    /** 1邮寄 2自提 */
    private Integer deliveryType;
    private Integer pickupPointId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date meetTime;
    /** 0待付款 1待发货 2待收货 3已完成 4已关闭 */
    private Integer status;
    private String remark;
    private String payType;
    private Date expireTime;
    private Date payTime;
    private Date shipTime;
    private Date completeTime;
    private Date closeTime;
    private Date settleTime;
    private Date createTime;
    private Date updateTime;

    // 非数据库字段，用于前端展示聚合
    private String goodsTitle;
    private String goodsImage;
    private String buyerName;
    private String sellerName;
    private String pickupPointName;
    private String pickupPointAddress;
}

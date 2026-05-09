package com.springboot.ylw.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Review {
    private Integer id;
    private Integer orderId;
    private String orderNo;
    private Integer goodsId;
    private Integer fromUserId;
    private Integer toUserId;
    /** 0 买家评卖家 / 1 卖家评买家 */
    private Integer role;
    private Integer item1Score;
    private Integer item2Score;
    private Integer item3Score;
    private BigDecimal overallScore;
    private String content;
    private String images;
    /** 0 用户评价 / 1 系统默认好评 */
    private Integer autoDefault;
    private Date createTime;

    // 非数据库字段
    private String fromUserName;
    private String fromUserAvatar;
    private String goodsTitle;
    private String goodsImage;
}

package com.springboot.ylw.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Goods {
    private Integer id;
    private Integer userId;
    private Integer categoryId;
    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String images;
    private Integer conditionLevel;
    private Integer status;
    private Integer viewCount;
    private Date createTime;
    private Date updateTime;
}

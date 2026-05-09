package com.springboot.ylw.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Comment {
    private Integer id;
    private Integer goodsId;
    private Integer userId;
    private String content;
    private Integer rating;
    private Date createTime;
}

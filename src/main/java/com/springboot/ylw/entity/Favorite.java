package com.springboot.ylw.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Favorite {
    private Integer id;
    private Integer userId;
    private Integer goodsId;
    private Date createTime;
}

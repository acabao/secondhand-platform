package com.springboot.ylw.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Notice {
    private Integer id;
    private String title;
    private String content;
    private Integer adminId;
    private Integer status;
    private Date createTime;
}

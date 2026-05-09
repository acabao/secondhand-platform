package com.springboot.ylw.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Message {
    private Integer id;
    private Integer fromId;
    private Integer toId;
    private Integer goodsId;
    private String content;
    private Integer isRead;
    private Date createTime;
    // 非数据库字段，用于收件箱聚合展示
    private Integer unreadCount;
}

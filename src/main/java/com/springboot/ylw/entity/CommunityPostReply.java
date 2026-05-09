package com.springboot.ylw.entity;

import lombok.Data;
import java.util.Date;

@Data
public class CommunityPostReply {
    private Integer id;
    private Integer postId;
    private Integer userId;
    private String content;
    private Date createTime;

    // 非数据库字段
    private String userNickname;
    private String userAvatar;
}

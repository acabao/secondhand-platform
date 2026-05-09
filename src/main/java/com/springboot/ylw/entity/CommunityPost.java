package com.springboot.ylw.entity;

import lombok.Data;
import java.util.Date;

@Data
public class CommunityPost {
    private Integer id;
    private Integer userId;
    /** 1求购 2赠送 3换物 */
    private Integer type;
    private String title;
    private String description;
    private String images;
    private String wantItem;
    private String location;
    private String contact;
    /** 1进行中 2已完成 3已关闭 */
    private Integer status;
    private Integer viewCount;
    private Integer replyCount;
    private Date createTime;
    private Date updateTime;

    // 非数据库字段：发帖人信息
    private String userNickname;
    private String userAvatar;
}

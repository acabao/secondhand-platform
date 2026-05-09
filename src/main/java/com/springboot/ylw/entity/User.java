package com.springboot.ylw.entity;

import lombok.Data;
import java.util.Date;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private String phone;
    private String email;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private Date lastActiveTime;
}

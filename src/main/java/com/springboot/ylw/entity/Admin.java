package com.springboot.ylw.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Admin {
    private Integer id;
    private String username;
    private String password;
    private Integer role;
    private Date createTime;
}

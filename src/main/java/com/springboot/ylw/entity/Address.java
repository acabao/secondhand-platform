package com.springboot.ylw.entity;

import lombok.Data;

@Data
public class Address {
    private Integer id;
    private Integer userId;
    private String receiver;
    private String phone;
    private String detail;
    private Integer isDefault;
}

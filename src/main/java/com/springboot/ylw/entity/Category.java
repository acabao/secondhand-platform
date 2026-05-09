package com.springboot.ylw.entity;

import lombok.Data;

@Data
public class Category {
    private Integer id;
    private String name;
    private String icon;
    private Integer sort;
    private Integer status;
}

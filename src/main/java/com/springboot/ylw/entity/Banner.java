package com.springboot.ylw.entity;

import lombok.Data;

@Data
public class Banner {
    private Integer id;
    private String image;
    private String link;
    private Integer sort;
    private Integer status;
}

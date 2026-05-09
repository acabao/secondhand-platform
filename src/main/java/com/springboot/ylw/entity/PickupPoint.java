package com.springboot.ylw.entity;

import lombok.Data;
import java.util.Date;

@Data
public class PickupPoint {
    private Integer id;
    private String name;
    private String address;
    private String description;
    private String hours;
    private Integer sort;
    /** 1启用 0停用 */
    private Integer status;
    private Date createTime;
}

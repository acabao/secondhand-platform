package com.springboot.ylw.entity;

import lombok.Data;
import java.util.Date;

@Data
public class SmsCode {
    private Integer id;
    private String phone;
    private String code;
    /** register / reset */
    private String purpose;
    private Date expireTime;
    /** 0 未使用 / 1 已使用 */
    private Integer used;
    private Date createTime;
}

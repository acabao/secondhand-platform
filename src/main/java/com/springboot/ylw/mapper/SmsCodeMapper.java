package com.springboot.ylw.mapper;

import com.springboot.ylw.entity.SmsCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface SmsCodeMapper {

    int insert(SmsCode smsCode);

    /** 找出该手机号+用途下最近一次验证码发送记录 */
    SmsCode selectLatest(@Param("phone") String phone, @Param("purpose") String purpose);

    /** 最近 24 小时该手机号发送次数（用于日发送量限流） */
    int countRecent(@Param("phone") String phone, @Param("since") Date since);

    /** 查询可用（未使用/未过期/code匹配）的最新记录 */
    SmsCode selectValid(@Param("phone") String phone,
                        @Param("code") String code,
                        @Param("purpose") String purpose,
                        @Param("now") Date now);

    int markUsed(@Param("id") Integer id);
}

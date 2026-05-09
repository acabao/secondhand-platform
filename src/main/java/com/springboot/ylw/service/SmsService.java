package com.springboot.ylw.service;

import java.util.Map;

public interface SmsService {

    /** 发送验证码。返回 Map 包含 expiresIn(秒) 以及沙箱模式下的 code（演示用）。 */
    Map<String, Object> sendCode(String phone, String purpose);

    /** 校验验证码。通过则标记已使用，未通过抛异常。 */
    void verifyCode(String phone, String code, String purpose);
}

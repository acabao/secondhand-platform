package com.springboot.ylw.sms;

/**
 * 短信服务抽象。
 * - 沙箱实现：将验证码回传给 Service 由前端 toast 展示，不真正外发
 * - 真实实现：后续可新增阿里云/腾讯云 AliyunSmsGateway 等，只需注册为 @Component 并返回 getType()
 */
public interface SmsGateway {

    /** 网关类型，例如 sandbox / aliyun / tencent */
    String getType();

    /**
     * 发送短信验证码。
     * @return 沙箱模式下返回 code 便于前端演示；真实网关返回 null
     */
    String send(String phone, String code, String purpose);
}

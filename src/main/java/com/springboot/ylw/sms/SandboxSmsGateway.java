package com.springboot.ylw.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 沙箱短信网关：不真正外发，仅记录日志并回传验证码让前端直接显示。
 * 与支付沙箱同一思路，方便演示；真实环境新增 AliyunSmsGateway 注入即可切换。
 */
@Component
public class SandboxSmsGateway implements SmsGateway {

    private static final Logger log = LoggerFactory.getLogger(SandboxSmsGateway.class);

    @Override
    public String getType() {
        return "sandbox";
    }

    @Override
    public String send(String phone, String code, String purpose) {
        log.info("[SMS-SANDBOX] phone={} purpose={} code={} (五分钟内有效)", phone, purpose, code);
        return code;
    }
}

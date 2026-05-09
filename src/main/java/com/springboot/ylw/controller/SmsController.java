package com.springboot.ylw.controller;

import com.springboot.ylw.common.Result;
import com.springboot.ylw.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    /** 发送验证码。body: { phone, purpose: register/reset } */
    @PostMapping("/send")
    public Result<?> send(@RequestBody Map<String, String> body) {
        try {
            return Result.success(smsService.sendCode(body.get("phone"), body.get("purpose")));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

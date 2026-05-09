package com.springboot.ylw.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.springboot.ylw.entity.SmsCode;
import com.springboot.ylw.entity.User;
import com.springboot.ylw.mapper.SmsCodeMapper;
import com.springboot.ylw.mapper.UserMapper;
import com.springboot.ylw.service.SmsService;
import com.springboot.ylw.sms.SmsGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SmsServiceImpl implements SmsService {

    private static final int EXPIRE_SECONDS = 5 * 60;
    private static final int MIN_RESEND_SECONDS = 60;
    private static final int DAILY_LIMIT = 10;
    private static final Pattern PHONE_REGEX = Pattern.compile("^1[3-9]\\d{9}$");

    @Autowired private SmsCodeMapper smsCodeMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private List<SmsGateway> gateways;

    @Value("${sms.gateway:sandbox}")
    private String configuredGateway;

    private SmsGateway pickGateway() {
        Map<String, SmsGateway> byType = gateways.stream()
                .collect(Collectors.toMap(SmsGateway::getType, g -> g));
        SmsGateway g = byType.get(configuredGateway);
        if (g == null) throw new RuntimeException("未配置可用短信网关: " + configuredGateway);
        return g;
    }

    @Override
    public Map<String, Object> sendCode(String phone, String purpose) {
        if (phone == null || !PHONE_REGEX.matcher(phone).matches()) {
            throw new RuntimeException("请输入有效的手机号");
        }
        if (!"register".equals(purpose) && !"reset".equals(purpose)) {
            throw new RuntimeException("无效的验证码用途");
        }

        // 不同用途对是否绑定用户的预校验不同
        User existing = userMapper.selectByPhone(phone);
        if ("register".equals(purpose) && existing != null) {
            throw new RuntimeException("该手机号已注册，请直接登录或找回密码");
        }
        if ("reset".equals(purpose) && existing == null) {
            throw new RuntimeException("该手机号未注册账号");
        }

        // 60s 内不能重发
        SmsCode latest = smsCodeMapper.selectLatest(phone, purpose);
        if (latest != null) {
            long gap = (System.currentTimeMillis() - latest.getCreateTime().getTime()) / 1000;
            if (gap < MIN_RESEND_SECONDS) {
                throw new RuntimeException("发送过于频繁，请 " + (MIN_RESEND_SECONDS - gap) + " 秒后再试");
            }
        }

        // 日限额
        Calendar since = Calendar.getInstance();
        since.add(Calendar.DAY_OF_MONTH, -1);
        int recent = smsCodeMapper.countRecent(phone, since.getTime());
        if (recent >= DAILY_LIMIT) {
            throw new RuntimeException("今日验证码发送次数已达上限，请明天再试");
        }

        String code = RandomUtil.randomNumbers(6);
        Calendar expire = Calendar.getInstance();
        expire.add(Calendar.SECOND, EXPIRE_SECONDS);

        SmsCode record = new SmsCode();
        record.setPhone(phone);
        record.setCode(code);
        record.setPurpose(purpose);
        record.setExpireTime(expire.getTime());
        smsCodeMapper.insert(record);

        String sandboxCode = pickGateway().send(phone, code, purpose);

        Map<String, Object> resp = new HashMap<>();
        resp.put("expiresIn", EXPIRE_SECONDS);
        resp.put("resendAfter", MIN_RESEND_SECONDS);
        if (sandboxCode != null) resp.put("sandboxCode", sandboxCode);
        return resp;
    }

    @Override
    public void verifyCode(String phone, String code, String purpose) {
        if (phone == null || code == null || code.isEmpty()) {
            throw new RuntimeException("请填写手机号与验证码");
        }
        SmsCode valid = smsCodeMapper.selectValid(phone, code.trim(), purpose, new Date());
        if (valid == null) {
            throw new RuntimeException("验证码错误或已过期");
        }
        smsCodeMapper.markUsed(valid.getId());
    }
}

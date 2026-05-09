package com.springboot.ylw.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.springboot.ylw.entity.User;
import com.springboot.ylw.mapper.UserMapper;
import com.springboot.ylw.service.SmsService;
import com.springboot.ylw.service.UserService;
import com.springboot.ylw.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private static final Pattern PHONE_REGEX = Pattern.compile("^1[3-9]\\d{9}$");

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SmsService smsService;

    @Override
    public Map<String, Object> login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被封禁");
        }
        if (!DigestUtil.md5Hex(password).equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        String token = JwtUtil.generateToken(user.getId(), "user");
        user.setPassword(null);
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        return result;
    }

    @Override
    public String register(User user, String code) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new RuntimeException("请输入用户名");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new RuntimeException("密码至少 6 位");
        }
        if (user.getPhone() == null || !PHONE_REGEX.matcher(user.getPhone()).matches()) {
            throw new RuntimeException("请输入有效的手机号");
        }
        if (userMapper.selectByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        if (userMapper.selectByPhone(user.getPhone()) != null) {
            throw new RuntimeException("该手机号已注册");
        }

        smsService.verifyCode(user.getPhone(), code, "register");

        user.setPassword(DigestUtil.md5Hex(user.getPassword()));
        user.setStatus(1);
        if (user.getNickname() == null || user.getNickname().isEmpty()) {
            user.setNickname(user.getUsername());
        }
        userMapper.insert(user);
        return "注册成功";
    }

    @Override
    public String resetPassword(String phone, String code, String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            throw new RuntimeException("新密码至少 6 位");
        }
        User user = userMapper.selectByPhone(phone);
        if (user == null) {
            throw new RuntimeException("该手机号未注册");
        }
        smsService.verifyCode(phone, code, "reset");

        User update = new User();
        update.setId(user.getId());
        update.setPassword(DigestUtil.md5Hex(newPassword));
        userMapper.update(update);
        return "密码重置成功";
    }

    @Override
    public User getById(Integer id) {
        User user = userMapper.selectById(id);
        if (user != null) user.setPassword(null);
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = userMapper.selectAll();
        users.forEach(u -> u.setPassword(null));
        return users;
    }

    @Override
    public String updateInfo(User user) {
        userMapper.update(user);
        return "更新成功";
    }

    @Override
    public String updatePassword(Integer userId, String oldPwd, String newPwd) {
        User user = userMapper.selectById(userId);
        if (!DigestUtil.md5Hex(oldPwd).equals(user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        User update = new User();
        update.setId(userId);
        update.setPassword(DigestUtil.md5Hex(newPwd));
        userMapper.update(update);
        return "密码修改成功";
    }

    @Override
    public String updateStatus(Integer id, Integer status) {
        User update = new User();
        update.setId(id);
        update.setStatus(status);
        userMapper.update(update);
        return status == 1 ? "已解封" : "已封禁";
    }

    @Override
    public void heartbeat(Integer id) {
        userMapper.updateLastActive(id);
    }
}

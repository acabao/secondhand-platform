package com.springboot.ylw.controller;

import com.springboot.ylw.common.Result;
import com.springboot.ylw.entity.User;
import com.springboot.ylw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> params) {
        try {
            return Result.success(userService.login(params.get("username"), params.get("password")));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody Map<String, Object> body) {
        try {
            User user = new User();
            user.setUsername((String) body.get("username"));
            user.setPassword((String) body.get("password"));
            user.setNickname((String) body.get("nickname"));
            user.setPhone((String) body.get("phone"));
            user.setEmail((String) body.get("email"));
            String code = (String) body.get("code");
            return Result.success(userService.register(user, code));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public Result<?> resetPassword(@RequestBody Map<String, String> body) {
        try {
            return Result.success(userService.resetPassword(
                    body.get("phone"), body.get("code"), body.get("newPassword")));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    @PutMapping("/info")
    public Result<?> updateInfo(@RequestBody User user) {
        try {
            return Result.success(userService.updateInfo(user));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/password")
    public Result<?> updatePassword(@RequestBody Map<String, Object> params) {
        try {
            Integer userId = (Integer) params.get("userId");
            String oldPwd = (String) params.get("oldPassword");
            String newPwd = (String) params.get("newPassword");
            return Result.success(userService.updatePassword(userId, oldPwd, newPwd));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/heartbeat/{id}")
    public Result<?> heartbeat(@PathVariable Integer id) {
        userService.heartbeat(id);
        return Result.success(null);
    }

    // 管理员接口
    @GetMapping("/list")
    public Result<?> getAll() {
        return Result.success(userService.getAll());
    }

    @PutMapping("/status/{id}/{status}")
    public Result<?> updateStatus(@PathVariable Integer id, @PathVariable Integer status) {
        return Result.success(userService.updateStatus(id, status));
    }
}

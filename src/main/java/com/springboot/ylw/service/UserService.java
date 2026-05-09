package com.springboot.ylw.service;

import com.springboot.ylw.entity.User;
import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String, Object> login(String username, String password);
    String register(User user, String code);
    String resetPassword(String phone, String code, String newPassword);
    User getById(Integer id);
    List<User> getAll();
    String updateInfo(User user);
    String updatePassword(Integer userId, String oldPwd, String newPwd);
    String updateStatus(Integer id, Integer status);
    void heartbeat(Integer id);
}

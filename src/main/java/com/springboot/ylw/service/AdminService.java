package com.springboot.ylw.service;

import com.springboot.ylw.entity.Admin;
import java.util.List;
import java.util.Map;

public interface AdminService {
    Map<String, Object> login(String username, String password);
    List<Admin> getAll();
    String addAdmin(Admin admin);
    String deleteAdmin(Integer id);
    String updatePassword(Integer id, String oldPwd, String newPwd);
}

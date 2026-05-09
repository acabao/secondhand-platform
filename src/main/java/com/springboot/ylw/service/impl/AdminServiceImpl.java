package com.springboot.ylw.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.springboot.ylw.entity.Admin;
import com.springboot.ylw.mapper.AdminMapper;
import com.springboot.ylw.service.AdminService;
import com.springboot.ylw.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Map<String, Object> login(String username, String password) {
        Admin admin = adminMapper.selectByUsername(username);
        if (admin == null) {
            throw new RuntimeException("管理员不存在");
        }
        if (!DigestUtil.md5Hex(password).equals(admin.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        String role = admin.getRole() == 2 ? "superAdmin" : "admin";
        String token = JwtUtil.generateToken(admin.getId(), role);
        admin.setPassword(null);
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("admin", admin);
        return result;
    }

    @Override
    public List<Admin> getAll() {
        List<Admin> admins = adminMapper.selectAll();
        admins.forEach(a -> a.setPassword(null));
        return admins;
    }

    @Override
    public String addAdmin(Admin admin) {
        if (adminMapper.selectByUsername(admin.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        admin.setPassword(DigestUtil.md5Hex(admin.getPassword()));
        admin.setRole(1);
        adminMapper.insert(admin);
        return "添加成功";
    }

    @Override
    public String deleteAdmin(Integer id) {
        adminMapper.deleteById(id);
        return "删除成功";
    }

    @Override
    public String updatePassword(Integer id, String oldPwd, String newPwd) {
        Admin admin = adminMapper.selectById(id);
        if (!DigestUtil.md5Hex(oldPwd).equals(admin.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        Admin update = new Admin();
        update.setId(id);
        update.setPassword(DigestUtil.md5Hex(newPwd));
        adminMapper.update(update);
        return "密码修改成功";
    }
}

package com.springboot.ylw.controller;

import com.springboot.ylw.common.Result;
import com.springboot.ylw.entity.Admin;
import com.springboot.ylw.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> params) {
        try {
            return Result.success(adminService.login(params.get("username"), params.get("password")));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    public Result<?> getAll() {
        return Result.success(adminService.getAll());
    }

    @PostMapping("/add")
    public Result<?> addAdmin(@RequestBody Admin admin) {
        try {
            return Result.success(adminService.addAdmin(admin));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        return Result.success(adminService.deleteAdmin(id));
    }

    @PutMapping("/password")
    public Result<?> updatePassword(@RequestBody Map<String, Object> params) {
        try {
            Integer id = (Integer) params.get("id");
            String oldPwd = (String) params.get("oldPassword");
            String newPwd = (String) params.get("newPassword");
            return Result.success(adminService.updatePassword(id, oldPwd, newPwd));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

package com.springboot.ylw.controller;

import com.springboot.ylw.common.Result;
import com.springboot.ylw.entity.Notice;
import com.springboot.ylw.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/list")
    public Result<?> list() {
        return Result.success(noticeService.getAll());
    }

    @GetMapping("/admin/list")
    public Result<?> adminList() {
        return Result.success(noticeService.getAllForAdmin());
    }

    @PutMapping("/status/{id}/{status}")
    public Result<?> toggle(@PathVariable Integer id, @PathVariable Integer status) {
        return Result.success(noticeService.toggleStatus(id, status));
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Integer id) {
        return Result.success(noticeService.getById(id));
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody Notice notice) {
        return Result.success(noticeService.add(notice));
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody Notice notice) {
        return Result.success(noticeService.update(notice));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        return Result.success(noticeService.delete(id));
    }
}

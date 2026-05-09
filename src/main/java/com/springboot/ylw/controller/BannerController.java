package com.springboot.ylw.controller;

import com.springboot.ylw.common.Result;
import com.springboot.ylw.entity.Banner;
import com.springboot.ylw.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @GetMapping("/list")
    public Result<?> list() {
        return Result.success(bannerService.getAll());
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody Banner banner) {
        return Result.success(bannerService.add(banner));
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody Banner banner) {
        return Result.success(bannerService.update(banner));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        return Result.success(bannerService.delete(id));
    }
}

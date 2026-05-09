package com.springboot.ylw.controller;

import com.springboot.ylw.common.Result;
import com.springboot.ylw.entity.PickupPoint;
import com.springboot.ylw.service.PickupPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pickup")
public class PickupPointController {

    @Autowired
    private PickupPointService service;

    /** 用户下单可选：仅启用的 */
    @GetMapping("/list")
    public Result<?> list() {
        return Result.success(service.listActive());
    }

    /** 管理端：全部 */
    @GetMapping("/all")
    public Result<?> all() {
        return Result.success(service.listAll());
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Integer id) {
        return Result.success(service.getById(id));
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody PickupPoint point) {
        try { return Result.success(service.add(point)); }
        catch (Exception e) { return Result.error(e.getMessage()); }
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody PickupPoint point) {
        return Result.success(service.update(point));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        return Result.success(service.delete(id));
    }
}

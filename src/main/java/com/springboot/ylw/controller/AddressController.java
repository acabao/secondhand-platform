package com.springboot.ylw.controller;

import com.springboot.ylw.common.Result;
import com.springboot.ylw.entity.Address;
import com.springboot.ylw.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/list/{userId}")
    public Result<?> list(@PathVariable Integer userId) {
        return Result.success(addressService.getByUserId(userId));
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody Address address) {
        return Result.success(addressService.add(address));
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody Address address) {
        return Result.success(addressService.update(address));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        return Result.success(addressService.delete(id));
    }

    @PutMapping("/default")
    public Result<?> setDefault(@RequestBody Map<String, Integer> params) {
        return Result.success(addressService.setDefault(params.get("id"), params.get("userId")));
    }
}

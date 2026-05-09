package com.springboot.ylw.controller;

import com.springboot.ylw.common.Result;
import com.springboot.ylw.entity.Goods;
import com.springboot.ylw.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/list")
    public Result<?> list(@RequestParam(required = false) String keyword,
                          @RequestParam(required = false) Integer categoryId,
                          @RequestParam(required = false) Integer status,
                          @RequestParam(required = false) BigDecimal minPrice,
                          @RequestParam(required = false) BigDecimal maxPrice,
                          @RequestParam(required = false) String conditions,
                          @RequestParam(required = false) String timeRange,
                          @RequestParam(required = false) String sort) {
        List<Integer> conditionLevels = parseConditions(conditions);
        Date sinceTime = parseSinceTime(timeRange);
        return Result.success(goodsService.search(
                keyword, categoryId, status,
                minPrice, maxPrice, conditionLevels, sinceTime, sort));
    }

    private List<Integer> parseConditions(String conditions) {
        if (conditions == null || conditions.isEmpty()) return null;
        List<Integer> list = new ArrayList<>();
        for (String s : conditions.split(",")) {
            try {
                list.add(Integer.parseInt(s.trim()));
            } catch (NumberFormatException ignored) {}
        }
        return list.isEmpty() ? null : list;
    }

    /** 支持 3d / 7d / 30d */
    private Date parseSinceTime(String timeRange) {
        if (timeRange == null || timeRange.isEmpty()) return null;
        int days;
        switch (timeRange) {
            case "3d":  days = 3;  break;
            case "7d":  days = 7;  break;
            case "30d": days = 30; break;
            default: return null;
        }
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -days);
        return c.getTime();
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Integer id) {
        try {
            return Result.success(goodsService.getById(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my/{userId}")
    public Result<?> getMyGoods(@PathVariable Integer userId) {
        return Result.success(goodsService.getMyGoods(userId));
    }

    @PostMapping("/publish")
    public Result<?> publish(@RequestBody Goods goods) {
        try {
            return Result.success(goodsService.publish(goods));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody Goods goods) {
        return Result.success(goodsService.update(goods));
    }

    @PutMapping("/status/{id}/{status}")
    public Result<?> updateStatus(@PathVariable Integer id, @PathVariable Integer status) {
        return Result.success(goodsService.updateStatus(id, status));
    }
}

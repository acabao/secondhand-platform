package com.springboot.ylw.controller;

import com.springboot.ylw.common.Result;
import com.springboot.ylw.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/toggle")
    public Result<?> toggle(@RequestBody Map<String, Integer> params) {
        return Result.success(favoriteService.toggle(params.get("userId"), params.get("goodsId")));
    }

    @GetMapping("/list/{userId}")
    public Result<?> list(@PathVariable Integer userId) {
        return Result.success(favoriteService.getMyFavorites(userId));
    }

    @GetMapping("/check")
    public Result<?> check(@RequestParam Integer userId, @RequestParam Integer goodsId) {
        return Result.success(favoriteService.isFavorited(userId, goodsId));
    }
}

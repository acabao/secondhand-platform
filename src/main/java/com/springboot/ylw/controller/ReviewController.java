package com.springboot.ylw.controller;

import com.springboot.ylw.common.Result;
import com.springboot.ylw.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /** 提交评价 body: {orderId, fromUserId, role, item1, item2, item3, content, images} */
    @PostMapping("/submit")
    public Result<?> submit(@RequestBody Map<String, Object> body) {
        try {
            Integer orderId = toInt(body.get("orderId"));
            Integer fromUserId = toInt(body.get("fromUserId"));
            Integer role = toInt(body.get("role"));
            int item1 = toInt(body.get("item1"));
            int item2 = toInt(body.get("item2"));
            int item3 = toInt(body.get("item3"));
            String content = (String) body.get("content");
            Object imgs = body.get("images");
            String imagesJson = imgs == null ? null : imgs.toString();
            return Result.success(reviewService.submit(
                    orderId, fromUserId, role, item1, item2, item3, content, imagesJson));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<?> byId(@PathVariable Integer id) {
        return Result.success(reviewService.getById(id));
    }

    @GetMapping("/order/{orderId}/role/{role}")
    public Result<?> byOrder(@PathVariable Integer orderId, @PathVariable Integer role) {
        return Result.success(reviewService.getByOrderAndRole(orderId, role));
    }

    @GetMapping("/goods/{goodsId}")
    public Result<?> listByGoods(@PathVariable Integer goodsId) {
        return Result.success(reviewService.listByGoods(goodsId));
    }

    @GetMapping("/seller/{sellerId}")
    public Result<?> listBySeller(@PathVariable Integer sellerId) {
        return Result.success(reviewService.listBySeller(sellerId));
    }

    @GetMapping("/goods/{goodsId}/stats")
    public Result<?> goodsStats(@PathVariable Integer goodsId) {
        return Result.success(reviewService.statsForGoods(goodsId));
    }

    @GetMapping("/seller/{sellerId}/stats")
    public Result<?> sellerStats(@PathVariable Integer sellerId) {
        return Result.success(reviewService.statsForSeller(sellerId));
    }

    private int toInt(Object o) {
        if (o instanceof Integer) return (Integer) o;
        if (o instanceof Number) return ((Number) o).intValue();
        return Integer.parseInt(o.toString());
    }
}

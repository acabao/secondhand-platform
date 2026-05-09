package com.springboot.ylw.controller;

import com.springboot.ylw.common.Result;
import com.springboot.ylw.entity.Order;
import com.springboot.ylw.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public Result<?> create(@RequestBody Order order) {
        try {
            return Result.success(orderService.createOrder(order));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Integer id) {
        return Result.success(orderService.getById(id));
    }

    @GetMapping("/buy/{buyerId}")
    public Result<?> getBuyOrders(@PathVariable Integer buyerId) {
        return Result.success(orderService.getMyBuyOrders(buyerId));
    }

    @GetMapping("/sell/{sellerId}")
    public Result<?> getSellOrders(@PathVariable Integer sellerId) {
        return Result.success(orderService.getMySellOrders(sellerId));
    }

    @GetMapping("/list")
    public Result<?> getAll() {
        return Result.success(orderService.getAll());
    }

    /** 管理端直接改状态（兼容旧路径） */
    @PutMapping("/status/{id}/{status}")
    public Result<?> updateStatus(@PathVariable Integer id, @PathVariable Integer status) {
        return Result.success(orderService.updateStatus(id, status));
    }

    /** 卖家发货 */
    @PutMapping("/ship/{id}")
    public Result<?> ship(@PathVariable Integer id) {
        try { return Result.success(orderService.ship(id)); }
        catch (Exception e) { return Result.error(e.getMessage()); }
    }

    /** 买家确认收货 */
    @PutMapping("/receive/{id}")
    public Result<?> receive(@PathVariable Integer id) {
        try { return Result.success(orderService.confirmReceive(id)); }
        catch (Exception e) { return Result.error(e.getMessage()); }
    }

    /** 买家取消待付款订单 */
    @PutMapping("/cancel/{id}")
    public Result<?> cancel(@PathVariable Integer id) {
        try { return Result.success(orderService.cancel(id)); }
        catch (Exception e) { return Result.error(e.getMessage()); }
    }
}

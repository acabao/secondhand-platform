package com.springboot.ylw.controller;

import com.springboot.ylw.common.Result;
import com.springboot.ylw.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/refund")
public class RefundController {

    @Autowired
    private RefundService refundService;

    /** 买家发起退款申请 */
    @PostMapping("/apply")
    public Result<?> apply(@RequestBody Map<String, Object> body) {
        try {
            Integer orderId = (Integer) body.get("orderId");
            Integer buyerId = (Integer) body.get("buyerId");
            String reason = (String) body.get("reason");
            Object imgs = body.get("images");
            String imagesJson = imgs == null ? null : imgs.toString();
            return Result.success(refundService.apply(orderId, buyerId, reason, imagesJson));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /** 卖家同意 */
    @PutMapping("/seller/agree/{id}")
    public Result<?> sellerAgree(@PathVariable Integer id, @RequestParam Integer sellerId) {
        try { return Result.success(refundService.sellerAgree(id, sellerId)); }
        catch (Exception e) { return Result.error(e.getMessage()); }
    }

    /** 卖家拒绝 */
    @PutMapping("/seller/reject/{id}")
    public Result<?> sellerReject(@PathVariable Integer id,
                                  @RequestParam Integer sellerId,
                                  @RequestBody Map<String, String> body) {
        try { return Result.success(refundService.sellerReject(id, sellerId, body.get("reply"))); }
        catch (Exception e) { return Result.error(e.getMessage()); }
    }

    /** 买家申请平台仲裁 */
    @PutMapping("/arbitrate/{id}")
    public Result<?> arbitrate(@PathVariable Integer id, @RequestParam Integer buyerId) {
        try { return Result.success(refundService.applyArbitrate(id, buyerId)); }
        catch (Exception e) { return Result.error(e.getMessage()); }
    }

    /** 管理员同意 */
    @PutMapping("/admin/agree/{id}")
    public Result<?> adminAgree(@PathVariable Integer id, @RequestBody(required = false) Map<String, String> body) {
        try {
            String note = body == null ? null : body.get("note");
            return Result.success(refundService.adminAgree(id, note));
        } catch (Exception e) { return Result.error(e.getMessage()); }
    }

    /** 管理员拒绝 */
    @PutMapping("/admin/reject/{id}")
    public Result<?> adminReject(@PathVariable Integer id, @RequestBody(required = false) Map<String, String> body) {
        try {
            String note = body == null ? null : body.get("note");
            return Result.success(refundService.adminReject(id, note));
        } catch (Exception e) { return Result.error(e.getMessage()); }
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Integer id) {
        return Result.success(refundService.getById(id));
    }

    @GetMapping("/order/{orderId}")
    public Result<?> getByOrderId(@PathVariable Integer orderId) {
        return Result.success(refundService.getByOrderId(orderId));
    }

    @GetMapping("/buyer/{buyerId}")
    public Result<?> listByBuyer(@PathVariable Integer buyerId) {
        return Result.success(refundService.listByBuyer(buyerId));
    }

    @GetMapping("/seller/{sellerId}")
    public Result<?> listBySeller(@PathVariable Integer sellerId) {
        return Result.success(refundService.listBySeller(sellerId));
    }

    /** 管理员：仲裁池 */
    @GetMapping("/admin/arbitrating")
    public Result<?> listArbitrating() {
        return Result.success(refundService.listArbitrating());
    }

    /** 管理员：所有申请 */
    @GetMapping("/admin/list")
    public Result<?> listAll() {
        return Result.success(refundService.listAll());
    }
}

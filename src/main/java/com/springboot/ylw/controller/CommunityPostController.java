package com.springboot.ylw.controller;

import com.springboot.ylw.common.Result;
import com.springboot.ylw.entity.CommunityPost;
import com.springboot.ylw.entity.CommunityPostReply;
import com.springboot.ylw.service.CommunityPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class CommunityPostController {

    @Autowired
    private CommunityPostService postService;

    @GetMapping("/list")
    public Result<?> list(@RequestParam(required = false) Integer type,
                          @RequestParam(required = false) String keyword,
                          @RequestParam(required = false) Integer status) {
        return Result.success(postService.search(type, keyword, status));
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Integer id) {
        try {
            return Result.success(postService.getById(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my/{userId}")
    public Result<?> myPosts(@PathVariable Integer userId) {
        return Result.success(postService.getMyPosts(userId));
    }

    @PostMapping("/publish")
    public Result<?> publish(@RequestBody CommunityPost post) {
        try {
            return Result.success(postService.publish(post));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody CommunityPost post) {
        return Result.success(postService.update(post));
    }

    @PutMapping("/status/{id}/{status}")
    public Result<?> updateStatus(@PathVariable Integer id, @PathVariable Integer status) {
        return Result.success(postService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        return Result.success(postService.delete(id));
    }

    // ===== 回复 =====

    @GetMapping("/replies/{postId}")
    public Result<?> listReplies(@PathVariable Integer postId) {
        return Result.success(postService.listReplies(postId));
    }

    @PostMapping("/reply")
    public Result<?> addReply(@RequestBody CommunityPostReply reply) {
        try {
            return Result.success(postService.addReply(reply));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/reply/{id}")
    public Result<?> deleteReply(@PathVariable Integer id) {
        return Result.success(postService.deleteReply(id));
    }
}

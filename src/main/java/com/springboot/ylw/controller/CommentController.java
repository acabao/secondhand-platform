package com.springboot.ylw.controller;

import com.springboot.ylw.common.Result;
import com.springboot.ylw.entity.Comment;
import com.springboot.ylw.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/list/{goodsId}")
    public Result<?> list(@PathVariable Integer goodsId) {
        return Result.success(commentService.getByGoodsId(goodsId));
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody Comment comment) {
        return Result.success(commentService.addComment(comment));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        return Result.success(commentService.deleteComment(id));
    }
}

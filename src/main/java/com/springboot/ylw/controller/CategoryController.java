package com.springboot.ylw.controller;

import com.springboot.ylw.common.Result;
import com.springboot.ylw.entity.Category;
import com.springboot.ylw.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Result<?> list() {
        return Result.success(categoryService.getAll());
    }

    @GetMapping("/admin/list")
    public Result<?> adminList() {
        return Result.success(categoryService.getAllForAdmin());
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody Category category) {
        return Result.success(categoryService.add(category));
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody Category category) {
        return Result.success(categoryService.update(category));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        return Result.success(categoryService.delete(id));
    }
}

package com.springboot.ylw.service.impl;

import com.springboot.ylw.common.RedisCache;
import com.springboot.ylw.entity.Category;
import com.springboot.ylw.mapper.CategoryMapper;
import com.springboot.ylw.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final String KEY_ALL = "category:all";
    private static final long TTL = 3600;

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    public List<Category> getAll() {
        List<Category> cached = redisCache.get(KEY_ALL);
        if (cached != null) return cached;
        List<Category> list = categoryMapper.selectAll();
        redisCache.set(KEY_ALL, list, TTL);
        return list;
    }

    @Override
    public List<Category> getAllForAdmin() {
        return categoryMapper.selectAllAdmin();
    }

    @Override
    public String add(Category category) {
        categoryMapper.insert(category);
        redisCache.del(KEY_ALL);
        return "添加成功";
    }

    @Override
    public String update(Category category) {
        categoryMapper.update(category);
        redisCache.del(KEY_ALL);
        return "更新成功";
    }

    @Override
    public String delete(Integer id) {
        categoryMapper.deleteById(id);
        redisCache.del(KEY_ALL);
        return "删除成功";
    }
}

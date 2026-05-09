package com.springboot.ylw.service;

import com.springboot.ylw.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAll();
    List<Category> getAllForAdmin();
    String add(Category category);
    String update(Category category);
    String delete(Integer id);
}

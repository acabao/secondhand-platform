package com.springboot.ylw.mapper;

import com.springboot.ylw.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> selectAll();
    List<Category> selectAllAdmin();
    Category selectById(Integer id);
    int insert(Category category);
    int update(Category category);
    int deleteById(Integer id);
}

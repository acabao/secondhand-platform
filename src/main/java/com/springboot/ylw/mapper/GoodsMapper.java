package com.springboot.ylw.mapper;

import com.springboot.ylw.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface GoodsMapper {
    Goods selectById(Integer id);
    List<Goods> selectByPage(@Param("keyword") String keyword,
                             @Param("categoryId") Integer categoryId,
                             @Param("status") Integer status,
                             @Param("minPrice") BigDecimal minPrice,
                             @Param("maxPrice") BigDecimal maxPrice,
                             @Param("conditionLevels") List<Integer> conditionLevels,
                             @Param("sinceTime") Date sinceTime,
                             @Param("sortBy") String sortBy);
    List<Goods> selectByUserId(Integer userId);
    int insert(Goods goods);
    int update(Goods goods);
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);
    int incrementViewCount(Integer id);
}

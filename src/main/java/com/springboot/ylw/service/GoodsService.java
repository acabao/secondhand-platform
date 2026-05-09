package com.springboot.ylw.service;

import com.springboot.ylw.entity.Goods;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface GoodsService {
    List<Goods> search(String keyword, Integer categoryId, Integer status,
                       BigDecimal minPrice, BigDecimal maxPrice,
                       List<Integer> conditionLevels,
                       Date sinceTime, String sortBy);
    Goods getById(Integer id);
    List<Goods> getMyGoods(Integer userId);
    String publish(Goods goods);
    String update(Goods goods);
    String updateStatus(Integer id, Integer status);
}

package com.springboot.ylw.mapper;

import com.springboot.ylw.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FavoriteMapper {
    List<Favorite> selectByUserId(Integer userId);
    Favorite selectByUserAndGoods(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);
    int insert(Favorite favorite);
    int deleteByUserAndGoods(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);
}

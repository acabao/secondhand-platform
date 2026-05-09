package com.springboot.ylw.service.impl;

import com.springboot.ylw.entity.Favorite;
import com.springboot.ylw.mapper.FavoriteMapper;
import com.springboot.ylw.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public String toggle(Integer userId, Integer goodsId) {
        Favorite existing = favoriteMapper.selectByUserAndGoods(userId, goodsId);
        if (existing != null) {
            favoriteMapper.deleteByUserAndGoods(userId, goodsId);
            return "已取消收藏";
        } else {
            Favorite f = new Favorite();
            f.setUserId(userId);
            f.setGoodsId(goodsId);
            favoriteMapper.insert(f);
            return "收藏成功";
        }
    }

    @Override
    public List<Favorite> getMyFavorites(Integer userId) {
        return favoriteMapper.selectByUserId(userId);
    }

    @Override
    public boolean isFavorited(Integer userId, Integer goodsId) {
        return favoriteMapper.selectByUserAndGoods(userId, goodsId) != null;
    }
}

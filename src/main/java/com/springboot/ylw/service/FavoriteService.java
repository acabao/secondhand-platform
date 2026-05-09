package com.springboot.ylw.service;

import com.springboot.ylw.entity.Favorite;
import java.util.List;

public interface FavoriteService {
    String toggle(Integer userId, Integer goodsId);
    List<Favorite> getMyFavorites(Integer userId);
    boolean isFavorited(Integer userId, Integer goodsId);
}

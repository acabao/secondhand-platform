package com.springboot.ylw.service.impl;

import com.springboot.ylw.common.RedisCache;
import com.springboot.ylw.entity.Goods;
import com.springboot.ylw.mapper.GoodsMapper;
import com.springboot.ylw.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    private static final String KEY_DETAIL = "goods:detail:";
    private static final String KEY_LIST   = "goods:list:";
    private static final String KEY_MY     = "goods:my:";
    private static final long TTL_DETAIL = 600;
    private static final long TTL_LIST   = 60;

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    public List<Goods> search(String keyword, Integer categoryId, Integer status,
                              BigDecimal minPrice, BigDecimal maxPrice,
                              List<Integer> conditionLevels,
                              Date sinceTime, String sortBy) {
        String key = KEY_LIST + buildListKey(keyword, categoryId, status,
                minPrice, maxPrice, conditionLevels, sinceTime, sortBy);
        List<Goods> cached = redisCache.get(key);
        if (cached != null) return cached;
        List<Goods> list = goodsMapper.selectByPage(keyword, categoryId, status,
                minPrice, maxPrice, conditionLevels, sinceTime, sortBy);
        redisCache.set(key, list, TTL_LIST);
        return list;
    }

    @Override
    public Goods getById(Integer id) {
        String key = KEY_DETAIL + id;
        Goods cached = redisCache.get(key);
        if (cached != null) {
            goodsMapper.incrementViewCount(id);
            return cached;
        }
        Goods goods = goodsMapper.selectById(id);
        if (goods == null) throw new RuntimeException("商品不存在");
        goodsMapper.incrementViewCount(id);
        redisCache.set(key, goods, TTL_DETAIL);
        return goods;
    }

    @Override
    public List<Goods> getMyGoods(Integer userId) {
        String key = KEY_MY + userId;
        List<Goods> cached = redisCache.get(key);
        if (cached != null) return cached;
        List<Goods> list = goodsMapper.selectByUserId(userId);
        redisCache.set(key, list, TTL_LIST);
        return list;
    }

    @Override
    public String publish(Goods goods) {
        goodsMapper.insert(goods);
        invalidateAll(goods.getUserId());
        return "发布成功，等待审核";
    }

    @Override
    public String update(Goods goods) {
        goodsMapper.update(goods);
        redisCache.del(KEY_DETAIL + goods.getId());
        invalidateAll(goods.getUserId());
        return "更新成功";
    }

    @Override
    public String updateStatus(Integer id, Integer status) {
        goodsMapper.updateStatus(id, status);
        redisCache.del(KEY_DETAIL + id);
        redisCache.delByPrefix(KEY_LIST);
        redisCache.delByPrefix(KEY_MY);
        String[] msg = {"待审核", "已上架", "已售出", "已下架"};
        return "状态已更新为：" + msg[status];
    }

    private void invalidateAll(Integer userId) {
        redisCache.delByPrefix(KEY_LIST);
        if (userId != null) redisCache.del(KEY_MY + userId);
    }

    private String buildListKey(String keyword, Integer categoryId, Integer status,
                                BigDecimal minPrice, BigDecimal maxPrice,
                                List<Integer> conditionLevels,
                                Date sinceTime, String sortBy) {
        StringBuilder sb = new StringBuilder();
        sb.append("k=").append(keyword == null ? "" : keyword)
          .append("|c=").append(categoryId)
          .append("|s=").append(status)
          .append("|min=").append(minPrice)
          .append("|max=").append(maxPrice)
          .append("|cl=").append(conditionLevels)
          .append("|t=").append(sinceTime == null ? "" : sinceTime.getTime())
          .append("|o=").append(sortBy);
        return sb.toString();
    }
}

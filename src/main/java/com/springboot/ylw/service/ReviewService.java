package com.springboot.ylw.service;

import com.springboot.ylw.entity.Review;

import java.util.List;
import java.util.Map;

public interface ReviewService {

    /** 提交评价（买家 role=0，卖家 role=1） */
    Review submit(Integer orderId, Integer fromUserId, Integer role,
                  int item1, int item2, int item3,
                  String content, String imagesJson);

    /** 按主键查询（评价详情页） */
    Review getById(Integer id);

    /** 订单+方向查询（前端判断"已评价/待评价"） */
    Review getByOrderAndRole(Integer orderId, Integer role);

    /** 商品详情评价 tab 用 */
    List<Review> listByGoods(Integer goodsId);

    /** 卖家主页列表 */
    List<Review> listBySeller(Integer sellerId);

    /** 聚合：某卖家的统计（总数、好评率、三维均分、星级分布） */
    Map<String, Object> statsForSeller(Integer sellerId);

    /** 聚合：某商品的统计（同上但只基于该商品的买家评价） */
    Map<String, Object> statsForGoods(Integer goodsId);

    /** 定时任务：给超过 15 天仍未评价的订单插入系统默认好评 */
    int autoDefaultForOverdue();
}

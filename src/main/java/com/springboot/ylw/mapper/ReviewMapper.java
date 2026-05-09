package com.springboot.ylw.mapper;

import com.springboot.ylw.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper {

    int insert(Review review);

    Review selectById(Integer id);

    /** 查询某订单某方向评价 */
    Review selectByOrderAndRole(@Param("orderId") Integer orderId, @Param("role") Integer role);

    /** 商品详情页：买家评卖家 (role=0) 的全部评价 */
    List<Review> selectByGoodsId(@Param("goodsId") Integer goodsId);

    /** 卖家主页：所有指向该卖家的买家评价 */
    List<Review> selectBySellerId(@Param("sellerId") Integer sellerId);

    /** 某用户收到的所有评价（用于用户主页） */
    List<Review> selectByToUser(@Param("toUserId") Integer toUserId);

    /** 某用户发出的评价 */
    List<Review> selectByFromUser(@Param("fromUserId") Integer fromUserId);

    /** 查询"该卖家已完成订单且超过 15 天仍无评价"的订单 ID — 用于定时补默认好评 */
    List<java.util.Map<String, Object>> selectOverdueNoReview();
}

package com.springboot.ylw.mapper;

import com.springboot.ylw.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

@Mapper
public interface OrderMapper {
    Order selectById(Integer id);
    Order selectByOrderNo(String orderNo);
    List<Order> selectByBuyerId(Integer buyerId);
    List<Order> selectBySellerId(Integer sellerId);
    List<Order> selectAll();
    /** 扫描待付款且已超时的订单 */
    List<Order> selectExpired(@Param("now") Date now);
    int insert(Order order);
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);
    int markPaid(@Param("id") Integer id, @Param("payType") String payType, @Param("payTime") Date payTime);
    int markShipped(@Param("id") Integer id, @Param("shipTime") Date shipTime);
    int markCompleted(@Param("id") Integer id, @Param("completeTime") Date completeTime, @Param("settleTime") Date settleTime);
    int markClosed(@Param("id") Integer id, @Param("closeTime") Date closeTime);
    /** 退款成功后关闭订单（仅允许从已发货 status=2 转到已关闭 status=4） */
    int closeForRefund(@Param("id") Integer id, @Param("closeTime") Date closeTime);
}

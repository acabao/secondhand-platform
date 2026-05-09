package com.springboot.ylw.service;

import com.springboot.ylw.entity.Order;
import java.util.List;

public interface OrderService {
    /** 创建订单：返回新订单（含 id / orderNo / expireTime），用于前端跳转支付页 */
    Order createOrder(Order order);
    Order getById(Integer id);
    List<Order> getMyBuyOrders(Integer buyerId);
    List<Order> getMySellOrders(Integer sellerId);
    List<Order> getAll();
    String updateStatus(Integer id, Integer status);

    /** 支付成功：0→1，记录支付时间/方式，发系统消息给卖家 */
    void markPaid(Integer orderId, String payType);
    /** 卖家发货：1→2 */
    String ship(Integer orderId);
    /** 买家确认收货：2→3，打标 settle_time（T+1 结算） */
    String confirmReceive(Integer orderId);
    /** 买家主动取消（仅待付款状态）：0→4，恢复商品在售 */
    String cancel(Integer orderId);
    /** 关闭超时订单：由定时任务调用 */
    int closeExpired();
}

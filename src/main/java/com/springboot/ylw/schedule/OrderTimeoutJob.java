package com.springboot.ylw.schedule;

import com.springboot.ylw.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderTimeoutJob {

    private static final Logger log = LoggerFactory.getLogger(OrderTimeoutJob.class);

    @Autowired
    private OrderService orderService;

    /** 每 60s 扫描一次超时未支付订单并关闭 */
    @Scheduled(fixedDelay = 60_000L, initialDelay = 30_000L)
    public void closeExpiredOrders() {
        try {
            int n = orderService.closeExpired();
            if (n > 0) log.info("[OrderTimeoutJob] 已关闭 {} 笔超时订单", n);
        } catch (Exception e) {
            log.error("[OrderTimeoutJob] 扫单失败", e);
        }
    }
}

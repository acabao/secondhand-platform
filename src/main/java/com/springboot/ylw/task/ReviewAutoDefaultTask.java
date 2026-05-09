package com.springboot.ylw.task;

import com.springboot.ylw.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 交易评价 15 天超时默认好评。
 * 每天凌晨 3 点扫描：已完成超过 15 天但双方某一侧仍未评价的订单，自动补写 5 星系统评价。
 */
@Component
public class ReviewAutoDefaultTask {

    @Autowired
    private ReviewService reviewService;

    @Scheduled(cron = "0 0 3 * * ?")
    public void run() {
        reviewService.autoDefaultForOverdue();
    }
}

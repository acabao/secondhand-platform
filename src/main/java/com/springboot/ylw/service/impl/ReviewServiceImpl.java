package com.springboot.ylw.service.impl;

import com.springboot.ylw.entity.Order;
import com.springboot.ylw.entity.Review;
import com.springboot.ylw.mapper.OrderMapper;
import com.springboot.ylw.mapper.ReviewMapper;
import com.springboot.ylw.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewServiceImpl.class);
    /** 好评阈值：overall >= 4 视为好评 */
    private static final BigDecimal GOOD_THRESHOLD = new BigDecimal("4.00");

    @Autowired private ReviewMapper reviewMapper;
    @Autowired private OrderMapper orderMapper;

    @Override
    @Transactional
    public Review submit(Integer orderId, Integer fromUserId, Integer role,
                         int item1, int item2, int item3,
                         String content, String imagesJson) {
        if (role == null || (role != 0 && role != 1)) {
            throw new RuntimeException("无效的评价方向");
        }
        if (!validScore(item1) || !validScore(item2) || !validScore(item3)) {
            throw new RuntimeException("评分必须在 1-5 之间");
        }

        Order order = orderMapper.selectById(orderId);
        if (order == null) throw new RuntimeException("订单不存在");
        if (order.getStatus() != 3) throw new RuntimeException("仅已完成订单可评价");

        Integer expectedFrom = role == 0 ? order.getBuyerId() : order.getSellerId();
        Integer toUserId = role == 0 ? order.getSellerId() : order.getBuyerId();
        if (!expectedFrom.equals(fromUserId)) {
            throw new RuntimeException("无权评价该订单");
        }

        if (reviewMapper.selectByOrderAndRole(orderId, role) != null) {
            throw new RuntimeException("您已经评价过该订单");
        }

        Review r = new Review();
        r.setOrderId(order.getId());
        r.setOrderNo(order.getOrderNo());
        r.setGoodsId(order.getGoodsId());
        r.setFromUserId(fromUserId);
        r.setToUserId(toUserId);
        r.setRole(role);
        r.setItem1Score(item1);
        r.setItem2Score(item2);
        r.setItem3Score(item3);
        r.setOverallScore(avg(item1, item2, item3));
        r.setContent(content);
        r.setImages(imagesJson);
        r.setAutoDefault(0);
        reviewMapper.insert(r);
        return reviewMapper.selectById(r.getId());
    }

    @Override
    public Review getById(Integer id) {
        return reviewMapper.selectById(id);
    }

    @Override
    public Review getByOrderAndRole(Integer orderId, Integer role) {
        return reviewMapper.selectByOrderAndRole(orderId, role);
    }

    @Override
    public List<Review> listByGoods(Integer goodsId) {
        return reviewMapper.selectByGoodsId(goodsId);
    }

    @Override
    public List<Review> listBySeller(Integer sellerId) {
        return reviewMapper.selectBySellerId(sellerId);
    }

    @Override
    public Map<String, Object> statsForSeller(Integer sellerId) {
        return aggregate(reviewMapper.selectBySellerId(sellerId));
    }

    @Override
    public Map<String, Object> statsForGoods(Integer goodsId) {
        return aggregate(reviewMapper.selectByGoodsId(goodsId));
    }

    @Override
    @Transactional
    public int autoDefaultForOverdue() {
        List<Map<String, Object>> overdueOrders = reviewMapper.selectOverdueNoReview();
        int inserted = 0;
        for (Map<String, Object> row : overdueOrders) {
            Integer orderId = toInt(row.get("orderId"));
            String orderNo = (String) row.get("orderNo");
            Integer goodsId = toInt(row.get("goodsId"));
            Integer buyerId = toInt(row.get("buyerId"));
            Integer sellerId = toInt(row.get("sellerId"));
            Integer buyerReviewed = toInt(row.get("buyerReviewed"));
            Integer sellerReviewed = toInt(row.get("sellerReviewed"));

            if (buyerReviewed == null || buyerReviewed == 0) {
                insertDefault(orderId, orderNo, goodsId, buyerId, sellerId, 0);
                inserted++;
            }
            if (sellerReviewed == null || sellerReviewed == 0) {
                insertDefault(orderId, orderNo, goodsId, sellerId, buyerId, 1);
                inserted++;
            }
        }
        if (inserted > 0) {
            log.info("[review] 超时默认好评自动补写 {} 条", inserted);
        }
        return inserted;
    }

    private void insertDefault(Integer orderId, String orderNo, Integer goodsId,
                               Integer fromUserId, Integer toUserId, Integer role) {
        Review r = new Review();
        r.setOrderId(orderId);
        r.setOrderNo(orderNo);
        r.setGoodsId(goodsId);
        r.setFromUserId(fromUserId);
        r.setToUserId(toUserId);
        r.setRole(role);
        r.setItem1Score(5);
        r.setItem2Score(5);
        r.setItem3Score(5);
        r.setOverallScore(new BigDecimal("5.00"));
        r.setContent("系统默认好评（超时未评价）");
        r.setAutoDefault(1);
        try {
            reviewMapper.insert(r);
        } catch (Exception e) {
            // 唯一键冲突：并发下另一端已先插入，忽略
            log.debug("默认好评插入冲突 orderId={} role={}", orderId, role);
        }
    }

    // ===== helpers =====

    private boolean validScore(int v) { return v >= 1 && v <= 5; }

    private BigDecimal avg(int a, int b, int c) {
        return BigDecimal.valueOf(a + b + c)
                .divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
    }

    private Map<String, Object> aggregate(List<Review> list) {
        Map<String, Object> m = new HashMap<>();
        int total = list.size();
        m.put("total", total);
        if (total == 0) {
            m.put("goodRate", null);
            m.put("avg", null);
            m.put("item1Avg", null);
            m.put("item2Avg", null);
            m.put("item3Avg", null);
            m.put("distribution", new int[]{0, 0, 0, 0, 0});
            return m;
        }

        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal s1 = BigDecimal.ZERO, s2 = BigDecimal.ZERO, s3 = BigDecimal.ZERO;
        int good = 0;
        int[] dist = new int[5]; // idx 0 = 5星, 1 = 4星 ... 4 = 1星
        for (Review r : list) {
            sum = sum.add(r.getOverallScore());
            s1 = s1.add(BigDecimal.valueOf(r.getItem1Score()));
            s2 = s2.add(BigDecimal.valueOf(r.getItem2Score()));
            s3 = s3.add(BigDecimal.valueOf(r.getItem3Score()));
            if (r.getOverallScore().compareTo(GOOD_THRESHOLD) >= 0) good++;
            int star = (int) Math.round(r.getOverallScore().doubleValue());
            if (star < 1) star = 1;
            if (star > 5) star = 5;
            dist[5 - star]++;
        }
        BigDecimal totalBd = BigDecimal.valueOf(total);
        m.put("avg", sum.divide(totalBd, 2, RoundingMode.HALF_UP));
        m.put("item1Avg", s1.divide(totalBd, 2, RoundingMode.HALF_UP));
        m.put("item2Avg", s2.divide(totalBd, 2, RoundingMode.HALF_UP));
        m.put("item3Avg", s3.divide(totalBd, 2, RoundingMode.HALF_UP));
        m.put("goodRate", BigDecimal.valueOf(good)
                .multiply(BigDecimal.valueOf(100))
                .divide(totalBd, 1, RoundingMode.HALF_UP));
        m.put("distribution", dist);
        return m;
    }

    private Integer toInt(Object o) {
        if (o == null) return null;
        if (o instanceof Integer) return (Integer) o;
        if (o instanceof Long) return ((Long) o).intValue();
        if (o instanceof Number) return ((Number) o).intValue();
        return Integer.parseInt(o.toString());
    }
}

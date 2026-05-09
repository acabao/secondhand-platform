-- ============================================================
-- P0 售后退款数据迁移脚本（在已有 ylw 库上增量执行）
-- 执行方式：mysql -uroot -p123456 ylw < migration_refund.sql
-- ============================================================
USE ylw;

CREATE TABLE IF NOT EXISTS `refund` (
  `id`                   INT PRIMARY KEY AUTO_INCREMENT,
  `order_id`             INT            NOT NULL          COMMENT '订单ID',
  `order_no`             VARCHAR(50)    NOT NULL          COMMENT '订单号冗余',
  `buyer_id`             INT            NOT NULL          COMMENT '买家ID',
  `seller_id`            INT            NOT NULL          COMMENT '卖家ID',
  `amount`               DECIMAL(10,2)  NOT NULL          COMMENT '退款金额',
  `reason`               VARCHAR(500)   NOT NULL          COMMENT '买家退款原因',
  `images`               TEXT                             COMMENT '凭证图片JSON数组',
  `status`               TINYINT        DEFAULT 0         COMMENT '0待卖家处理 1卖家同意 2卖家拒绝 3买家申请仲裁 4仲裁同意 5仲裁拒绝 6已退款',
  `seller_reply`         VARCHAR(500)                     COMMENT '卖家拒绝原因',
  `admin_note`           VARCHAR(500)                     COMMENT '管理员仲裁说明',
  `apply_time`           DATETIME       DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `seller_respond_time`  DATETIME                         COMMENT '卖家响应时间',
  `arbitrate_time`       DATETIME                         COMMENT '买家申请仲裁时间',
  `admin_respond_time`   DATETIME                         COMMENT '管理员裁决时间',
  `refund_time`          DATETIME                         COMMENT '退款完成时间',
  `deadline`             DATETIME       NOT NULL          COMMENT '卖家响应截止 apply_time+48h',
  UNIQUE KEY `uk_order_id` (`order_id`),
  INDEX `idx_buyer` (`buyer_id`),
  INDEX `idx_seller` (`seller_id`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退款/售后申请表';

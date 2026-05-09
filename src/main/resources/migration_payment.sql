-- ============================================================
-- P0 支付闭环数据迁移脚本（在已有 ylw 库上增量执行）
-- 执行方式：mysql -uroot -p123456 ylw < migration_payment.sql
-- ============================================================
USE ylw;

-- 订单表新增字段
ALTER TABLE `order`
  ADD COLUMN `pay_type`      VARCHAR(20)  COMMENT '支付方式: alipay/wechat/sandbox' AFTER `remark`,
  ADD COLUMN `expire_time`   DATETIME     COMMENT '支付超时时间' AFTER `pay_type`,
  ADD COLUMN `pay_time`      DATETIME     COMMENT '支付成功时间' AFTER `expire_time`,
  ADD COLUMN `ship_time`     DATETIME     COMMENT '发货时间' AFTER `pay_time`,
  ADD COLUMN `complete_time` DATETIME     COMMENT '确认收货时间' AFTER `ship_time`,
  ADD COLUMN `close_time`    DATETIME     COMMENT '订单关闭时间' AFTER `complete_time`,
  ADD COLUMN `settle_time`   DATETIME     COMMENT '平台结算时间(T+1打款给卖家)' AFTER `close_time`,
  ADD INDEX `idx_status_expire` (`status`, `expire_time`);

-- 订单状态注释更新：4 由"已取消"改为"已关闭"，含义扩展为取消/超时关闭
ALTER TABLE `order`
  MODIFY COLUMN `status` TINYINT DEFAULT 0 COMMENT '0待付款 1待发货 2待收货 3已完成 4已关闭';

-- 支付流水表
CREATE TABLE IF NOT EXISTS `payment` (
  `id`           INT PRIMARY KEY AUTO_INCREMENT,
  `order_id`     INT            NOT NULL            COMMENT '订单ID',
  `order_no`     VARCHAR(50)    NOT NULL            COMMENT '订单号',
  `pay_type`     VARCHAR(20)    NOT NULL            COMMENT 'alipay/wechat/sandbox',
  `amount`       DECIMAL(10,2)  NOT NULL            COMMENT '支付金额',
  `trade_no`     VARCHAR(64)                        COMMENT '第三方交易号',
  `qr_payload`   VARCHAR(512)                       COMMENT '支付二维码内容/跳转链接',
  `status`       TINYINT        DEFAULT 0           COMMENT '0待支付 1已支付 2已关闭 3已退款',
  `raw_response` TEXT                               COMMENT '网关原始响应',
  `create_time`  DATETIME       DEFAULT CURRENT_TIMESTAMP,
  `pay_time`     DATETIME                           COMMENT '支付成功时间',
  INDEX `idx_order_id` (`order_id`),
  INDEX `idx_order_no` (`order_no`),
  INDEX `idx_trade_no` (`trade_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付流水表';

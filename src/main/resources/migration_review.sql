-- 3.2.1 交易评价系统 迁移脚本
USE ylw;

CREATE TABLE IF NOT EXISTS `review` (
  `id`             INT PRIMARY KEY AUTO_INCREMENT,
  `order_id`       INT NOT NULL,
  `order_no`       VARCHAR(50) NOT NULL,
  `goods_id`       INT NOT NULL,
  `from_user_id`   INT NOT NULL                      COMMENT '评价人',
  `to_user_id`     INT NOT NULL                      COMMENT '被评价人',
  `role`           TINYINT NOT NULL                  COMMENT '0 买家评卖家 / 1 卖家评买家',
  -- role=0: item1=描述相符 item2=沟通态度 item3=发货速度
  -- role=1: item1=付款速度 item2=沟通态度 item3=信用度
  `item1_score`    TINYINT NOT NULL                  COMMENT '1-5 星',
  `item2_score`    TINYINT NOT NULL                  COMMENT '1-5 星',
  `item3_score`    TINYINT NOT NULL                  COMMENT '1-5 星',
  `overall_score`  DECIMAL(3,2) NOT NULL             COMMENT '三维平均分',
  `content`        VARCHAR(500)                      COMMENT '评价文本',
  `images`         TEXT                              COMMENT '图片 URL JSON 数组',
  `auto_default`   TINYINT DEFAULT 0                 COMMENT '0 用户评价 / 1 系统默认好评',
  `create_time`    DATETIME DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `uk_order_role` (`order_id`, `role`),
  INDEX `idx_to_user` (`to_user_id`),
  INDEX `idx_goods` (`goods_id`),
  INDEX `idx_from_user` (`from_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易评价';

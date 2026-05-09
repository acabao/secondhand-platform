-- 贡江社区二手商品交易平台 数据库初始化脚本
-- 创建并使用数据库
CREATE DATABASE IF NOT EXISTS ylw DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ylw;

-- 1. 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id`          INT PRIMARY KEY AUTO_INCREMENT,
  `username`    VARCHAR(50)  NOT NULL UNIQUE COMMENT '用户名',
  `password`    VARCHAR(100) NOT NULL        COMMENT '密码(加密)',
  `nickname`    VARCHAR(50)                  COMMENT '昵称',
  `avatar`      VARCHAR(255)                 COMMENT '头像URL',
  `phone`       VARCHAR(20)   UNIQUE         COMMENT '手机号（注册唯一）',
  `email`       VARCHAR(100)                 COMMENT '邮箱',
  `status`      TINYINT      DEFAULT 1       COMMENT '状态 1正常 0封禁',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 管理员表
CREATE TABLE IF NOT EXISTS `admin` (
  `id`          INT PRIMARY KEY AUTO_INCREMENT,
  `username`    VARCHAR(50)  NOT NULL UNIQUE COMMENT '用户名',
  `password`    VARCHAR(100) NOT NULL        COMMENT '密码(加密)',
  `role`        TINYINT      DEFAULT 1       COMMENT '1管理员 2超级管理员',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 3. 商品分类表
CREATE TABLE IF NOT EXISTS `category` (
  `id`     INT PRIMARY KEY AUTO_INCREMENT,
  `name`   VARCHAR(50)  NOT NULL COMMENT '分类名称',
  `icon`   VARCHAR(255)          COMMENT '分类图标',
  `sort`   INT          DEFAULT 0 COMMENT '排序(越小越靠前)',
  `status` TINYINT      DEFAULT 1 COMMENT '1启用 0禁用'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 4. 商品表
CREATE TABLE IF NOT EXISTS `goods` (
  `id`             INT            PRIMARY KEY AUTO_INCREMENT,
  `user_id`        INT            NOT NULL              COMMENT '发布者ID',
  `category_id`    INT            NOT NULL              COMMENT '分类ID',
  `title`          VARCHAR(100)   NOT NULL              COMMENT '商品标题',
  `description`    TEXT                                 COMMENT '商品描述',
  `price`          DECIMAL(10,2)  NOT NULL              COMMENT '售价',
  `original_price` DECIMAL(10,2)                        COMMENT '原价',
  `images`         TEXT                                 COMMENT '图片URL列表(JSON数组)',
  `condition_level` TINYINT                             COMMENT '新旧程度 1全新 2几乎全新 3有使用痕迹',
  `status`         TINYINT        DEFAULT 0             COMMENT '0待审核 1在售 2已售出 3下架',
  `view_count`     INT            DEFAULT 0             COMMENT '浏览量',
  `create_time`    DATETIME       DEFAULT CURRENT_TIMESTAMP,
  `update_time`    DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_category_id` (`category_id`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 5. 收货地址表
CREATE TABLE IF NOT EXISTS `address` (
  `id`         INT PRIMARY KEY AUTO_INCREMENT,
  `user_id`    INT          NOT NULL COMMENT '用户ID',
  `receiver`   VARCHAR(50)  NOT NULL COMMENT '收货人姓名',
  `phone`      VARCHAR(20)  NOT NULL COMMENT '联系电话',
  `detail`     VARCHAR(255) NOT NULL COMMENT '详细地址',
  `is_default` TINYINT      DEFAULT 0 COMMENT '是否默认地址 1是 0否',
  INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';

-- 6. 订单表
CREATE TABLE IF NOT EXISTS `order` (
  `id`            INT PRIMARY KEY AUTO_INCREMENT,
  `order_no`      VARCHAR(50)   NOT NULL UNIQUE COMMENT '订单号',
  `goods_id`      INT           NOT NULL        COMMENT '商品ID',
  `buyer_id`      INT           NOT NULL        COMMENT '买家ID',
  `seller_id`     INT           NOT NULL        COMMENT '卖家ID',
  `price`         DECIMAL(10,2) NOT NULL        COMMENT '成交价格',
  `address_id`    INT                           COMMENT '收货地址ID',
  `status`        TINYINT       DEFAULT 0       COMMENT '0待付款 1待发货 2待收货 3已完成 4已关闭',
  `remark`        VARCHAR(255)                  COMMENT '买家备注',
  `pay_type`      VARCHAR(20)                   COMMENT '支付方式: alipay/wechat/sandbox',
  `expire_time`   DATETIME                      COMMENT '支付超时时间',
  `pay_time`      DATETIME                      COMMENT '支付成功时间',
  `ship_time`     DATETIME                      COMMENT '发货时间',
  `complete_time` DATETIME                      COMMENT '确认收货时间',
  `close_time`    DATETIME                      COMMENT '订单关闭时间',
  `settle_time`   DATETIME                      COMMENT '平台结算时间(T+1打款给卖家)',
  `create_time`   DATETIME      DEFAULT CURRENT_TIMESTAMP,
  `update_time`   DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_buyer_id` (`buyer_id`),
  INDEX `idx_seller_id` (`seller_id`),
  INDEX `idx_goods_id` (`goods_id`),
  INDEX `idx_status_expire` (`status`, `expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 6.1 支付流水表
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

-- 7. 收藏表
CREATE TABLE IF NOT EXISTS `favorite` (
  `id`          INT PRIMARY KEY AUTO_INCREMENT,
  `user_id`     INT      NOT NULL,
  `goods_id`    INT      NOT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `uk_user_goods` (`user_id`, `goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- 8. 评论表
CREATE TABLE IF NOT EXISTS `comment` (
  `id`          INT PRIMARY KEY AUTO_INCREMENT,
  `goods_id`    INT          NOT NULL COMMENT '商品ID',
  `user_id`     INT          NOT NULL COMMENT '评论用户ID',
  `content`     VARCHAR(500) NOT NULL COMMENT '评论内容',
  `rating`      TINYINT               COMMENT '评分 1-5',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 9. 私信表
CREATE TABLE IF NOT EXISTS `message` (
  `id`          INT PRIMARY KEY AUTO_INCREMENT,
  `from_id`     INT          NOT NULL COMMENT '发送者ID',
  `to_id`       INT          NOT NULL COMMENT '接收者ID',
  `goods_id`    INT                   COMMENT '关联商品ID',
  `content`     VARCHAR(500) NOT NULL COMMENT '消息内容',
  `is_read`     TINYINT      DEFAULT 0 COMMENT '是否已读 1已读 0未读',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_from_to` (`from_id`, `to_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='私信表';

-- 10. 公告表
CREATE TABLE IF NOT EXISTS `notice` (
  `id`          INT PRIMARY KEY AUTO_INCREMENT,
  `title`       VARCHAR(100) NOT NULL COMMENT '公告标题',
  `content`     TEXT         NOT NULL COMMENT '公告内容',
  `admin_id`    INT                   COMMENT '发布管理员ID',
  `status`      TINYINT      DEFAULT 1 COMMENT '1发布 0下线',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- 11. 轮播图表
CREATE TABLE IF NOT EXISTS `banner` (
  `id`     INT PRIMARY KEY AUTO_INCREMENT,
  `image`  VARCHAR(255) NOT NULL COMMENT '图片URL',
  `link`   VARCHAR(255)          COMMENT '点击跳转链接',
  `sort`   INT          DEFAULT 0 COMMENT '排序(越小越靠前)',
  `status` TINYINT      DEFAULT 1 COMMENT '1显示 0隐藏'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮播图表';

-- 12. 退款/售后申请表
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

-- ===================== 初始化数据 =====================

-- 初始化超级管理员账号 (密码: admin123，实际使用时请替换为加密后的值)
-- 初始超级管理员账号 admin / admin123 (MD5加密)
INSERT INTO `admin` (`username`, `password`, `role`) VALUES ('admin', '0192023a7bbd73250516f069df18b500', 2);

-- 交易评价表（3.2.1 交易评价系统）
CREATE TABLE IF NOT EXISTS `review` (
  `id`             INT PRIMARY KEY AUTO_INCREMENT,
  `order_id`       INT NOT NULL,
  `order_no`       VARCHAR(50) NOT NULL,
  `goods_id`       INT NOT NULL,
  `from_user_id`   INT NOT NULL,
  `to_user_id`     INT NOT NULL,
  `role`           TINYINT NOT NULL COMMENT '0 买家评卖家 / 1 卖家评买家',
  `item1_score`    TINYINT NOT NULL,
  `item2_score`    TINYINT NOT NULL,
  `item3_score`    TINYINT NOT NULL,
  `overall_score`  DECIMAL(3,2) NOT NULL,
  `content`        VARCHAR(500),
  `images`         TEXT,
  `auto_default`   TINYINT DEFAULT 0,
  `create_time`    DATETIME DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `uk_order_role` (`order_id`, `role`),
  INDEX `idx_to_user` (`to_user_id`),
  INDEX `idx_goods` (`goods_id`),
  INDEX `idx_from_user` (`from_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易评价';

-- 短信验证码表（3.1.3 手机号注册验证）
CREATE TABLE IF NOT EXISTS `sms_code` (
  `id`           INT PRIMARY KEY AUTO_INCREMENT,
  `phone`        VARCHAR(20) NOT NULL COMMENT '手机号',
  `code`         VARCHAR(10) NOT NULL COMMENT '验证码',
  `purpose`      VARCHAR(20) NOT NULL COMMENT 'register / reset',
  `expire_time`  DATETIME    NOT NULL,
  `used`         TINYINT     DEFAULT 0,
  `create_time`  DATETIME    DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_phone_purpose` (`phone`, `purpose`),
  INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短信验证码';

-- 初始化商品分类
INSERT INTO `category` (`name`, `icon`, `sort`) VALUES
('数码电子', 'icon-digital', 1),
('服装鞋帽', 'icon-clothes', 2),
('书籍教材', 'icon-book',    3),
('家居用品', 'icon-home',    4),
('运动户外', 'icon-sport',   5),
('母婴玩具', 'icon-baby',    6),
('其他',     'icon-other',   7);

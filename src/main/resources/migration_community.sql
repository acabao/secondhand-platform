-- 邻里互助板块 迁移脚本
USE ylw;

-- 社区互助帖
CREATE TABLE IF NOT EXISTS `community_post` (
  `id`          INT PRIMARY KEY AUTO_INCREMENT,
  `user_id`     INT           NOT NULL            COMMENT '发帖人ID',
  `type`        TINYINT       NOT NULL            COMMENT '1求购 2赠送 3换物',
  `title`       VARCHAR(100)  NOT NULL            COMMENT '标题',
  `description` TEXT                              COMMENT '详细描述',
  `images`      TEXT                              COMMENT '图片URL JSON数组',
  `want_item`   VARCHAR(200)                      COMMENT '换物时想要换什么',
  `location`    VARCHAR(100)                      COMMENT '所在位置/小区/楼栋',
  `contact`     VARCHAR(100)                      COMMENT '联系方式(可选)',
  `status`      TINYINT       DEFAULT 1           COMMENT '1进行中 2已完成 3已关闭',
  `view_count`  INT           DEFAULT 0           COMMENT '浏览量',
  `reply_count` INT           DEFAULT 0           COMMENT '回复数',
  `create_time` DATETIME      DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_type` (`type`),
  INDEX `idx_status` (`status`),
  INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社区互助帖';

-- 帖子回复
CREATE TABLE IF NOT EXISTS `community_post_reply` (
  `id`          INT PRIMARY KEY AUTO_INCREMENT,
  `post_id`     INT           NOT NULL            COMMENT '帖子ID',
  `user_id`     INT           NOT NULL            COMMENT '回复人ID',
  `content`     VARCHAR(500)  NOT NULL            COMMENT '回复内容',
  `create_time` DATETIME      DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_post_id` (`post_id`),
  INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社区帖回复';

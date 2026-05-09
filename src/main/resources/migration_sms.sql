-- 3.1.3 手机号注册验证 迁移脚本
-- 对已有 ylw 库使用；新库可直接由 schema.sql 构建
USE ylw;

-- 短信验证码表
CREATE TABLE IF NOT EXISTS `sms_code` (
  `id`           INT PRIMARY KEY AUTO_INCREMENT,
  `phone`        VARCHAR(20) NOT NULL               COMMENT '手机号',
  `code`         VARCHAR(10) NOT NULL               COMMENT '验证码',
  `purpose`      VARCHAR(20) NOT NULL               COMMENT 'register 注册 / reset 重置密码',
  `expire_time`  DATETIME    NOT NULL               COMMENT '过期时间',
  `used`         TINYINT     DEFAULT 0              COMMENT '0未使用 1已使用',
  `create_time`  DATETIME    DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_phone_purpose` (`phone`, `purpose`),
  INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短信验证码';

-- 对 user.phone 加唯一索引
-- 若已有重复非空 phone，需先清理；空值(NULL) MySQL 允许多条
-- 如已存在同名索引可忽略错误
ALTER TABLE `user` ADD UNIQUE KEY `uk_user_phone` (`phone`);

-- 邻里互助板块 迁移脚本
USE ylw;

ALTER TABLE `user` ADD COLUMN `last_active_time` DATETIME NULL COMMENT '最后心跳时间';
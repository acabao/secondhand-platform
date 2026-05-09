-- 安全自提点 迁移脚本
USE ylw;

-- 自提点表
CREATE TABLE IF NOT EXISTS `pickup_point` (
  `id`          INT PRIMARY KEY AUTO_INCREMENT,
  `name`        VARCHAR(100)  NOT NULL          COMMENT '地点名称',
  `address`     VARCHAR(255)  NOT NULL          COMMENT '详细地址',
  `description` VARCHAR(500)                    COMMENT '说明（如：物业旁 摄像头覆盖）',
  `hours`       VARCHAR(100)                    COMMENT '开放时间',
  `sort`        INT           DEFAULT 0         COMMENT '排序',
  `status`      TINYINT       DEFAULT 1         COMMENT '1启用 0停用',
  `create_time` DATETIME      DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社区自提点';

-- 订单表新增自提字段（兼容幂等：重复执行时忽略重复列错误需人工判断，首次执行即可）
ALTER TABLE `order`
  ADD COLUMN `delivery_type`   TINYINT   DEFAULT 1 COMMENT '1邮寄 2自提' AFTER `address_id`,
  ADD COLUMN `pickup_point_id` INT                 COMMENT '自提点ID'    AFTER `delivery_type`,
  ADD COLUMN `meet_time`       DATETIME            COMMENT '约定见面时间' AFTER `pickup_point_id`;

-- 预置示例自提点
INSERT INTO `pickup_point` (`name`, `address`, `description`, `hours`, `sort`) VALUES
('社区服务中心门口', '贡江社区党群服务中心正门前广场', '人流密集 摄像头覆盖 推荐工作时段使用', '09:00-21:00', 1),
('1号楼物业前台',   '贡江社区 1 号楼物业大厅',         '物业值班 可寄存短暂交换',                '07:30-22:00', 2),
('东门快递柜旁',     '贡江社区东门菜鸟快递柜旁座椅区', '靠近小区出入口 监控良好',                 '全天开放',    3),
('西门岗亭旁',       '贡江社区西门保安岗亭旁',         '有保安值班 夜间也安全',                   '全天开放',    4);

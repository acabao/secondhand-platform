mysqldump: [Warning] Using a password on the command line interface can be insecure.
-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: localhost    Database: shop
-- ------------------------------------------------------
-- Server version	8.0.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `address`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `receiver` varchar(50) NOT NULL COMMENT '收货人姓名',
  `phone` varchar(20) NOT NULL COMMENT '联系电话',
  `detail` varchar(255) NOT NULL COMMENT '详细地址',
  `is_default` tinyint(4) DEFAULT '0' COMMENT '是否默认地址 1是 0否',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='收货地址表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `admin`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码(加密)',
  `role` tinyint(4) DEFAULT '1' COMMENT '1管理员 2超级管理员',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='管理员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `banner`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `banner` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `image` varchar(255) NOT NULL COMMENT '图片URL',
  `link` varchar(255) DEFAULT NULL COMMENT '点击跳转链接',
  `sort` int(11) DEFAULT '0' COMMENT '排序(越小越靠前)',
  `status` tinyint(4) DEFAULT '1' COMMENT '1显示 0隐藏',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='轮播图表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `category`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `icon` varchar(255) DEFAULT NULL COMMENT '分类图标',
  `sort` int(11) DEFAULT '0' COMMENT '排序(越小越靠前)',
  `status` tinyint(4) DEFAULT '1' COMMENT '1启用 0禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comment`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `goods_id` int(11) NOT NULL COMMENT '商品ID',
  `user_id` int(11) NOT NULL COMMENT '评论用户ID',
  `content` varchar(500) NOT NULL COMMENT '评论内容',
  `rating` tinyint(4) DEFAULT NULL COMMENT '评分 1-5',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_goods_id` (`goods_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `community_post`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `community_post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '鍙戝笘浜篒D',
  `type` tinyint(4) NOT NULL COMMENT '1姹傝喘 2璧犻? 3鎹㈢墿',
  `title` varchar(100) NOT NULL COMMENT '鏍囬?',
  `description` text COMMENT '璇︾粏鎻忚堪',
  `images` text COMMENT '鍥剧墖URL JSON鏁扮粍',
  `want_item` varchar(200) DEFAULT NULL COMMENT '鎹㈢墿鏃舵兂瑕佹崲浠?箞',
  `location` varchar(100) DEFAULT NULL COMMENT '鎵?湪浣嶇疆/灏忓尯/妤兼爧',
  `contact` varchar(100) DEFAULT NULL COMMENT '鑱旂郴鏂瑰紡(鍙??)',
  `status` tinyint(4) DEFAULT '1' COMMENT '1杩涜?涓?2宸插畬鎴?3宸插叧闂',
  `view_count` int(11) DEFAULT '0' COMMENT '娴忚?閲',
  `reply_count` int(11) DEFAULT '0' COMMENT '鍥炲?鏁',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type` (`type`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='绀惧尯浜掑姪甯';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `community_post_reply`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `community_post_reply` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `post_id` int(11) NOT NULL COMMENT '甯栧瓙ID',
  `user_id` int(11) NOT NULL COMMENT '鍥炲?浜篒D',
  `content` varchar(500) NOT NULL COMMENT '鍥炲?鍐呭?',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='绀惧尯甯栧洖澶';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `favorite`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorite` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `goods_id` int(11) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_goods` (`user_id`,`goods_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='收藏表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goods`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '发布者ID',
  `category_id` int(11) NOT NULL COMMENT '分类ID',
  `title` varchar(100) NOT NULL COMMENT '商品标题',
  `description` text COMMENT '商品描述',
  `price` decimal(10,2) NOT NULL COMMENT '售价',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '原价',
  `images` text COMMENT '图片URL列表(JSON数组)',
  `condition_level` tinyint(4) DEFAULT NULL COMMENT '新旧程度 1全新 2几乎全新 3有使用痕迹',
  `status` tinyint(4) DEFAULT '0' COMMENT '0待审核 1在售 2已售出 3下架',
  `view_count` int(11) DEFAULT '0' COMMENT '浏览量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `message`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_id` int(11) NOT NULL COMMENT '发送者ID',
  `to_id` int(11) NOT NULL COMMENT '接收者ID',
  `goods_id` int(11) DEFAULT NULL COMMENT '关联商品ID',
  `content` varchar(500) NOT NULL COMMENT '消息内容',
  `is_read` tinyint(4) DEFAULT '0' COMMENT '是否已读 1已读 0未读',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_from_to` (`from_id`,`to_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='私信表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notice`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL COMMENT '公告标题',
  `content` text NOT NULL COMMENT '公告内容',
  `admin_id` int(11) DEFAULT NULL COMMENT '发布管理员ID',
  `status` tinyint(4) DEFAULT '1' COMMENT '1发布 0下线',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='公告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) NOT NULL COMMENT '订单号',
  `goods_id` int(11) NOT NULL COMMENT '商品ID',
  `buyer_id` int(11) NOT NULL COMMENT '买家ID',
  `seller_id` int(11) NOT NULL COMMENT '卖家ID',
  `price` decimal(10,2) NOT NULL COMMENT '成交价格',
  `address_id` int(11) DEFAULT NULL COMMENT '收货地址ID',
  `delivery_type` tinyint(4) DEFAULT '1' COMMENT '1閭?瘎 2鑷?彁',
  `pickup_point_id` int(11) DEFAULT NULL COMMENT '鑷?彁鐐笽D',
  `meet_time` datetime DEFAULT NULL COMMENT '绾﹀畾瑙侀潰鏃堕棿',
  `status` tinyint(4) DEFAULT '0' COMMENT '0待付款 1待发货 2待收货 3已完成 4已关闭',
  `remark` varchar(255) DEFAULT NULL COMMENT '买家备注',
  `pay_type` varchar(20) DEFAULT NULL COMMENT '支付方式: alipay/wechat/sandbox',
  `expire_time` datetime DEFAULT NULL COMMENT '支付超时时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付成功时间',
  `ship_time` datetime DEFAULT NULL COMMENT '发货时间',
  `complete_time` datetime DEFAULT NULL COMMENT '确认收货时间',
  `close_time` datetime DEFAULT NULL COMMENT '订单关闭时间',
  `settle_time` datetime DEFAULT NULL COMMENT '平台结算时间(T+1打款给卖家)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no` (`order_no`),
  KEY `idx_buyer_id` (`buyer_id`),
  KEY `idx_seller_id` (`seller_id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_status_expire` (`status`,`expire_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单号',
  `pay_type` varchar(20) NOT NULL COMMENT 'alipay/wechat/sandbox',
  `amount` decimal(10,2) NOT NULL COMMENT '支付金额',
  `trade_no` varchar(64) DEFAULT NULL COMMENT '第三方交易号',
  `qr_payload` varchar(512) DEFAULT NULL COMMENT '支付二维码内容/跳转链接',
  `status` tinyint(4) DEFAULT '0' COMMENT '0待支付 1已支付 2已关闭 3已退款',
  `raw_response` text COMMENT '网关原始响应',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `pay_time` datetime DEFAULT NULL COMMENT '支付成功时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_order_no` (`order_no`),
  KEY `idx_trade_no` (`trade_no`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='支付流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pickup_point`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pickup_point` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '鍦扮偣鍚嶇О',
  `address` varchar(255) NOT NULL COMMENT '璇︾粏鍦板潃',
  `description` varchar(500) DEFAULT NULL COMMENT '璇存槑锛堝?锛氱墿涓氭梺 鎽勫儚澶磋?鐩栵級',
  `hours` varchar(100) DEFAULT NULL COMMENT '寮?斁鏃堕棿',
  `sort` int(11) DEFAULT '0' COMMENT '鎺掑簭',
  `status` tinyint(4) DEFAULT '1' COMMENT '1鍚?敤 0鍋滅敤',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='绀惧尯鑷?彁鐐';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `refund`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refund` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单号冗余',
  `buyer_id` int(11) NOT NULL COMMENT '买家ID',
  `seller_id` int(11) NOT NULL COMMENT '卖家ID',
  `amount` decimal(10,2) NOT NULL COMMENT '退款金额',
  `reason` varchar(500) NOT NULL COMMENT '买家退款原因',
  `images` text COMMENT '凭证图片JSON数组',
  `status` tinyint(4) DEFAULT '0' COMMENT '0待卖家处理 1卖家同意 2卖家拒绝 3买家申请仲裁 4仲裁同意 5仲裁拒绝 6已退款',
  `seller_reply` varchar(500) DEFAULT NULL COMMENT '卖家拒绝原因',
  `admin_note` varchar(500) DEFAULT NULL COMMENT '管理员仲裁说明',
  `apply_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `seller_respond_time` datetime DEFAULT NULL COMMENT '卖家响应时间',
  `arbitrate_time` datetime DEFAULT NULL COMMENT '买家申请仲裁时间',
  `admin_respond_time` datetime DEFAULT NULL COMMENT '管理员裁决时间',
  `refund_time` datetime DEFAULT NULL COMMENT '退款完成时间',
  `deadline` datetime NOT NULL COMMENT '卖家响应截止 apply_time+48h',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  KEY `idx_buyer` (`buyer_id`),
  KEY `idx_seller` (`seller_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='退款/售后申请表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `review`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `order_no` varchar(50) NOT NULL,
  `goods_id` int(11) NOT NULL,
  `from_user_id` int(11) NOT NULL COMMENT '评价人',
  `to_user_id` int(11) NOT NULL COMMENT '被评价人',
  `role` tinyint(4) NOT NULL COMMENT '0 买家评卖家 / 1 卖家评买家',
  `item1_score` tinyint(4) NOT NULL COMMENT '1-5 星',
  `item2_score` tinyint(4) NOT NULL COMMENT '1-5 星',
  `item3_score` tinyint(4) NOT NULL COMMENT '1-5 星',
  `overall_score` decimal(3,2) NOT NULL COMMENT '三维平均分',
  `content` varchar(500) DEFAULT NULL COMMENT '评价文本',
  `images` text COMMENT '图片 URL JSON 数组',
  `auto_default` tinyint(4) DEFAULT '0' COMMENT '0 用户评价 / 1 系统默认好评',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_role` (`order_id`,`role`),
  KEY `idx_to_user` (`to_user_id`),
  KEY `idx_goods` (`goods_id`),
  KEY `idx_from_user` (`from_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='交易评价';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sms_code`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sms_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(20) NOT NULL COMMENT '手机号',
  `code` varchar(10) NOT NULL COMMENT '验证码',
  `purpose` varchar(20) NOT NULL COMMENT 'register 注册 / reset 重置密码',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  `used` tinyint(4) DEFAULT '0' COMMENT '0未使用 1已使用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_phone_purpose` (`phone`,`purpose`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='短信验证码';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码(加密)',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态 1正常 0封禁',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `last_active_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-09 13:02:27

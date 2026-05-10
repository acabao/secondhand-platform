# 二手商品交易平台（后端）

校园/社区场景的二手交易平台，提供商品发布、订单交易、支付、退款仲裁、双向评价、实时私信、社区帖、AI 客服等完整闭环；面向用户端与管理端两类用户。

> 前端仓库：[acabao/secondhand-platform-front](https://github.com/acabao/secondhand-platform-front)（Vue 3 + Vite + Element Plus）

## 技术栈

- **后端**：Spring Boot 2.6 / MyBatis / MySQL 8 / Redis / WebSocket / JWT / Hutool
- **第三方**：阿里云 OSS（图片存储）、通义千问 DashScope（AI 客服）
- **前端**：Vue 3 + Vite + Element Plus + Pinia（[独立仓库](https://github.com/acabao/secondhand-platform-front)）

## 快速启动

### 1. 准备环境
- JDK 8+
- MySQL 5.7+ / 8.0
- Redis 5+
- Maven 3.6+

### 2. 创建数据库
```sql
CREATE DATABASE shop DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;
```
导入 `docs/` 下的 SQL（建表语句 + 演示数据）。

### 3. 配置
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
# 编辑 application.properties，填写你的 MySQL 密码、Redis 密码、OSS / DashScope 密钥
```

### 4. 启动
```bash
mvn spring-boot:run
# 或
mvn clean package -DskipTests
java -jar target/*.jar
```

后端启动在 `8080`，WebSocket 在 `/ws/chat?userId={id}`。

## 默认账号
- 管理员：`admin` / `admin123`（登录路径 `/admin/login`）

## 部署到生产

生产环境用 `application-prod.properties`，启动加 `-Dspring.profiles.active=prod`。

Nginx 需配置：
- 静态目录指向前端 `dist/`
- `/api/` 反代到后端 8080
- `/ws/` 反代到后端 8080，带 `Upgrade` / `Connection` 头

## 主要模块

| 模块 | 说明 |
|---|---|
| 商品 | 发布 / 列表 / 详情，带 Redis 缓存 |
| 订单 | 邮寄 + 自提两种配送，支付沙箱模拟 |
| 退款 | 48h 超时仅开仲裁入口，原路退款 |
| 评价 | 双向三维评分，15 天超时默认好评 |
| 私信 | WebSocket 实时推送，离线落库 |
| 公告 | 管理端发布，首页弹窗 |
| AI 客服 | 通义千问 qwen-turbo |

## 相关仓库

- 后端 (本仓库)：https://github.com/acabao/secondhand-platform
- 前端 (Vue 3)：https://github.com/acabao/secondhand-platform-front

## 许可证
MIT

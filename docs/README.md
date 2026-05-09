# SQL 初始化

按顺序执行（数据库名按你自己的来）：

```bash
mysql -u root -p shop < schema.sql   # 建表
mysql -u root -p shop < seed.sql     # 默认管理员 + 分类 + 自提点等基础数据
```

默认管理员：`admin` / `admin123`

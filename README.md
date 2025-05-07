# GoGoPet 宠物托运智能服务系统

## 项目简介

GoGoPet是一个现代化的宠物托运智能服务系统，为宠物主人和托运公司提供一个高效、透明、安全的宠物托运服务平台。系统采用前后端分离架构，使用Vue 3和Spring Boot等现代技术栈，实现了包括托运服务、实时追踪、社区互动等核心功能。

## 核心功能

- 宠物托运服务管理
- 用户和宠物信息管理
- 订单全流程管理
- 实时位置追踪
- 社区交流平台
- 数据分析和可视化

## 技术栈

### 前端技术
- Vue 3.3.4
- Vite
- Element Plus 2.3.12
- Pinia 3.0.2
- Vue Router 4.5.0
- Axios
- ECharts
- WebSocket

### 后端技术
- Java 17
- Spring Boot 3.3.10
- MyBatis Plus
- MySQL 8.0
- Spring Security
- JWT
- WebSocket

## 快速开始

### 环境要求
- JDK 17.0.14+
- Node.js 16+
- MySQL 8.0+
- Maven 3.8+

### 开发环境部署

1. 克隆项目
```bash
git clone https://github.com/your-username/gogopet.git
cd gogopet
```

2. 前端部署
```bash
cd frontend
npm install
npm run dev
```

3. 后端部署
```bash
cd backend
./mvnw clean package
java -jar target/gogopet-0.0.1-SNAPSHOT.jar
```

4. 数据库配置
```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE gogopet;

# 导入数据
mysql -u root -p gogopet < backend/src/main/resources/sql/chaopi_v4_post_comment.sql
```

## 项目文档

详细文档请参考 `/docs` 目录：

- [项目概述](docs/1.项目概述.md)
- [需求规格说明](docs/2.需求规格说明.md)
- [系统架构设计](docs/3.系统架构设计.md)
- [数据库设计](docs/4.数据库设计.md)
- [API接口文档](docs/5.API接口文档.md)
- [部署指南](docs/6.部署指南.md)
- [开发指南](docs/7.开发指南.md)

## 项目结构

```
gogopet/
├── frontend/          # 前端项目
│   ├── src/          # 源代码
│   ├── public/       # 静态资源
│   └── package.json  # 依赖配置
├── backend/          # 后端项目
│   ├── src/         # 源代码
│   └── pom.xml      # Maven配置
└── docs/            # 项目文档
```

## 开发团队

- 产品经理：[姓名]
- 前端开发：[姓名]
- 后端开发：[姓名]
- 测试：[姓名]

## 版本历史

- v1.0.0 (2024-04-23)
  - 初始版本发布
  - 实现基础功能
  - 完成核心模块

## 贡献指南

1. Fork 项目
2. 创建功能分支
3. 提交代码
4. 发起 Pull Request

## 许可证

[MIT License](LICENSE)


# nursing-home-system

> 基于 Spring Boot + Vue3 的养老院综合管理系统，前后端分离、支持 Docker 一键部署。

## 项目简介

`nursing-home-system` 是一个完整的养老院管理解决方案，采用**前后端分离**架构：后端基于 Spring Boot 提供 REST API，前端基于 Vue3 + Element Plus 构建管理界面。系统涵盖老人管理、入住申请、护理计划、费用管理、员工管理、房间管理、数据统计等核心业务模块，界面美观、操作简便。

> ⚠️ 说明：本项目为**全栈应用，需要后端服务器与数据库**，无法纯静态托管到 GitHub Pages。本地运行需启动后端（Spring Boot）+ 前端（Vue3），并准备 MySQL 数据库。

## 功能特性（典型模块）

| 模块 | 功能说明 |
|------|---------|
| 👥 老人管理 | 老人信息录入、健康状况跟踪、入住 / 退住办理 |
| 📝 入住申请 | 在线申请、审批流程、状态跟踪 |
| 💊 护理计划 | 个性化护理方案、用药管理、饮食计划 |
| 💰 费用管理 | 自动费用生成、缴费 / 退款、费用统计 |
| 👨‍⚕️ 员工管理 | 员工信息、部门管理、入职 / 离职 |
| 🏠 房间管理 | 房间分配、换房、容量管理 |
| 📊 数据统计 | 可视化数据展示（ECharts）、经营分析 |

> 以上为养老院系统的常见业务模块，具体实现以仓库代码为准。

## 技术栈

| 层 | 技术 |
|----|------|
| 后端 | Java 17 + Spring Boot `3.4`、Maven |
| 前端 | Vue `3`、Vue Router `4`、Vuex `4`、Element Plus、Axios、ECharts（`vue-echarts`） |
| 构建 | 前端 `vue-cli-service`（已含 `dist/` 构建产物）、Nginx |
| 数据库 | MySQL `8.0`（`utf8mb4`） |
| 部署 | Docker Compose（Nginx + Spring Boot 后端） |

## 目录结构

```text
nursing-home-system/
├── backend/             # Spring Boot 后端
│   ├── pom.xml          # Maven 构建配置
│   ├── Dockerfile
│   ├── src/             # 业务代码
│   └── target/          # 构建产物
├── frontend/            # Vue3 前端
│   ├── package.json     # Vue CLI 配置
│   ├── vue.config.js
│   ├── Dockerfile
│   ├── nginx.conf       # 容器内的 Nginx 配置
│   ├── dist/            # 已构建的静态产物
│   ├── public/ src/     # 源码与静态资源
│   └── .env.development
├── database/            # 数据库脚本
│   ├── nursing_home_schema.sql            # 表结构
│   └── nursing_home_test_data_2026.sql    # 测试数据
├── docker-compose.yml   # 一键编排（nginx + backend）
├── 启动说明.md          # 详细启动文档（零基础向）
├── 快速启动.bat         # Windows 快速启动脚本
└── LICENSE
```

## 本地运行

### 方式一：Docker Compose（推荐）

`docker-compose.yml` 编排了 `nginx`（前端，`80` 端口）与 `backend`（Spring Boot，`8081` 端口），后端通过环境变量连接 MySQL（主机名 `mysql`，位于外部网络 `nursing-network`，需自行准备 MySQL 8.0 实例）。

```bash
# 1) 准备数据库：创建 nursing_home 库并导入 schema + 测试数据
mysql -u root -p nursing_home < database/nursing_home_schema.sql
mysql -u root -p nursing_home < database/nursing_home_test_data_2026.sql

# 2) 创建 compose 所需的外部网络（backend 通过 mysql 主机名访问数据库）
docker network create nursing-network

# 3) 启动（确保 MySQL 已在该网络可用，或使用自定义 compose 加入 mysql 服务）
docker compose up -d --build
```

> 提示：`docker-compose.yml` 中 `backend` 的环境变量默认 `SPRING_DATASOURCE_PASSWORD=1234`，请按你的 MySQL 实际密码修改；也可自行在 compose 中补充 `mysql` 服务以形成完整闭环。

### 方式二：分别启动前后端

```bash
# 后端（需 JDK17 + Maven）
cd backend
mvn spring-boot:run          # 默认 8081 端口

# 前端（需 Node.js 16+）
cd frontend
npm install
npm run serve                # 开发模式；npm run build 产出 dist/
```

更详尽的「零基础」步骤（环境准备、建库、改 `application.yml` 连接配置等）见仓库内 **`启动说明.md`**；Windows 用户可直接运行 **`快速启动.bat`**。

## 在线演示

- 未提供公开在线演示。本系统为全栈应用，需自行部署后端 + 数据库，无法纯静态托管到 GitHub Pages。
- 本地启动后通常访问前端 `http://localhost:80`（Docker Nginx）或前端 dev 端口，后端 API 在 `http://localhost:8081`。

## 说明 / 备注

- 数据库初始化脚本位于 `database/`，导入顺序：先 `nursing_home_schema.sql` 建表，再 `nursing_home_test_data_2026.sql` 灌入测试数据。
- 后端数据库连接配置见 `backend/src/main/resources/application.yml`（或由 compose 环境变量覆盖）。
- 前端已预构建 `dist/`，如需重新构建请执行 `npm run build` 并由 `nginx.conf` 托管。

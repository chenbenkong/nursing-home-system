# 养老院管理系统

> 一个基于 Spring Boot + Vue3 的养老院综合管理系统，帮助养老院提升运营效率和服务质量。

[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4-green.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3-4FC08D.svg)](https://vuejs.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-orange.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 📖 项目简介

本项目是一个完整的养老院管理解决方案，涵盖老人管理、入住申请、护理计划、费用管理、员工管理等核心业务模块。系统采用前后端分离架构，界面美观，操作简便。

### 核心功能

| 模块 | 功能说明 |
|------|---------|
| 👥 **老人管理** | 老人信息录入、健康状况跟踪、入住/退住办理 |
| 📝 **入住申请** | 在线申请、审批流程、状态跟踪 |
| 💊 **护理计划** | 个性化护理方案、用药管理、饮食计划 |
| 💰 **费用管理** | 自动费用生成、缴费/退款、费用统计 |
| 👨‍⚕️ **员工管理** | 员工信息、部门管理、入职/离职 |
| 🏠 **房间管理** | 房间分配、换房、容量管理 |
| 📊 **数据统计** | 可视化数据展示、经营分析 |

---

## 🚀 快速开始

### 环境要求

| 软件 | 版本 | 说明 |
|------|------|------|
| JDK | 17+ | Java运行环境 |
| MySQL | 8.0+ | 数据存储 |
| Node.js | 16+ | 前端运行环境 |
| Maven | 3.6+ | 项目构建工具 |

### 1. 克隆项目

```bash
git clone https://github.com/your-repo/nursing-home-system.git
cd nursing-home-system
```

### 2. 数据库配置

```bash
# 登录MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE nursing_home DEFAULT CHARACTER SET utf8mb4;

# 导入数据（退出MySQL后执行）
mysql -u root -p nursing_home < database/nursing_home.sql
```

### 3. 修改配置

编辑 `backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/nursing_home
    username: root
    password: 你的密码
```

### 4. 启动项目

**Windows用户（推荐）：**
```bash
双击运行：快速启动.bat
```

**手动启动：**

```bash
# 终端1：启动后端
cd backend
mvn spring-boot:run

# 终端2：启动前端
cd frontend
npm install
npm run serve
```

### 5. 访问系统

- 前端地址：http://localhost:8080
- 后端地址：http://localhost:8081
- 默认账号：admin / 123456

---

## 📸 系统截图

> 这里可以添加系统界面截图

---

## 🛠️ 技术栈

### 后端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.4.0 | 核心框架 |
| MyBatis | 3.0.4 | ORM框架 |
| MySQL | 8.0 | 数据库 |
| Druid | 1.2.23 | 连接池 |
| JWT | 0.12.6 | 身份认证 |
| Maven | 3.6+ | 构建工具 |

### 前端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.x | 前端框架 |
| Element Plus | 2.x | UI组件库 |
| Vue Router | 4.x | 路由管理 |
| Vuex | 4.x | 状态管理 |
| Axios | 1.x | HTTP请求 |
| ECharts | 5.x | 图表库 |

---

## 📁 项目结构

```
nursing-home-system/
├── backend/                    # 后端项目
│   ├── src/main/java/com/nursinghome/
│   │   ├── controller/         # 控制器层（处理HTTP请求）
│   │   ├── service/            # 业务逻辑层
│   │   ├── mapper/             # 数据访问层（MyBatis）
│   │   ├── entity/             # 实体类
│   │   ├── config/             # 配置类
│   │   ├── util/               # 工具类
│   │   └── interceptor/        # 拦截器
│   ├── src/main/resources/
│   │   ├── application.yml     # 核心配置文件
│   │   └── mapper/*.xml        # MyBatis映射文件
│   ├── Dockerfile
│   └── pom.xml
│
├── frontend/                   # 前端项目
│   ├── src/
│   │   ├── api/                # API接口定义
│   │   ├── views/              # 页面组件
│   │   ├── components/         # 公共组件
│   │   ├── router/             # 路由配置
│   │   ├── store/              # 状态管理（Vuex）
│   │   ├── utils/              # 工具函数
│   │   └── assets/             # 静态资源
│   ├── package.json
│   └── vue.config.js
│
├── database/
│   ├── nursing_home.sql        # 数据库初始化脚本
│   └── nursing_home_schema.sql # 数据库结构脚本
│
├── docker-compose.yml          # Docker编排配置
├── 快速启动.bat                 # Windows快速启动脚本
├── 启动说明.md                  # 详细启动文档
└── README.md                   # 项目说明文档
```

---

## 📚 详细文档

- [启动说明.md](启动说明.md) - 详细的启动配置说明
- [API文档](#) - 接口文档（待完善）
- [开发规范](#) - 代码规范（待完善）

---

## 🔧 开发指南

### 后端开发

```bash
cd backend

# 编译
mvn clean compile

# 测试
mvn test

# 打包
mvn clean package -DskipTests

# 运行
mvn spring-boot:run
```

### 前端开发

```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run serve

# 构建生产环境
npm run build

# 代码检查
npm run lint
```

---

## 🐳 Docker部署

```bash
# 构建并启动所有服务
docker-compose up -d

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose down
```

---

## ❓ 常见问题

**Q: 后端启动报错 "Name for argument of type [java.lang.String] not specified"**

A: 执行 `mvn clean compile` 清理缓存后重新启动

**Q: 数据库连接失败**

A: 检查MySQL服务是否启动，用户名密码是否正确

**Q: 前端安装依赖失败**

A: 尝试使用淘宝镜像：`npm config set registry https://registry.npmmirror.com`

更多问题请参考 [启动说明.md](启动说明.md) 中的"常见问题解决"章节

---

## 🤝 贡献指南

欢迎提交Issue和Pull Request！

1. Fork 本仓库
2. 创建你的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交你的修改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开一个 Pull Request

---

## 📄 许可证

本项目采用 [MIT](LICENSE) 许可证

---

## 👨‍💻 开发团队

- 项目发起：养老院管理系统项目组
- 技术栈：Spring Boot + Vue3 + MySQL
- 开发时间：2024-2025

---

## 🙏 致谢

感谢以下开源项目：
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Vue.js](https://vuejs.org/)
- [Element Plus](https://element-plus.org/)
- [MyBatis](https://mybatis.org/)

---

**如果觉得本项目对你有帮助，请给个 Star ⭐️ 支持一下！**

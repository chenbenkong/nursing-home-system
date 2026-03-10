# 养老院管理系统

## 项目概述

针对养老院运营效率与服务管理的挑战，本项目开发了一套前后端分离的综合管理系统，解决养老院客户管理、服务流程（如入住申请、护理计划）、员工工作管理等核心业务痛点，提升运营效率与服务质量。

## 技术栈

### 前端
- **框架**: Vue 3 + Composition API
- **UI组件库**: Element Plus
- **状态管理**: Vuex
- **路由**: Vue Router
- **HTTP客户端**: Axios
- **构建工具**: Vue CLI

### 后端
- **框架**: Spring MVC + Spring
- **ORM框架**: MyBatis
- **数据库**: MySQL 8.0
- **连接池**: Druid
- **安全**: JWT Token认证
- **构建工具**: Maven

## 项目结构

```
nursing-home-system/
├── backend/                    # 后端项目
│   ├── src/main/java/com/nursinghome/
│   │   ├── controller/         # 控制器层
│   │   ├── service/            # 业务逻辑层
│   │   ├── mapper/             # 数据访问层
│   │   ├── entity/             # 实体类
│   │   ├── util/               # 工具类
│   │   └── interceptor/        # 拦截器
│   ├── src/main/resources/
│   │   ├── spring-mvc.xml      # SpringMVC配置
│   │   ├── spring-service.xml  # Spring服务配置
│   │   ├── mybatis-config.xml  # MyBatis配置
│   │   └── jdbc.properties     # 数据库配置
│   └── pom.xml                 # Maven配置
├── frontend/                   # 前端项目
│   ├── src/
│   │   ├── api/                # API接口
│   │   ├── views/              # 页面组件
│   │   ├── router/             # 路由配置
│   │   ├── store/              # 状态管理
│   │   └── assets/             # 静态资源
│   ├── package.json
│   └── vue.config.js
└── database/
    └── nursing_home.sql        # 数据库脚本
```

## 功能模块

### 1. 用户管理
- 用户登录/登出（JWT认证）
- 用户角色管理（管理员、经理、护士、医生、员工）
- 用户增删改查

### 2. 老人管理
- 老人信息录入与管理
- 入住/退住办理
- 健康状况跟踪
- 护理等级管理

### 3. 入住申请
- 在线提交入住申请
- 申请审批流程
- 申请状态跟踪

### 4. 护理计划
- 个性化护理计划制定
- 护理计划执行跟踪
- 饮食、用药、康复计划管理

### 5. 员工管理
- 员工信息管理
- 部门管理
- 入职/离职办理

### 6. 仪表盘
- 数据统计展示
- 快捷操作入口
- 系统公告

## 数据库设计

### 核心表结构

1. **sys_user** - 系统用户表
2. **elder** - 老人信息表
3. **check_in_application** - 入住申请表
4. **care_plan** - 护理计划表
5. **care_record** - 护理记录表
6. **employee** - 员工表
7. **room** - 房间表
8. **fee_record** - 费用记录表
9. **visitor_record** - 访客记录表
10. **announcement** - 公告表

## 快速开始

### 环境要求
- JDK 11+
- MySQL 8.0+
- Node.js 16+
- Maven 3.6+

### 后端部署

1. 创建数据库并导入SQL脚本
```bash
mysql -u root -p < database/nursing_home.sql
```

2. 修改数据库配置
编辑 `backend/src/main/resources/jdbc.properties`:
```properties
jdbc.url=jdbc:mysql://localhost:3306/nursing_home
jdbc.username=root
jdbc.password=your_password
```

3. 编译打包
```bash
cd backend
mvn clean package
```

4. 部署到Tomcat
将生成的 `nursing-home-system.war` 复制到Tomcat的webapps目录

### 前端部署

1. 安装依赖
```bash
cd frontend
npm install
```

2. 开发模式运行
```bash
npm run serve
```

3. 生产构建
```bash
npm run build
```

## 默认账号

- 用户名: admin
- 密码: admin123

## API接口文档

### 用户相关
- POST `/api/user/login` - 用户登录
- POST `/api/user/register` - 用户注册
- GET `/api/user/info` - 获取当前用户信息
- GET `/api/user/list` - 获取用户列表

### 老人相关
- GET `/api/elder/list` - 获取老人列表（分页）
- POST `/api/elder` - 添加老人
- PUT `/api/elder` - 更新老人
- DELETE `/api/elder/{id}` - 删除老人
- PUT `/api/elder/{id}/checkin` - 办理入住
- PUT `/api/elder/{id}/checkout` - 办理退住

### 入住申请相关
- GET `/api/application/list` - 获取申请列表
- POST `/api/application` - 提交申请
- PUT `/api/application/{id}/approve` - 审批通过
- PUT `/api/application/{id}/reject` - 审批拒绝

### 护理计划相关
- GET `/api/care-plan/list` - 获取护理计划列表
- POST `/api/care-plan` - 创建护理计划
- PUT `/api/care-plan/{id}/pause` - 暂停计划
- PUT `/api/care-plan/{id}/resume` - 恢复计划

### 员工相关
- GET `/api/employee/list` - 获取员工列表
- POST `/api/employee` - 添加员工
- PUT `/api/employee/{id}/resign` - 办理离职

## 项目特点

1. **前后端分离**: 前端Vue3 + 后端SpringMVC，职责清晰
2. **RESTful API**: 统一的接口设计规范
3. **JWT认证**: 安全的用户认证机制
4. **分页查询**: 大数据量分页展示
5. **响应式布局**: 适配不同屏幕尺寸
6. **代码规范**: 遵循Java和Vue编码规范

## 开发团队

本项目为养老院管理系统演示项目，包含完整的业务功能和技术实现。

## 许可证

MIT License

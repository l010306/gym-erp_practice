# 健身房 ERP 管理系统 (Gym ERP System)

## 📋 项目简介

这是一个基于 Spring Boot 开发的健身房企业资源计划（ERP）管理系统，旨在提供现代化的店面管理体验。系统采用玻璃拟态 (Glassmorphism) 设计语言，集成了会员注册、续费审批、课程预约及数据管理等核心功能。

### 主要功能模块

- **🏠 店面管理看板**：实时查看会员总数、待办审批、今日课程等核心经营指标。
- **👤 会员注册**：支持会员入会注册、健康档案建立。
- **💳 会员续费**：会员卡办理、套餐升级与审批流。
- **📚 课程管理**：全方位的课程排班与资源调度。
- **⚙️ 数据管理中心**：提供底层数据的 CRUD 操作及一键测试数据填充。
- **教练日历与课程预约**：集成教练日历与会员课程预约流程（bata）。

---

## 🛠️ 技术栈

- **后端**: Spring Boot 3.4.1 (LTS), Spring Data JPA
- **数据库**: H2 (开发/演示), PostgreSQL (生产)
- **模板引擎**: Thymeleaf (已配置开发模式实时刷新)
- **前端艺术**: Vanilla CSS (Glassmorphism), SweetAlert2
- **构建工具**: Maven
- **开发语言**: Java 21

---

## 🚀 快速本地部署

详细步骤请参考 [deployment_guide.md](file:///Users/lidachuan/.gemini/antigravity/brain/786356ac-4f3e-4e13-8891-aa054bb31995/deployment_guide.md)。

### 1. 环境检查
确保 `JAVA_HOME` 指向 JDK 21。

### 2. 启动开发模式 (H2)
```bash
./mvnw clean compile
./mvnw spring-boot:run -Dspring-boot.run.profiles=h2
```

### 3. 访问入口
- **首页仪表盘**: http://localhost:8080/
- **数据管理**: http://localhost:8080/admin/data

---

## 📦 项目结构

- `com.gym.erp.membership`: 会员注册与管理逻辑。
- `com.gym.erp.finance`: 财务流水与会员续费审批。
- `com.gym.erp.training`: 课程定义与预约逻辑。
- `com.gym.erp.staff`: 员工与教练管理。
- `com.gym.erp.component`: 仪表盘控制器及全局组件。

---

## 🐳 生产环境部署 (Docker)

项目支持 Docker 隔离部署，详情请咨询管理员或参考 `docker-compose.yml` 配置。

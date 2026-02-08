# 健身房 ERP 管理系统 (Gym ERP System)

## 📋 项目简介

这是一个基于 Spring Boot 开发的健身房企业资源计划（ERP）管理系统，用于管理健身房的日常运营，包括会员管理、员工管理、课程管理和财务管理等核心功能。

### 主要功能模块

- **会员管理（Membership）**：会员信息管理、会员卡续费、会员课程预约
- **员工管理（Staff）**：员工信息管理、密码修改、权限管理
- **课程管理（Training）**：课程创建、课程预约、教练日历
- **财务管理（Finance）**：财务记录、收支管理

## 🛠️ 技术栈

- **后端框架**：Spring Boot 4.0.1
- **数据库**：PostgreSQL
- **ORM 框架**：Spring Data JPA / Hibernate
- **模板引擎**：Thymeleaf
- **前端**：HTML + CSS + JavaScript
- **构建工具**：Maven
- **开发语言**：Java 21

## 📦 项目结构

```
gym-erp/
├── src/
│   ├── main/
│   │   ├── java/com/gym/erp/
│   │   │   ├── component/          # 公共组件
│   │   │   ├── finance/            # 财务模块
│   │   │   ├── membership/         # 会员模块
│   │   │   ├── staff/              # 员工模块
│   │   │   ├── training/           # 课程培训模块
│   │   │   └── GymErpApplication.java
│   │   └── resources/
│   │       ├── static/             # 静态资源（HTML/CSS/JS）
│   │       ├── templates/          # Thymeleaf 模板
│   │       └── application.properties
│   └── test/                       # 测试代码
├── pom.xml                         # Maven 配置文件
└── README.md                       # 项目说明文档
```

## 🚀 快速开始

### 环境要求

- **Java**：JDK 21 或更高版本
- **Maven**：3.6+ 
- **PostgreSQL**：12.0 或更高版本
- **IDE**：IntelliJ IDEA（推荐）或其他 Java IDE

### 数据库配置

1. **安装 PostgreSQL**（如果尚未安装）

2. **创建数据库**：
   ```sql
   CREATE DATABASE gym_erp;
   ```

3. **配置数据库连接**：
   
   创建 `src/main/resources/application-local.properties` 文件（不会被提交到 Git）：
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/gym_erp
   spring.datasource.username=你的数据库用户名
   spring.datasource.password=你的数据库密码
   ```

   **注意**：请不要直接修改 `application.properties` 文件中的密码，该文件会被提交到版本控制。

### 本地运行

#### 方式一：使用 Maven 命令行

```bash
# 1. 克隆项目
git clone <你的仓库地址>
cd gym-erp

# 2. 安装依赖并编译
mvn clean install

# 3. 运行应用
mvn spring-boot:run
```

#### 方式二：使用 IDE (IntelliJ IDEA)

1. 打开 IntelliJ IDEA
2. 选择 `File` → `Open` → 选择项目目录
3. 等待 Maven 自动下载依赖
4. 找到 `GymErpApplication.java`
5. 右键点击 → `Run 'GymErpApplication'`

#### 访问应用

应用启动成功后，在浏览器访问：
- **主页面**：http://localhost:8080
- **管理后台**：http://localhost:8080/admin.html

## 📝 开发指南

### 代码规范

项目采用**功能模块化（Feature-based）**架构：
- 每个功能模块独立管理其 Entity、Repository、Service、Controller
- 便于功能扩展和维护

### 数据库迁移

- 项目使用 JPA 的 `ddl-auto=update` 自动更新数据库结构
- 首次运行时会自动创建所需的表结构
- 如需手动执行 SQL，请将文件放在 `src/main/resources/db/migration/` 目录

## 🔧 常见问题

### 1. 端口被占用

如果 8080 端口被占用，可以在 `application.properties` 中修改：
```properties
server.port=8081
```

### 2. 数据库连接失败

- 检查 PostgreSQL 服务是否启动
- 确认数据库名称、用户名、密码是否正确
- 确认 PostgreSQL 监听端口（默认 5432）

### 3. Maven 依赖下载慢

可以配置国内镜像源，编辑 `~/.m2/settings.xml`：
```xml
<mirror>
  <id>aliyun</id>
  <mirrorOf>central</mirrorOf>
  <name>Aliyun Maven</name>
  <url>https://maven.aliyun.com/repository/public</url>
</mirror>
```

## 📄 License

本项目仅供学习和参考使用。

## 👥 贡献者

- 开发者：LI DACHUAN

## 📮 联系方式

如有问题或建议，请通过 GitHub Issues 联系。

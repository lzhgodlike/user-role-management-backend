# 用户角色管理系统后端

## 项目介绍

这是一个基于Spring Boot开发的用户角色管理系统后端，提供用户认证、授权和角色管理等功能。

> **注意**: 完整的项目文档请查看[文档仓库](https://github.com/lzhgodlike/user-role-management-docs)。

## 技术栈

- Java 8+
- Spring Boot
- Spring Data JPA
- MySQL 数据库
- Maven

## 功能特性

- 用户认证系统（注册与登录）
- 用户角色的增删改查操作
- RESTful API规范设计
- 全局异常处理机制
- 跨域资源共享 (CORS) 支持
- MySQL数据持久化
- Spring Data JPA自动创建表结构

## 安装与运行

### 前置条件

- Java JDK 8+
- Maven 3.6+
- MySQL 数据库

### 步骤

1. 克隆项目到本地

```bash
git clone https://github.com/lzhgodlike/user-role-management-backend.git
cd user-role-management-backend
```

2. 配置数据库

在MySQL中创建名为`user_role_db`的数据库：

```sql
CREATE DATABASE user_role_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. 调整数据库连接配置

在`src/main/resources/application.properties`中检查并修改数据库连接配置：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/user_role_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=123456
```

4. 使用Maven编译和运行项目

```bash
mvn clean install
mvn spring-boot:run
```

服务将在 `http://localhost:8080` 启动。

## API 文档

### 认证相关

- `POST /api/auth/register` - 用户注册
  - 请求体: `{ "username": "用户名", "password": "密码" }`
  - 成功响应: `201 Created` 返回创建的用户信息

- `POST /api/auth/login` - 用户登录
  - 请求体: `{ "username": "用户名", "password": "密码" }`
  - 成功响应: `200 OK` 返回token、用户ID、用户名和角色信息

### 用户角色管理

- `GET /api/user-roles` - 获取所有用户角色
  - 成功响应: `200 OK` 返回用户角色列表

- `GET /api/user-roles/{id}` - 获取特定用户角色
  - 成功响应: `200 OK` 返回用户角色详情
  - 失败响应: `404 Not Found` 角色不存在

- `POST /api/user-roles` - 创建新用户角色
  - 请求体: `{ "name": "角色名称", "roleDesc": "描述", "isEnabled": true }`
  - 成功响应: `201 Created` 返回创建的角色信息

- `PUT /api/user-roles/{id}` - 更新用户角色
  - 请求体: `{ "name": "新角色名称", "roleDesc": "新描述", "isEnabled": false }`
  - 成功响应: `200 OK` 返回更新后的角色信息
  - 失败响应: `404 Not Found` 角色不存在

- `DELETE /api/user-roles/{id}` - 删除用户角色
  - 成功响应: `200 OK` 返回成功消息
  - 失败响应: `404 Not Found` 角色不存在

## 开发说明

- 项目使用MySQL数据库，配置在`application.properties`中
- 应用首次启动时会自动创建数据库表结构
- 初始用户数据在`data.sql`中配置
- 全局异常处理提供统一的错误响应格式
- 跨域配置允许前端应用进行API调用
- 用户认证采用简单的基于Token的方式，生产环境建议使用JWT或Spring Security

## 核心功能说明

### 用户认证
`AuthController`提供用户注册和登录功能，登录成功后返回一个简单的token供前端使用。

### 用户角色管理
`UserRoleController`提供完整的CRUD操作：
- 获取所有角色
- 根据ID获取特定角色
- 创建新角色
- 更新角色信息
- 删除角色

每个操作都有适当的响应状态码和错误处理。

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── demo/
│   │               ├── config/           - 配置类
│   │               │   ├── CorsConfig.java            - 跨域配置
│   │               │   └── GlobalExceptionHandler.java - 全局异常处理
│   │               ├── controller/       - 控制器
│   │               │   ├── AuthController.java        - 认证控制器
│   │               │   └── UserRoleController.java    - 用户角色控制器
│   │               ├── entity/           - 实体类
│   │               │   ├── Student.java               - 学生实体
│   │               │   ├── User.java                  - 用户实体
│   │               │   └── UserRole.java              - 用户角色实体
│   │               ├── repository/       - 数据访问层
│   │               │   ├── UserRepository.java        - 用户仓库
│   │               │   └── UserRoleRepository.java    - 用户角色仓库
│   │               ├── service/          - 服务层
│   │               │   └── UserRoleService.java       - 用户角色服务
│   │               └── util/             - 工具类
│   │                   └── CreateAdminUser.java       - 初始管理员创建工具
│   └── resources/
│       ├── application.properties        - 应用配置
│       └── data.sql                      - 初始数据
└── test/                                 - 单元测试
    └── java/
        └── com/
            └── example/
                └── demo/
                    ├── DemoApplicationTests.java    - 应用测试类
                    └── UserRoleServiceTest.java     - 用户角色服务测试
```

## 协作与贡献

欢迎提交问题和合并请求。

## 数据库设计

### 用户表 (User)
- id: 主键
- username: 用户名
- password: 密码
- role_id: 外键，关联用户角色表

### 用户角色表 (UserRole)
- id: 主键
- name: 角色名称
- roleDesc: 角色描述
- isEnabled: 是否启用
- createTime: 创建时间

## 部署指南

### 开发环境部署

使用Maven的Spring Boot插件运行应用：

```bash
mvn spring-boot:run
```

### 生产环境部署

1. 构建应用

```bash
mvn clean package
```

2. 运行JAR文件

```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

3. 使用环境变量覆盖配置

```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar --spring.datasource.url=jdbc:mysql://生产数据库地址:3306/user_role_db
```

### Docker部署

1. 创建Dockerfile:

```dockerfile
FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

2. 构建并运行Docker镜像:

```bash
docker build -t user-role-system .
docker run -p 8080:8080 user-role-system
```

## 安全性建议

本项目使用了简化的认证机制，生产环境中应考虑以下增强措施：

1. 使用Spring Security进行全面的安全保护
2. 实现JWT认证替代简单token
3. 密码加盐哈希存储
4. 实现请求限流防止暴力攻击
5. 敏感操作添加日志审计

## 性能优化建议

1. 添加Redis缓存减轻数据库压力
2. 分页查询大数据集
3. 使用连接池优化数据库连接

## 许可证

本项目使用 MIT 许可证，详情请参阅 [LICENSE](./LICENSE) 文件。

# 教师学生助手系统

> 基于 Spring Boot + Vue.js + WebSocket + AI 的教学管理平台

---

## 📢 写在前面

**如果你正在为大学期末课设（课程设计/课程实训）发愁，这个项目或许能帮到你！**

本项目是一套完整的、可直接运行的 **Java Web 全栈项目**，涵盖了软件工程课设中常见的技术要求：

- ✅ **后端开发**：Spring Boot + MyBatis + MySQL（增删改查、分层架构）
- ✅ **前端开发**：Vue.js + Element Plus（响应式UI、组件化）
- ✅ **实时通讯**：WebSocket（双向通信、房间隔离）
- ✅ **权限控制**：RBAC 模型（角色管理、越权防护）
- ✅ **AI集成**：大模型API调用（角色感知提示词）
- ✅ **数据可视化**：ECharts 图表

项目代码结构清晰，注释完整，配有详细的使用教程和数据库脚本，**克隆下来简单配置就能跑通**。你可以直接使用，也可以在此基础上二次开发，添加自己的功能模块。

> 💡 **温馨提示**：课设提交前请务必理解项目的核心逻辑和代码结构，答辩时老师可能会提问哦！

---

## 项目介绍

教师学生助手系统是一个面向学校教学管理场景的Web应用，支持管理员、教师、学生三种角色，提供用户管理、班级管理、实时群聊、AI智能问答等功能。

### 技术栈

**后端：** Spring Boot 2.7.18 + MyBatis 3.5.14 + MySQL 8.0

**前端：** Vue.js 3 + Element Plus + Axios + ECharts

**其他：** WebSocket + 小米MiMo AI API

### 角色功能

| 功能 | 管理员 | 教师 | 学生 |
|------|--------|------|------|
| 数据大屏 | ✅ | ❌ | ❌ |
| 教师管理 | ✅ | ❌ | ❌ |
| 学生管理 | ✅ | ✅ 本班 | ❌ |
| 班级管理 | ✅ | ✅ 负责班 | ❌ |
| 我的班级 | ❌ | ❌ | ✅ |
| 班级群聊 | ❌ | ✅ | ✅ |
| AI助手 | ❌ | ✅ | ✅ |
| 个人中心 | ✅ | ✅ | ✅ |

---

## 环境要求

| 环境 | 版本 | 必需 |
|------|------|------|
| JDK | 1.8 | ✅ |
| Maven | 3.6+ | ✅ |
| MySQL | 5.7+（推荐8.0） | ✅ |
| IDE | IntelliJ IDEA（推荐） | ✅ |
| 浏览器 | Chrome 80+ | ✅ |

---

## 快速开始

### 第一步：安装JDK 1.8

1. 下载JDK 1.8：https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html
2. 安装并配置环境变量：
   ```
   JAVA_HOME = C:\Program Files\Java\jdk1.8.0_xxx
   Path 追加 = %JAVA_HOME%\bin
   ```
3. 验证：
   ```bash
   java -version
   # 输出 java version "1.8.0_xxx" 即成功
   ```

### 第二步：安装Maven

1. 下载Maven：https://maven.apache.org/download.cgi
2. 解压到目录（如 `D:\apache-maven-3.8.6`）
3. 配置环境变量：
   ```
   MAVEN_HOME = D:\apache-maven-3.8.6
   Path 追加 = %MAVEN_HOME%\bin
   ```
4. 验证：
   ```bash
   mvn -version
   ```

5. （推荐）配置阿里云镜像加速，编辑 `~/.m2/settings.xml`：
   ```xml
   <settings>
     <mirrors>
       <mirror>
         <id>aliyun</id>
         <url>https://maven.aliyun.com/repository/public</url>
         <mirrorOf>central</mirrorOf>
       </mirror>
     </mirrors>
   </settings>
   ```

### 第三步：安装并启动MySQL

1. 下载MySQL 8.0：https://dev.mysql.com/downloads/mysql/
2. 安装时设置root密码（记住这个密码）
3. 确保MySQL服务已启动

### 第四步：创建数据库并导入数据

打开命令行或Navicat，执行以下操作：

```sql
-- 1. 登录MySQL
mysql -u root -p

-- 2. 创建数据库
CREATE DATABASE IF NOT EXISTS teacher_assistant
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- 3. 切换到目标数据库
USE teacher_assistant;

-- 4. 导入数据表和测试数据
SOURCE 你的项目路径/assistant_schema.sql;

-- 5. 验证（应该看到3张表和测试数据）
SHOW TABLES;
SELECT * FROM t_user;
```

### 第五步：修改项目配置

编辑 `src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/teacher_assistant?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8&allowPublicKeyRetrieval=true
    username: root              # 改成你的MySQL用户名
    password: 你的密码           # 改成你的MySQL密码
    driver-class-name: com.mysql.cj.jdbc.Driver
```

（可选）如果需要AI功能，配置小米MiMo API Key：

```yaml
xiaomi:
  mimo:
    api-key: "你的真实API Key"   # 替换 YOUR_API_KEY_HERE
    url: "https://api.xiaomimimo.com/v1/chat/completions"
    model: "mimo-v2-flash"
```

### 第六步：用IDEA打开项目

1. 打开 IntelliJ IDEA
2. File → Open → 选择项目根目录（包含 `pom.xml` 的文件夹）
3. 等待IDEA自动下载依赖（右下角进度条）
4. 如果IDEA提示"Import Changes"，点击确认

### 第七步：运行项目

**方式一：IDEA运行（推荐）**

1. 找到 `src/main/java/com/assistant/TeacherAssistantApplication.java`
2. 右键 → Run 'TeacherAssistantApplication'
3. 等待控制台显示：
   ```
   Started TeacherAssistantApplication in X.XXX seconds
   ```

**方式二：命令行运行**

```bash
cd 项目目录
mvn spring-boot:run
```

### 第八步：访问系统

打开浏览器访问：

```
http://localhost:10001
```

---

## 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 教师 | T001 | 123456 |
| 教师 | T002 | 123456 |
| 学生 | 2021001 | 123456 |
| 学生 | 2021002 | 123456 |
| 学生 | 2021003 | 123456 |

> 注意：登录时必须选择正确的角色，否则会提示"身份与选择不符"

---

## 项目结构

```
teacher-assistant/
├── src/main/java/com/assistant/
│   ├── common/Result.java              # 统一响应封装
│   ├── config/WebSocketConfig.java     # WebSocket配置
│   ├── controller/                     # 接口层（4个Controller）
│   ├── dao/                            # 数据访问层（3个Mapper）
│   ├── model/                          # 实体类（3个Entity）
│   ├── service/                        # 业务逻辑层
│   ├── websocket/                      # WebSocket服务
│   └── TeacherAssistantApplication.java # 启动类
├── src/main/resources/
│   ├── mapper/                         # MyBatis XML映射文件
│   ├── static/
│   │   ├── index.html                  # 登录/注册页面
│   │   └── main.html                   # 主框架页面
│   └── application.yml                 # 配置文件
├── assistant_schema.sql                # 数据库脚本
├── pom.xml                             # Maven配置
└── README.md                           # 本文档
```

---

## 常见问题

### 1. 启动报错 `Unsupported character encoding 'utf8mb4'`

**解决：** 修改 `application.yml` 中的URL，把 `utf8mb4` 改成 `UTF-8`：

```yaml
url: jdbc:mysql://localhost:3306/teacher_assistant?characterEncoding=UTF-8&...
```

### 2. 启动报错 `Access denied for user 'root'`

**解决：** 检查 `application.yml` 中的MySQL用户名和密码是否正确。

### 3. 启动报错 `Port 10001 was already in use`

**解决：** 修改 `application.yml` 中的端口号，或者关闭占用10001端口的程序：
```bash
# Windows查看占用端口的进程
netstat -ano | findstr 10001
# 杀掉进程（替换PID）
taskkill /F /PID 进程号
```

### 4. IDEA中Java文件报错（红色）

**解决：**
1. 右键项目 → Maven → Reload Project
2. 等待依赖下载完成
3. 如仍有错误：File → Invalidate Caches → Invalidate and Restart

### 5. Maven依赖下载慢

**解决：** 配置阿里云镜像，编辑 `~/.m2/settings.xml` 添加镜像配置（见第二步第5点）。

### 6. 登录后页面空白

**解决：** 强制刷新浏览器 `Ctrl + Shift + R`，或打开F12控制台查看错误信息。

### 7. WebSocket群聊无法收发消息

**解决：**
1. 检查浏览器控制台是否有WebSocket连接错误
2. 确认数据库中有对应的班级数据
3. 确认当前用户有对应的classId

### 8. AI助手返回错误

**解决：** 检查 `application.yml` 中的小米MiMo API Key是否已配置。如不需要AI功能可忽略。

---

## 核心API接口

### 用户相关

```
POST   /user/login                           # 登录
POST   /user/add                             # 新增用户
DELETE /user/delete?id=&operatorRole=&operatorId=  # 删除用户（管理员）
PUT    /user/update?operatorRole=            # 更新用户（管理员）
PUT    /user/update/profile?userId=&operatorId=    # 更新个人信息
GET    /user/list/role?role=0                # 按角色查询
GET    /user/list/teacher/students?teacherId= # 教师的学生列表
GET    /user/stats                           # 用户统计
```

### 班级相关

```
GET    /classInfo/list?operatorRole=&operatorId=   # 班级列表
GET    /classInfo/detail/students?classId=         # 班级详情
PUT    /classInfo/update/teacher-classes?teacherId=&operatorRole=  # 教师班级绑定
```

### 聊天相关

```
GET    /chat/history/{classId}               # 最近50条历史消息
```

### AI相关

```
POST   /ai/chat                              # AI问答
请求体：{"message": "问题内容", "role": 0}
```

### WebSocket

```
ws://localhost:10001/ws/chat/{classId}/{userId}  # 班级群聊连接
```

---

## 数据库表结构

### t_user（用户表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| username | VARCHAR(50) | 用户名 |
| password | VARCHAR(100) | 密码 |
| real_name | VARCHAR(50) | 真实姓名 |
| role | TINYINT | 0学生 1教师 2管理员 |
| class_id | BIGINT | 所属班级ID |
| status | TINYINT | 0禁用 1正常 |

### t_class（班级表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| class_name | VARCHAR(100) | 班级名称 |
| teacher_id | BIGINT | 班主任ID |
| grade | VARCHAR(10) | 年级 |
| department | VARCHAR(100) | 院系 |

### t_chat_message（消息表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| sender_id | BIGINT | 发送者ID |
| sender_name | VARCHAR(100) | 发送者姓名 |
| receiver_id | BIGINT | 接收者ID |
| content | TEXT | 消息内容 |
| send_time | DATETIME | 发送时间 |

---

## 许可证

MIT License

---

## 联系方式

有问题请提Issue或联系作者。

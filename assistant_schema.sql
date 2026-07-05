-- ============================================================
-- 教师学生助手系统数据库脚本
-- 创建日期：2026-06-26
-- 说明：本脚本用于初始化 teacher_assistant 数据库，包含
--       用户、班级、聊天记录等核心业务表
-- ============================================================

CREATE DATABASE IF NOT EXISTS teacher_assistant
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE teacher_assistant;

-- ============================================================
-- 1. 用户表 (t_user)
--    说明：统一管理教师和学生账户，通过 role 字段区分身份
-- ============================================================
CREATE TABLE t_user (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户主键ID',
    username    VARCHAR(50)  NOT NULL                COMMENT '登录用户名（唯一）',
    password    VARCHAR(100) NOT NULL                COMMENT '登录密码（存储密文）',
    real_name   VARCHAR(50)  NOT NULL                COMMENT '真实姓名',
    role        TINYINT      NOT NULL DEFAULT 0      COMMENT '角色：0-学生，1-教师，2-管理员',
    gender      CHAR(2)      DEFAULT NULL            COMMENT '性别：男/女',
    phone       VARCHAR(20)  DEFAULT NULL            COMMENT '联系电话',
    email       VARCHAR(100) DEFAULT NULL            COMMENT '电子邮箱',
    avatar      VARCHAR(255) DEFAULT NULL            COMMENT '头像地址',
    class_id    BIGINT       DEFAULT NULL            COMMENT '所属班级ID（学生用，教师可为空）',
    status      TINYINT      NOT NULL DEFAULT 1      COMMENT '账户状态：0-禁用，1-正常',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username),
    KEY idx_role (role),
    KEY idx_class_id (class_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================================
-- 2. 班级表 (t_class)
--    说明：管理班级信息，一个班级可包含多名学生
-- ============================================================
CREATE TABLE t_class (
    id            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '班级主键ID',
    class_name    VARCHAR(100) NOT NULL                COMMENT '班级名称（如：计科2101）',
    grade         VARCHAR(10)  DEFAULT NULL            COMMENT '年级（如：2021）',
    department    VARCHAR(100) DEFAULT NULL            COMMENT '所属院系',
    teacher_id    BIGINT       DEFAULT NULL            COMMENT '班主任/负责人ID（关联t_user.id）',
    description   VARCHAR(500) DEFAULT NULL            COMMENT '班级描述',
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_teacher_id (teacher_id),
    CONSTRAINT fk_class_teacher FOREIGN KEY (teacher_id) REFERENCES t_user(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='班级表';

-- 为用户表添加班级外键（班级表创建后才可关联）
ALTER TABLE t_user
    ADD CONSTRAINT fk_user_class FOREIGN KEY (class_id) REFERENCES t_class(id) ON DELETE SET NULL;

-- ============================================================
-- 3. 聊天记录表 (t_chat_message)
--    说明：存储所有聊天消息，支持群聊和私聊两种类型
--    - 群聊：chat_type=0, receiver_id 为班级ID
--    - 私聊：chat_type=1, receiver_id 为对端用户ID
-- ============================================================
CREATE TABLE t_chat_message (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '消息主键ID',
    sender_id   BIGINT       NOT NULL                COMMENT '发送者ID（关联t_user.id）',
    sender_name VARCHAR(100) DEFAULT NULL            COMMENT '发送者真实姓名（冗余字段）',
    receiver_id BIGINT       NOT NULL                COMMENT '接收者ID：群聊时为班级ID，私聊时为用户ID',
    chat_type   TINYINT      NOT NULL DEFAULT 0      COMMENT '聊天类型：0-群聊，1-私聊',
    msg_type    TINYINT      NOT NULL DEFAULT 0      COMMENT '消息类型：0-文本，1-图片，2-文件',
    content     TEXT         NOT NULL                COMMENT '消息内容',
    send_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    is_read     TINYINT      NOT NULL DEFAULT 0      COMMENT '是否已读：0-未读，1-已读',
    PRIMARY KEY (id),
    KEY idx_sender (sender_id),
    KEY idx_receiver (receiver_id),
    KEY idx_chat_type_receiver (chat_type, receiver_id),
    KEY idx_send_time (send_time),
    CONSTRAINT fk_msg_sender   FOREIGN KEY (sender_id)   REFERENCES t_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天记录表';

-- ============================================================
-- 4. 插入初始测试数据
-- ============================================================

-- 管理员
INSERT INTO t_user (username, password, real_name, role, gender, phone)
VALUES ('admin', 'admin123', '系统管理员', 2, '男', '13800000000');

-- 测试班级
INSERT INTO t_class (class_name, grade, department, teacher_id, description)
VALUES ('计科2101', '2021', '计算机学院', NULL, '计算机科学与技术专业2021级1班'),
       ('软工2101', '2021', '计算机学院', NULL, '软件工程专业2021级1班');

-- 教师
INSERT INTO t_user (username, password, real_name, role, gender, phone, email)
VALUES ('T001', '123456', '刘教授',   1, '男', '13800000001', 'liuprof@example.com'),
       ('T002', '123456', '陈老师',   1, '女', '13800000002', 'chentea@example.com');

-- 学生
INSERT INTO t_user (username, password, real_name, role, gender, phone, class_id)
VALUES ('2021001', '123456', '张三', 0, '男', '13900000001', 1),
       ('2021002', '123456', '李四', 0, '女', '13900000002', 1),
       ('2021003', '123456', '王五', 0, '男', '13900000003', 2);

-- 回填班级的班主任
UPDATE t_class SET teacher_id = (SELECT id FROM t_user WHERE username = 'T001') WHERE id = 1;
UPDATE t_class SET teacher_id = (SELECT id FROM t_user WHERE username = 'T002') WHERE id = 2;

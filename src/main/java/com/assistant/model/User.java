package com.assistant.model;

import java.util.Date;

/**
 * 用户实体类
 * 对应数据库表：t_user
 * 统一管理教师、学生、管理员账户，通过 role 字段区分身份
 */
public class User {

    /** 用户主键ID */
    private Long id;

    /** 登录用户名（唯一） */
    private String username;

    /** 登录密码（存储密文） */
    private String password;

    /** 真实姓名 */
    private String realName;

    /**
     * 角色：
     * 0 - 学生
     * 1 - 教师
     * 2 - 管理员
     */
    private Integer role;

    /** 性别：男/女 */
    private String gender;

    /** 联系电话 */
    private String phone;

    /** 电子邮箱 */
    private String email;

    /** 头像地址 */
    private String avatar;

    /** 所属班级ID（学生用，教师/管理员可为空） */
    private Long classId;

    /**
     * 账户状态：
     * 0 - 禁用
     * 1 - 正常
     */
    private Integer status;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;

    // ======================== Getter / Setter ========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}

package com.assistant.model;

import java.util.Date;

/**
 * 班级实体类
 * 对应数据库表：t_class
 * 注意：Java 关键字 class 不能用作类名，故命名为 ClassInfo
 */
public class ClassInfo {

    /** 班级主键ID */
    private Long id;

    /** 班级名称（如：计科2101） */
    private String className;

    /** 年级（如：2021） */
    private String grade;

    /** 所属院系 */
    private String department;

    /** 班主任/负责人ID（关联 t_user.id） */
    private Long teacherId;

    /** 班级描述 */
    private String description;

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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

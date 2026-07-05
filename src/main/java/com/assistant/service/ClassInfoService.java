package com.assistant.service;

import com.assistant.model.ClassInfo;
import com.assistant.model.User;

import java.util.List;
import java.util.Map;

/**
 * 班级业务逻辑接口
 */
public interface ClassInfoService {

    /** 新增班级 */
    int addClass(ClassInfo classInfo);

    /** 根据ID删除班级 */
    int deleteClass(Long id);

    /** 更新班级信息 */
    int updateClass(ClassInfo classInfo);

    /** 根据ID查询班级 */
    ClassInfo getClassById(Long id);

    /** 查询全部班级 */
    List<ClassInfo> listAll();

    /** 根据班主任ID查询其负责的班级 */
    List<ClassInfo> listByTeacherId(Long teacherId);

    /**
     * 查询班级详情（含班内学生列表）
     * 返回 Map，包含 "classInfo"（班级信息）和 "students"（该班所有学生列表）
     */
    Map<String, Object> getClassDetailWithStudents(Long classId);

    /**
     * 更新教师所带班级（仅管理员可操作）
     * 本质是批量更新 t_class 表中对应记录的 teacher_id 字段
     * @param teacherId 教师ID
     * @param classIds 该教师负责的班级ID列表
     * @return 更新的记录数
     */
    int updateTeacherClasses(Long teacherId, List<Long> classIds);
}

package com.assistant.service.impl;

import com.assistant.dao.ClassInfoMapper;
import com.assistant.dao.UserMapper;
import com.assistant.model.ClassInfo;
import com.assistant.model.User;
import com.assistant.service.ClassInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 班级业务逻辑实现类
 */
@Service
public class ClassInfoServiceImpl implements ClassInfoService {

    @Autowired
    private ClassInfoMapper classInfoMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public int addClass(ClassInfo classInfo) {
        return classInfoMapper.insert(classInfo);
    }

    @Override
    public int deleteClass(Long id) {
        return classInfoMapper.deleteById(id);
    }

    @Override
    public int updateClass(ClassInfo classInfo) {
        return classInfoMapper.update(classInfo);
    }

    @Override
    public ClassInfo getClassById(Long id) {
        return classInfoMapper.selectById(id);
    }

    @Override
    public List<ClassInfo> listAll() {
        return classInfoMapper.selectAll();
    }

    @Override
    public List<ClassInfo> listByTeacherId(Long teacherId) {
        return classInfoMapper.selectByTeacherId(teacherId);
    }

    /**
     * 查询班级详情 + 班内所有学生
     * 返回包含 classInfo 和 students 的 Map，前端一次请求拿到完整数据
     */
    @Override
    public Map<String, Object> getClassDetailWithStudents(Long classId) {
        Map<String, Object> result = new HashMap<>();
        ClassInfo classInfo = classInfoMapper.selectById(classId);
        List<User> students = userMapper.selectByClassId(classId);
        result.put("classInfo", classInfo);
        result.put("students", students);
        return result;
    }

    /**
     * 更新教师所带班级（仅管理员可操作）
     * 逻辑：先清空该教师的所有班级绑定，再批量设置新的班级绑定
     * 注意：因为 teacher_id 在 t_class 表中，所以是更新班级表，不是更新用户表
     */
    @Override
    public int updateTeacherClasses(Long teacherId, List<Long> classIds) {
        int count = 0;

        // 1. 先清空该教师原来负责的所有班级（将 teacher_id 设为 NULL）
        List<ClassInfo> oldClasses = classInfoMapper.selectByTeacherId(teacherId);
        if (oldClasses != null) {
            for (ClassInfo cls : oldClasses) {
                cls.setTeacherId(null);
                classInfoMapper.update(cls);
                count++;
            }
        }

        // 2. 再批量设置新的班级绑定
        if (classIds != null && !classIds.isEmpty()) {
            for (Long classId : classIds) {
                ClassInfo cls = classInfoMapper.selectById(classId);
                if (cls != null) {
                    cls.setTeacherId(teacherId);
                    classInfoMapper.update(cls);
                    count++;
                }
            }
        }

        return count;
    }
}

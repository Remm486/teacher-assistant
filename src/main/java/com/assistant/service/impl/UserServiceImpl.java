package com.assistant.service.impl;

import com.assistant.dao.ClassInfoMapper;
import com.assistant.dao.UserMapper;
import com.assistant.model.User;
import com.assistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户业务逻辑实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ClassInfoMapper classInfoMapper;

    /**
     * 用户登录校验
     * 根据用户名查询记录，再比对密码，成功返回完整用户对象，失败返回 null
     */
    @Override
    public User login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return null;
        }
        // 简单密码比对（后续可替换为 BCrypt 等加密比对）
        if (!user.getPassword().equals(password)) {
            return null;
        }
        // 登录成功，清空密码字段后返回，避免密码泄露到前端
        user.setPassword(null);
        return user;
    }

    @Override
    public int addUser(User user) {
        // 新用户默认状态为正常
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        return userMapper.insert(user);
    }

    @Override
    public int deleteUser(Long id) {
        return userMapper.deleteById(id);
    }

    @Override
    public int updateUser(User user) {
        return userMapper.update(user);
    }

    @Override
    public int updateUserProfile(Long userId, User updateData) {
        // 先查询原用户信息
        User existingUser = userMapper.selectById(userId);
        if (existingUser == null) {
            return 0;
        }

        // 安全过滤：只允许更新非敏感字段
        // 允许更新：password、gender、phone、email
        // 强制忽略：id、username、realName、role、classId、status
        if (updateData.getPassword() != null && !updateData.getPassword().isEmpty()) {
            existingUser.setPassword(updateData.getPassword());
        }
        if (updateData.getGender() != null) {
            existingUser.setGender(updateData.getGender());
        }
        if (updateData.getPhone() != null) {
            existingUser.setPhone(updateData.getPhone());
        }
        if (updateData.getEmail() != null) {
            existingUser.setEmail(updateData.getEmail());
        }

        // 注意：不更新 realName、role、classId、status、username、id
        return userMapper.update(existingUser);
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<User> listAll() {
        return userMapper.selectAll();
    }

    @Override
    public List<User> listByRole(Integer role) {
        return userMapper.selectByRole(role);
    }

    @Override
    public List<User> listByClassId(Long classId) {
        return userMapper.selectByClassId(classId);
    }

    @Override
    public List<Long> getClassIdsByTeacherId(Long teacherId) {
        return classInfoMapper.selectClassIdsByTeacherId(teacherId);
    }

    @Override
    public List<User> listStudentsByTeacherId(Long teacherId) {
        // 1. 查询该教师负责的所有班级ID
        List<Long> classIds = classInfoMapper.selectClassIdsByTeacherId(teacherId);
        if (classIds == null || classIds.isEmpty()) {
            return new ArrayList<>();  // 该教师没有负责任何班级
        }

        // 2. 查询这些班级的所有学生
        List<User> allStudents = new ArrayList<>();
        for (Long classId : classIds) {
            List<User> students = userMapper.selectByClassId(classId);
            if (students != null) {
                allStudents.addAll(students);
            }
        }
        return allStudents;
    }

    /**
     * 数据大屏统计：统计各角色的总人数
     * 返回 Map 供前端直接使用
     */
    @Override
    public Map<String, Long> countByRole() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("studentCount", (long) userMapper.selectByRole(0).size());
        stats.put("teacherCount", (long) userMapper.selectByRole(1).size());
        stats.put("adminCount",   (long) userMapper.selectByRole(2).size());
        return stats;
    }
}

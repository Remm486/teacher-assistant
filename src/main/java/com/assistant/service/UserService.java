package com.assistant.service;

import com.assistant.model.User;

import java.util.List;
import java.util.Map;

/**
 * 用户业务逻辑接口
 */
public interface UserService {

    /** 用户登录校验：成功返回完整 User 对象，失败返回 null */
    User login(String username, String password);

    /** 新增用户 */
    int addUser(User user);

    /** 根据ID删除用户 */
    int deleteUser(Long id);

    /** 更新用户信息（管理员专用，可更新所有字段） */
    int updateUser(User user);

    /**
     * 安全更新个人信息（防垂直提权）
     * 仅允许更新：密码、手机号、邮箱、性别、真实姓名
     * 强制忽略：id、username、role、classId、status
     */
    int updateUserProfile(Long userId, User updateData);

    /** 根据ID查询用户 */
    User getUserById(Long id);

    /** 查询全部用户 */
    List<User> listAll();

    /** 根据角色查询用户列表 */
    List<User> listByRole(Integer role);

    /** 根据班级ID查询该班所有学生 */
    List<User> listByClassId(Long classId);

    /**
     * 查询教师负责的所有学生（防水平越权）
     * @param teacherId 教师ID
     * @return 该教师负责的所有班级的学生列表
     */
    List<User> listStudentsByTeacherId(Long teacherId);

    /**
     * 查询教师负责的所有班级ID
     * @param teacherId 教师ID
     * @return 班级ID列表
     */
    List<Long> getClassIdsByTeacherId(Long teacherId);

    /**
     * 数据大屏统计：统计各角色总人数
     * 返回 Map，key 为角色描述，value 为人数
     * 例如 { "studentCount": 3, "teacherCount": 2, "adminCount": 1 }
     */
    Map<String, Long> countByRole();
}

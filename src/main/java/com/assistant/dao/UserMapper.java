package com.assistant.dao;

import com.assistant.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表 Mapper 接口
 * 对应表：t_user
 */
@Mapper
public interface UserMapper {

    /** 新增用户 */
    int insert(User user);

    /** 根据主键ID删除用户 */
    int deleteById(@Param("id") Long id);

    /** 更新用户信息 */
    int update(User user);

    /** 根据主键ID查询用户 */
    User selectById(@Param("id") Long id);

    /** 根据用户名查询用户（登录用） */
    User selectByUsername(@Param("username") String username);

    /** 查询全部用户 */
    List<User> selectAll();

    /** 根据角色查询用户列表（0-学生，1-教师，2-管理员） */
    List<User> selectByRole(@Param("role") Integer role);

    /** 根据班级ID查询该班所有学生 */
    List<User> selectByClassId(@Param("classId") Long classId);
}

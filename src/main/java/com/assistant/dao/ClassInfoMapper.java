package com.assistant.dao;

import com.assistant.model.ClassInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 班级表 Mapper 接口
 * 对应表：t_class
 */
@Mapper
public interface ClassInfoMapper {

    /** 新增班级 */
    int insert(ClassInfo classInfo);

    /** 根据主键ID删除班级 */
    int deleteById(@Param("id") Long id);

    /** 更新班级信息 */
    int update(ClassInfo classInfo);

    /** 根据主键ID查询班级 */
    ClassInfo selectById(@Param("id") Long id);

    /** 查询全部班级 */
    List<ClassInfo> selectAll();

    /** 根据班主任ID查询其负责的班级列表 */
    List<ClassInfo> selectByTeacherId(@Param("teacherId") Long teacherId);

    /** 根据班主任ID查询其负责的班级ID列表 */
    List<Long> selectClassIdsByTeacherId(@Param("teacherId") Long teacherId);
}

package com.assistant.controller;

import com.assistant.common.Result;
import com.assistant.model.ClassInfo;
import com.assistant.service.ClassInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 班级管理接口控制器
 */
@RestController
@RequestMapping("/classInfo")
public class ClassInfoController {

    @Autowired
    private ClassInfoService classInfoService;

    /**
     * 新增班级
     * POST /classInfo/add
     */
    @PostMapping("/add")
    public Result<String> addClass(@RequestBody ClassInfo classInfo) {
        int rows = classInfoService.addClass(classInfo);
        return rows > 0 ? Result.success("新增成功", null) : Result.error("新增失败");
    }

    /**
     * 根据ID删除班级
     * DELETE /classInfo/delete?id=1
     */
    @DeleteMapping("/delete")
    public Result<String> deleteClass(@RequestParam Long id) {
        int rows = classInfoService.deleteClass(id);
        return rows > 0 ? Result.success("删除成功", null) : Result.error("删除失败");
    }

    /**
     * 更新班级信息
     * PUT /classInfo/update
     */
    @PutMapping("/update")
    public Result<String> updateClass(@RequestBody ClassInfo classInfo) {
        int rows = classInfoService.updateClass(classInfo);
        return rows > 0 ? Result.success("更新成功", null) : Result.error("更新失败");
    }

    /**
     * 更新教师所带班级（仅管理员可操作）
     * PUT /classInfo/update/teacher-classes?teacherId=2
     * 请求体：[1, 2, 3]（班级ID列表）
     */
    @PutMapping("/update/teacher-classes")
    public Result<String> updateTeacherClasses(@RequestParam Long teacherId,
                                               @RequestBody List<Long> classIds,
                                               @RequestParam Integer operatorRole) {
        // 权限校验：仅管理员可操作
        if (operatorRole == null || operatorRole != 2) {
            return Result.error(403, "权限不足，仅管理员可修改教师所带班级");
        }

        int rows = classInfoService.updateTeacherClasses(teacherId, classIds);
        return rows > 0 ? Result.success("更新成功", null) : Result.error("更新失败");
    }

    /**
     * 根据ID查询班级详情
     * GET /classInfo/1
     */
    @GetMapping("/{id}")
    public Result<ClassInfo> getClassById(@PathVariable Long id) {
        ClassInfo classInfo = classInfoService.getClassById(id);
        return classInfo != null ? Result.success(classInfo) : Result.error("班级不存在");
    }

    /**
     * 查询班级列表（带权限控制）
     * GET /classInfo/list?operatorRole=1&operatorId=2
     * 教师只能看到自己负责的班级，管理员可以看到所有班级
     */
    @GetMapping("/list")
    public Result<List<ClassInfo>> listAll(@RequestParam(required = false) Integer operatorRole,
                                           @RequestParam(required = false) Long operatorId) {
        // 如果是教师，只返回该教师负责的班级
        if (operatorRole != null && operatorRole == 1 && operatorId != null) {
            return Result.success(classInfoService.listByTeacherId(operatorId));
        }
        // 管理员或其他情况返回所有班级
        return Result.success(classInfoService.listAll());
    }

    /**
     * 根据班主任ID查询其负责的班级
     * GET /classInfo/list/teacher?teacherId=2
     */
    @GetMapping("/list/teacher")
    public Result<List<ClassInfo>> listByTeacherId(@RequestParam Long teacherId) {
        return Result.success(classInfoService.listByTeacherId(teacherId));
    }

    /**
     * 查询班级详情（含班内所有学生列表）
     * GET /classInfo/detail/students?classId=1
     */
    @GetMapping("/detail/students")
    public Result<Map<String, Object>> detailWithStudents(@RequestParam Long classId) {
        return Result.success(classInfoService.getClassDetailWithStudents(classId));
    }
}

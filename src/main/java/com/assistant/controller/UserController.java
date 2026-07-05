package com.assistant.controller;

import com.assistant.common.Result;
import com.assistant.model.User;
import com.assistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户管理接口控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * POST /user/login
     * 请求体：{ "username": "xxx", "password": "xxx", "role": 0 }
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody User user) {
        // 验证必填参数
        if (user.getUsername() == null || user.getPassword() == null || user.getRole() == null) {
            return Result.error(400, "用户名、密码和角色不能为空");
        }

        User loginUser = userService.login(user.getUsername(), user.getPassword());
        if (loginUser == null) {
            return Result.error(401, "用户名或密码错误");
        }

        // 验证角色是否匹配
        if (!loginUser.getRole().equals(user.getRole())) {
            return Result.error(403, "身份与选择不符，请重新选择！");
        }

        return Result.success("登录成功", loginUser);
    }

    /**
     * 新增用户
     * POST /user/add
     */
    @PostMapping("/add")
    public Result<String> addUser(@RequestBody User user) {
        int rows = userService.addUser(user);
        return rows > 0 ? Result.success("新增成功", null) : Result.error("新增失败");
    }

    /**
     * 根据ID删除用户（仅管理员可调用）
     * DELETE /user/delete?id=1&operatorRole=2&operatorId=1
     */
    @DeleteMapping("/delete")
    public Result<String> deleteUser(@RequestParam Long id,
                                     @RequestParam Integer operatorRole,
                                     @RequestParam Long operatorId) {
        // 权限校验：只有管理员可以删除用户
        if (operatorRole == null || operatorRole != 2) {
            return Result.error(403, "权限不足，仅管理员可删除用户");
        }

        // 防止管理员删除自己
        if (operatorId != null && operatorId.equals(id)) {
            return Result.error(400, "不能删除自己的账号");
        }

        int rows = userService.deleteUser(id);
        return rows > 0 ? Result.success("删除成功", null) : Result.error("删除失败");
    }

    /**
     * 更新用户信息（管理员专用，可更新所有字段）
     * PUT /user/update
     */
    @PutMapping("/update")
    public Result<String> updateUser(@RequestBody User user,
                                     @RequestParam Integer operatorRole) {
        // 权限校验：只有管理员可以更新任意用户的所有字段
        if (operatorRole == null || operatorRole != 2) {
            return Result.error(403, "权限不足，仅管理员可执行此操作");
        }

        int rows = userService.updateUser(user);
        return rows > 0 ? Result.success("更新成功", null) : Result.error("更新失败");
    }

    /**
     * 安全更新个人信息（防垂直提权）
     * PUT /user/update/profile?userId=1&operatorId=1
     * 仅允许更新：密码、手机号、邮箱、性别、真实姓名
     * 强制忽略：id、username、role、classId、status
     */
    @PutMapping("/update/profile")
    public Result<String> updateProfile(@RequestBody User updateData,
                                        @RequestParam Long userId,
                                        @RequestParam Long operatorId) {
        // 防止越权：只能更新自己的信息
        if (!userId.equals(operatorId)) {
            return Result.error(403, "无权修改其他用户的信息");
        }

        int rows = userService.updateUserProfile(userId, updateData);
        return rows > 0 ? Result.success("更新成功", null) : Result.error("更新失败");
    }

    /**
     * 根据ID查询用户详情
     * GET /user/1
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return user != null ? Result.success(user) : Result.error("用户不存在");
    }

    /**
     * 查询全部用户列表
     * GET /user/list
     */
    @GetMapping("/list")
    public Result<List<User>> listAll() {
        return Result.success(userService.listAll());
    }

    /**
     * 根据角色查询用户列表
     * GET /user/list/role?role=0
     */
    @GetMapping("/list/role")
    public Result<List<User>> listByRole(@RequestParam Integer role) {
        return Result.success(userService.listByRole(role));
    }

    /**
     * 根据班级ID查询该班所有学生
     * GET /user/list/class?classId=1
     */
    @GetMapping("/list/class")
    public Result<List<User>> listByClassId(@RequestParam Long classId) {
        return Result.success(userService.listByClassId(classId));
    }

    /**
     * 查询教师负责的所有学生（防水平越权）
     * GET /user/list/teacher/students?teacherId=2
     * 教师只能看到自己负责班级的学生
     */
    @GetMapping("/list/teacher/students")
    public Result<List<User>> listStudentsByTeacherId(@RequestParam Long teacherId) {
        List<User> students = userService.listStudentsByTeacherId(teacherId);
        return Result.success(students);
    }

    /**
     * 数据大屏统计：各角色总人数
     * GET /user/stats
     */
    @GetMapping("/stats")
    public Result<Map<String, Long>> stats() {
        return Result.success(userService.countByRole());
    }
}

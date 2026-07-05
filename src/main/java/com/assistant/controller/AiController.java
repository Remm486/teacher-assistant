package com.assistant.controller;

import com.assistant.common.Result;
import com.assistant.model.User;
import com.assistant.service.AiService;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * AI 智能问答接口控制器
 * 封装对小米 MiMo 大模型 API 的调用，对外暴露简洁的 REST 接口
 */
@RestController
@RequestMapping("/ai")
public class AiController {

    private static final Logger log = LoggerFactory.getLogger(AiController.class);

    @Autowired
    private AiService aiService;

    /**
     * 向 AI 发送提问并获取回答（新接口，支持角色感知）
     * POST /ai/chat
     *
     * 请求体示例：
     * {
     *   "message": "什么是数据结构？"
     * }
     *
     * 返回示例（Result 包裹）：
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": "数据结构是计算机科学中……"
     * }
     */
    @PostMapping("/chat")
    public Result<String> chat(@RequestBody JSONObject requestBody, HttpSession session) {
        String message = requestBody.getString("message");

        if (message == null || message.trim().isEmpty()) {
            return Result.error("消息内容不能为空");
        }

        // 从Session中获取当前登录用户的角色
        // 注意：当前项目使用localStorage存储用户信息，前端需要在请求中传递role
        Integer role = requestBody.getInteger("role");
        if (role == null) {
            // 默认为学生角色
            role = 0;
        }

        log.info("[AI] 收到提问 - 角色：{}，消息：{}", role, message);

        try {
            String answer = aiService.chat(role, message);
            return Result.success(answer);
        } catch (Exception e) {
            log.error("[AI] 调用异常：{}", e.getMessage(), e);
            return Result.error("AI 请求处理异常：" + e.getMessage());
        }
    }

    /**
     * 向 AI 发送提问并获取回答（旧接口，保持兼容）
     * POST /ai/ask
     *
     * 请求体示例：
     * {
     *   "prompt": "什么是数据结构？"
     * }
     */
    @PostMapping("/ask")
    public Result<String> ask(@RequestBody JSONObject requestBody) {
        String prompt = requestBody.getString("prompt");

        if (prompt == null || prompt.trim().isEmpty()) {
            return Result.error("prompt 不能为空");
        }

        // 旧接口默认使用学生角色
        Integer role = requestBody.getInteger("role");
        if (role == null) {
            role = 0;
        }

        log.info("[AI] 收到提问（旧接口）- 角色：{}，消息：{}", role, prompt);

        try {
            String answer = aiService.chat(role, prompt);
            return Result.success(answer);
        } catch (Exception e) {
            log.error("[AI] 调用异常：{}", e.getMessage(), e);
            return Result.error("AI 请求处理异常：" + e.getMessage());
        }
    }
}

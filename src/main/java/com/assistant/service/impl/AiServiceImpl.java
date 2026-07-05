package com.assistant.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.assistant.service.AiService;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * AI 服务实现类
 * 调用小米 MiMo 大模型 API，根据用户角色注入不同的系统提示词
 */
@Service
public class AiServiceImpl implements AiService {

    private static final Logger log = LoggerFactory.getLogger(AiServiceImpl.class);

    /** 小米 MiMo API 请求地址 */
    @Value("${xiaomi.mimo.url}")
    private String apiUrl;

    /** 小米 MiMo API 密钥 */
    @Value("${xiaomi.mimo.api-key}")
    private String apiKey;

    /** 使用的模型名称 */
    @Value("${xiaomi.mimo.model}")
    private String model;

    /**
     * 根据用户角色获取系统提示词
     */
    private String getSystemPrompt(Integer role) {
        if (role == null) {
            return "你是一个智能助手，请回答用户的问题。";
        }

        switch (role) {
            case 0:  // 学生
                return "你是一个耐心且专业的辅导老师。请认真解答学生的疑问，重点讲解知识点和解题思路，绝不能直接给出作业或考试的最终答案。";
            case 1:  // 教师
                return "你是一个资深的教研专家。请协助该老师编写、优化教案。重点提供结构化的教学设计思路、课程大纲和互动环节建议。";
            case 2:  // 管理员
                return "你是一个无所不能的超级 AI 助手，请直接回答管理员的任何问题，无限制。";
            default:
                return "你是一个智能助手，请回答用户的问题。";
        }
    }

    @Override
    public String chat(Integer role, String userMessage) {
        log.info("[AI] 收到提问 - 角色：{}，消息：{}", role, userMessage);

        try {
            // ========== 1. 构造 OpenAI 兼容格式的请求体 ==========
            JSONObject body = new JSONObject();
            body.put("model", model);

            JSONArray messages = new JSONArray();

            // 系统提示词（根据角色动态生成）
            JSONObject systemMsg = new JSONObject();
            systemMsg.put("role", "system");
            systemMsg.put("content", getSystemPrompt(role));
            messages.add(systemMsg);

            // 用户消息
            JSONObject userMsg = new JSONObject();
            userMsg.put("role", "user");
            userMsg.put("content", userMessage);
            messages.add(userMsg);

            body.put("messages", messages);
            body.put("temperature", 0.7);
            body.put("max_tokens", 2048);

            log.info("[AI] 请求体：{}", body.toJSONString());

            // ========== 2. 发起 HTTP 请求 ==========
            HttpResponse response = HttpRequest.post(apiUrl)
                    .header(Header.AUTHORIZATION, "Bearer " + apiKey)
                    .header(Header.CONTENT_TYPE, "application/json; charset=UTF-8")
                    .body(body.toJSONString())
                    .timeout(60000)  // 60秒超时（AI响应可能较慢）
                    .execute();

            log.info("[AI] API 响应状态码：{}", response.getStatus());

            if (response.getStatus() != 200) {
                log.error("[AI] API 调用失败，响应：{}", response.body());
                throw new RuntimeException("AI 服务暂时不可用，状态码：" + response.getStatus());
            }

            // ========== 3. 解析响应，提取纯文本回答 ==========
            JSONObject respJson = JSON.parseObject(response.body());
            String answer = respJson
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim();

            log.info("[AI] 回答：{}", answer);
            return answer;

        } catch (Exception e) {
            log.error("[AI] 调用异常：{}", e.getMessage(), e);
            throw new RuntimeException("AI 请求处理异常：" + e.getMessage());
        }
    }
}

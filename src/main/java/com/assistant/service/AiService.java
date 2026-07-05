package com.assistant.service;

/**
 * AI 服务接口
 */
public interface AiService {

    /**
     * 向 AI 发送提问并获取回答
     *
     * @param role        用户角色（0-学生，1-教师，2-管理员）
     * @param userMessage 用户消息内容
     * @return AI 回答内容
     */
    String chat(Integer role, String userMessage);
}

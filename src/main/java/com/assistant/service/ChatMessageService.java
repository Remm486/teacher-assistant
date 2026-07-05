package com.assistant.service;

import com.assistant.model.ChatMessage;

import java.util.List;

/**
 * 聊天消息业务逻辑接口
 */
public interface ChatMessageService {

    /** 新增一条聊天消息 */
    int addMessage(ChatMessage chatMessage);

    /** 根据ID删除消息 */
    int deleteMessage(Long id);

    /** 更新消息（如标记已读） */
    int updateMessage(ChatMessage chatMessage);

    /** 根据ID查询消息 */
    ChatMessage getMessageById(Long id);

    /** 查询全部消息 */
    List<ChatMessage> listAll();

    /** 查询指定班级的群聊消息列表 */
    List<ChatMessage> listGroupMessages(Long classId);

    /**
     * 查询指定班级的最近N条群聊消息（用于历史记录加载）
     * @param classId 班级ID
     * @param limit 限制条数
     * @return 消息列表（按时间正序）
     */
    List<ChatMessage> listRecentGroupMessages(Long classId, int limit);

    /** 查询两个用户之间的私聊消息列表 */
    List<ChatMessage> listPrivateMessages(Long userIdA, Long userIdB);
}

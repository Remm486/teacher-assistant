package com.assistant.service.impl;

import com.assistant.dao.ChatMessageMapper;
import com.assistant.model.ChatMessage;
import com.assistant.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 聊天消息业务逻辑实现类
 */
@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Override
    public int addMessage(ChatMessage chatMessage) {
        return chatMessageMapper.insert(chatMessage);
    }

    @Override
    public int deleteMessage(Long id) {
        return chatMessageMapper.deleteById(id);
    }

    @Override
    public int updateMessage(ChatMessage chatMessage) {
        return chatMessageMapper.update(chatMessage);
    }

    @Override
    public ChatMessage getMessageById(Long id) {
        return chatMessageMapper.selectById(id);
    }

    @Override
    public List<ChatMessage> listAll() {
        return chatMessageMapper.selectAll();
    }

    @Override
    public List<ChatMessage> listGroupMessages(Long classId) {
        return chatMessageMapper.selectGroupMessages(classId);
    }

    @Override
    public List<ChatMessage> listRecentGroupMessages(Long classId, int limit) {
        // 查询最近N条记录（DESC顺序）
        List<ChatMessage> list = chatMessageMapper.selectRecentGroupMessages(classId, limit);
        // 反转为正序（最旧的在上面，最新的在下面）
        Collections.reverse(list);
        return list;
    }

    @Override
    public List<ChatMessage> listPrivateMessages(Long userIdA, Long userIdB) {
        return chatMessageMapper.selectPrivateMessages(userIdA, userIdB);
    }
}

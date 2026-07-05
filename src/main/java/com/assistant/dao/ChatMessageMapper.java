package com.assistant.dao;

import com.assistant.model.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 聊天消息表 Mapper 接口
 * 对应表：t_chat_message
 */
@Mapper
public interface ChatMessageMapper {

    /** 新增一条聊天消息 */
    int insert(ChatMessage chatMessage);

    /** 根据主键ID删除消息 */
    int deleteById(@Param("id") Long id);

    /** 更新消息（如标记已读） */
    int update(ChatMessage chatMessage);

    /** 根据主键ID查询消息 */
    ChatMessage selectById(@Param("id") Long id);

    /** 查询全部消息 */
    List<ChatMessage> selectAll();

    /**
     * 查询群聊消息列表（chat_type = 0）
     * @param classId 班级ID
     */
    List<ChatMessage> selectGroupMessages(@Param("classId") Long classId);

    /**
     * 查询指定班级的最近N条群聊消息（用于历史记录加载）
     * @param classId 班级ID
     * @param limit 限制条数
     */
    List<ChatMessage> selectRecentGroupMessages(@Param("classId") Long classId, @Param("limit") int limit);

    /**
     * 查询两个用户之间的私聊消息列表（chat_type = 1）
     * @param userIdA 用户A的ID
     * @param userIdB 用户B的ID
     */
    List<ChatMessage> selectPrivateMessages(@Param("userIdA") Long userIdA,
                                            @Param("userIdB") Long userIdB);
}

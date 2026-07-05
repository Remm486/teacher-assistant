package com.assistant.model;

import java.util.Date;

/**
 * 聊天消息实体类
 * 对应数据库表：t_chat_message
 * 支持群聊（chat_type=0，receiver_id 为班级ID）
 * 和私聊（chat_type=1，receiver_id 为对端用户ID）
 */
public class ChatMessage {

    /** 消息主键ID */
    private Long id;

    /** 发送者ID（关联 t_user.id） */
    private Long senderId;

    /** 发送者真实姓名（冗余字段，避免关联查询） */
    private String senderName;

    /**
     * 接收者ID：
     * 群聊时为班级ID（关联 t_class.id）
     * 私聊时为对端用户ID（关联 t_user.id）
     */
    private Long receiverId;

    /**
     * 聊天类型：
     * 0 - 群聊
     * 1 - 私聊
     */
    private Integer chatType;

    /**
     * 消息类型：
     * 0 - 文本
     * 1 - 图片
     * 2 - 文件
     */
    private Integer msgType;

    /** 消息内容 */
    private String content;

    /** 发送时间 */
    private Date sendTime;

    /**
     * 是否已读：
     * 0 - 未读
     * 1 - 已读
     */
    private Integer isRead;

    // ======================== Getter / Setter ========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Integer getChatType() {
        return chatType;
    }

    public void setChatType(Integer chatType) {
        this.chatType = chatType;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }
}

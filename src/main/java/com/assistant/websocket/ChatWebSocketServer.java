package com.assistant.websocket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.assistant.model.ChatMessage;
import com.assistant.model.User;
import com.assistant.service.ChatMessageService;
import com.assistant.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 聊天服务端点（班级房间隔离版）
 *
 * 前端连接地址：ws://localhost:10001/ws/chat/{classId}/{userId}
 *
 * 采用双层 Map 实现班级房间隔离：
 * - 外层 Key：classId（班级ID）
 * - 内层 Key：userId（用户ID）
 * - 内层 Value：WebSocket Session
 */
@Component
@ServerEndpoint("/ws/chat/{classId}/{userId}")
public class ChatWebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(ChatWebSocketServer.class);

    /**
     * 班级房间会话表（双层Map实现房间隔离）
     */
    private static final Map<String, Map<String, Session>> ROOM_CLIENTS = new ConcurrentHashMap<>();

    /**
     * 用户信息缓存
     */
    private static final Map<String, String> USER_NAME_CACHE = new ConcurrentHashMap<>();

    /** Spring注入的Service（通过静态setter注入，避免@ServerEndpoint多例模式问题） */
    private static UserService userService;
    private static ChatMessageService chatMessageService;

    /** 当前连接的用户ID */
    private String userId;
    /** 当前连接的班级ID（房间ID） */
    private String classId;
    /** 当前连接用户的真实姓名 */
    private String userName;

    /**
     * Spring注入Service（通过静态setter注入）
     * 注意：@ServerEndpoint是多例模式，不能直接使用@Autowired
     */
    @Autowired
    public void setUserService(UserService userService) {
        ChatWebSocketServer.userService = userService;
    }

    @Autowired
    public void setChatMessageService(ChatMessageService chatMessageService) {
        ChatWebSocketServer.chatMessageService = chatMessageService;
    }

    // ======================== 生命周期方法 ========================

    /**
     * 连接建立成功 - 加入对应班级房间
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("classId") String classId, @PathParam("userId") String userId) {
        this.classId = classId;
        this.userId = userId;

        // 获取用户真实姓名
        this.userName = getUserNameById(userId);
        USER_NAME_CACHE.put(userId, this.userName);

        // 加入对应班级的房间
        ROOM_CLIENTS.putIfAbsent(classId, new ConcurrentHashMap<>());
        Map<String, Session> room = ROOM_CLIENTS.get(classId);
        room.put(userId, session);

        int roomSize = room.size();
        int totalRooms = ROOM_CLIENTS.size();

        log.info("[WebSocket] 用户 {}({}) 加入班级 {} 房间，当前房间人数：{}，总房间数：{}",
                this.userName, userId, classId, roomSize, totalRooms);
    }

    /**
     * 连接关闭 - 从对应班级房间移除
     */
    @OnClose
    public void onClose() {
        // 从对应班级房间移除
        Map<String, Session> room = ROOM_CLIENTS.get(classId);
        if (room != null) {
            room.remove(userId);
            if (room.isEmpty()) {
                ROOM_CLIENTS.remove(classId);
                log.info("[WebSocket] 班级 {} 房间已空，已移除", classId);
            }
        }

        USER_NAME_CACHE.remove(userId);
        log.info("[WebSocket] 用户 {}({}) 离开班级 {} 房间", this.userName, userId, classId);
    }

    /**
     * 连接异常
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("[WebSocket] 用户 {}({}) 在班级 {} 房间连接异常：{}",
                this.userName, userId, classId, error.getMessage(), error);

        Map<String, Session> room = ROOM_CLIENTS.get(classId);
        if (room != null) {
            room.remove(userId);
            if (room.isEmpty()) {
                ROOM_CLIENTS.remove(classId);
            }
        }

        USER_NAME_CACHE.remove(userId);

        try {
            session.close();
        } catch (IOException ignored) {}
    }

    /**
     * 收到客户端消息 - 保存到数据库并广播给当前班级房间
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("[WebSocket] 班级 {} 房间 - 用户 {}({}) 发送消息：{}", classId, this.userName, userId, message);

        // 解析前端传来的JSON
        JSONObject json = JSON.parseObject(message);
        String senderId   = json.getString("senderId");
        String senderName = json.getString("senderName");
        String content    = json.getString("content");
        Integer chatType  = json.getInteger("chatType");

        // 兜底保护
        if (senderId == null || senderId.isEmpty()) {
            senderId = this.userId;
        }
        if (senderName == null || senderName.isEmpty()) {
            senderName = this.userName;
        }

        // 保存消息到数据库
        saveMessageToDatabase(senderId, senderName, content, chatType);

        // 构建广播消息
        String broadcastMsg = buildOutgoingMessage(senderId, senderName, content);

        // 仅广播给当前班级房间内的所有用户
        broadcastToRoom(this.classId, broadcastMsg);
    }

    // ======================== 数据库存储方法 ========================

    /**
     * 保存消息到数据库
     */
    private void saveMessageToDatabase(String senderId, String senderName, String content, Integer chatType) {
        try {
            if (chatMessageService == null) {
                log.error("[WebSocket] ChatMessageService 未注入，无法保存消息");
                return;
            }

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setSenderId(Long.parseLong(senderId));
            chatMessage.setSenderName(senderName);
            chatMessage.setReceiverId(Long.parseLong(this.classId));  // 群聊时receiverId为班级ID
            chatMessage.setChatType(chatType != null ? chatType : 0);  // 默认群聊
            chatMessage.setMsgType(0);  // 默认文本
            chatMessage.setContent(content);
            chatMessage.setIsRead(0);  // 默认未读

            chatMessageService.addMessage(chatMessage);
            log.info("[WebSocket] 消息已保存到数据库 - senderId: {}, classId: {}", senderId, this.classId);
        } catch (Exception e) {
            log.error("[WebSocket] 保存消息到数据库失败：{}", e.getMessage(), e);
        }
    }

    // ======================== 内部推送方法 ========================

    /**
     * 广播消息给指定班级房间内的所有用户
     */
    private void broadcastToRoom(String roomId, String message) {
        Map<String, Session> room = ROOM_CLIENTS.get(roomId);
        if (room == null || room.isEmpty()) {
            log.warn("[WebSocket] 班级 {} 房间不存在或为空，广播失败", roomId);
            return;
        }

        log.info("[WebSocket] 广播消息到班级 {} 房间，当前房间人数：{}", roomId, room.size());

        room.forEach((uid, session) -> {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    log.error("[WebSocket] 向用户 {} 广播消息失败：{}", uid, e.getMessage());
                }
            }
        });
    }

    /**
     * 构建用户消息的下行 JSON
     */
    private String buildOutgoingMessage(String senderId, String senderName, String content) {
        JSONObject json = new JSONObject();
        json.put("senderId",   senderId);
        json.put("senderName", senderName);
        json.put("content",    content);
        json.put("timestamp",  new Date().getTime());
        String result = json.toJSONString();
        log.info("[WebSocket] 构建消息JSON：{}", result);
        return result;
    }

    // ======================== 静态工具方法 ========================

    /**
     * 根据用户ID获取真实姓名（优先从缓存获取）
     */
    private String getUserNameById(String userId) {
        String cachedName = USER_NAME_CACHE.get(userId);
        if (cachedName != null) {
            return cachedName;
        }

        try {
            if (userService != null) {
                User user = userService.getUserById(Long.parseLong(userId));
                if (user != null) {
                    String name = user.getRealName();
                    if (name == null || name.isEmpty()) {
                        name = user.getUsername();
                    }
                    if (name != null && !name.isEmpty()) {
                        return name;
                    }
                }
            }
        } catch (Exception e) {
            log.error("[WebSocket] 查询用户 {} 真实姓名失败：{}", userId, e.getMessage());
        }

        return "";
    }

    /**
     * 获取指定班级房间的在线用户数
     */
    public static int getRoomOnlineCount(String classId) {
        Map<String, Session> room = ROOM_CLIENTS.get(classId);
        return room != null ? room.size() : 0;
    }

    /**
     * 获取所有房间的在线用户总数
     */
    public static int getTotalOnlineCount() {
        int total = 0;
        for (Map<String, Session> room : ROOM_CLIENTS.values()) {
            total += room.size();
        }
        return total;
    }

    /**
     * 获取当前活跃房间数
     */
    public static int getActiveRoomCount() {
        return ROOM_CLIENTS.size();
    }
}

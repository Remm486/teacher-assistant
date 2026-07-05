package com.assistant.controller;

import com.assistant.common.Result;
import com.assistant.model.ChatMessage;
import com.assistant.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 聊天消息接口控制器
 */
@RestController
@RequestMapping("/chat")
public class ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    /**
     * 新增一条聊天消息
     * POST /chat/add
     */
    @PostMapping("/add")
    public Result<String> addMessage(@RequestBody ChatMessage chatMessage) {
        int rows = chatMessageService.addMessage(chatMessage);
        return rows > 0 ? Result.success("发送成功", null) : Result.error("发送失败");
    }

    /**
     * 根据ID删除消息
     * DELETE /chat/delete?id=1
     */
    @DeleteMapping("/delete")
    public Result<String> deleteMessage(@RequestParam Long id) {
        int rows = chatMessageService.deleteMessage(id);
        return rows > 0 ? Result.success("删除成功", null) : Result.error("删除失败");
    }

    /**
     * 更新消息（如标记已读）
     * PUT /chat/update
     */
    @PutMapping("/update")
    public Result<String> updateMessage(@RequestBody ChatMessage chatMessage) {
        int rows = chatMessageService.updateMessage(chatMessage);
        return rows > 0 ? Result.success("更新成功", null) : Result.error("更新失败");
    }

    /**
     * 根据ID查询消息
     * GET /chat/1
     */
    @GetMapping("/{id}")
    public Result<ChatMessage> getMessageById(@PathVariable Long id) {
        ChatMessage message = chatMessageService.getMessageById(id);
        return message != null ? Result.success(message) : Result.error("消息不存在");
    }

    /**
     * 查询全部消息
     * GET /chat/list
     */
    @GetMapping("/list")
    public Result<List<ChatMessage>> listAll() {
        return Result.success(chatMessageService.listAll());
    }

    /**
     * 查询指定班级的群聊消息列表
     * GET /chat/list/group?classId=1
     */
    @GetMapping("/list/group")
    public Result<List<ChatMessage>> listGroupMessages(@RequestParam Long classId) {
        return Result.success(chatMessageService.listGroupMessages(classId));
    }

    /**
     * 查询指定班级的最近N条群聊消息（历史记录加载）
     * GET /chat/history/{classId}
     * 返回最近50条消息（按时间正序）
     */
    @GetMapping("/history/{classId}")
    public Result<List<ChatMessage>> getRecentGroupMessages(@PathVariable Long classId) {
        List<ChatMessage> messages = chatMessageService.listRecentGroupMessages(classId, 50);
        return Result.success(messages);
    }

    /**
     * 查询两个用户之间的私聊消息列表
     * GET /chat/list/private?userIdA=4&userIdB=2
     */
    @GetMapping("/list/private")
    public Result<List<ChatMessage>> listPrivateMessages(@RequestParam Long userIdA,
                                                         @RequestParam Long userIdB) {
        return Result.success(chatMessageService.listPrivateMessages(userIdA, userIdB));
    }
}

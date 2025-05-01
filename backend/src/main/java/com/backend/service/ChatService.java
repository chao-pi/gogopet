package com.backend.service;

import com.backend.model.entity.ChatMessage;
import java.util.List;

public interface ChatService {
    /**
     * 发送消息并获取AI响应
     * @param message 用户消息
     * @param sessionId 会话ID
     * @return AI响应消息
     */
    ChatMessage sendMessage(String message, String sessionId);

    /**
     * 获取历史消息
     * @param sessionId 会话ID
     * @return 历史消息列表
     */
    List<ChatMessage> getHistory(String sessionId);
} 
package com.backend.service.impl;

import com.backend.mapper.ChatMessageMapper;
import com.backend.model.entity.ChatMessage;
import com.backend.service.ChatService;
import com.backend.util.DeepSeekUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private DeepSeekUtil deepSeekUtil;

    @Override
    public ChatMessage sendMessage(String message, String sessionId) {
        // 保存用户消息
        saveMessage(message, sessionId, "user");

        // 获取AI响应
        String aiResponse = deepSeekUtil.getResponse(message);

        // 保存AI响应
        saveMessage(aiResponse, sessionId, "assistant");

        // 返回AI消息
        ChatMessage aiMessage = new ChatMessage();
        aiMessage.setRole("assistant");
        aiMessage.setContent(aiResponse);
        aiMessage.setCreateTime(LocalDateTime.now());
        aiMessage.setSessionId(sessionId);
        return aiMessage;
    }

    @Override
    public List<ChatMessage> getHistory(String sessionId) {
        return chatMessageMapper.selectBySessionId(sessionId);
    }

    @Override
    public void saveMessage(String content, String sessionId, String role) {
        ChatMessage message = new ChatMessage();
        message.setRole(role);
        message.setContent(content);
        message.setCreateTime(LocalDateTime.now());
        message.setSessionId(sessionId);
        chatMessageMapper.insert(message);
    }
} 
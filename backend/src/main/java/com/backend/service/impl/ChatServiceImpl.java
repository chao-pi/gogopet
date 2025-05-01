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
        ChatMessage userMessage = new ChatMessage();
        userMessage.setRole("user");
        userMessage.setContent(message);
        userMessage.setCreateTime(LocalDateTime.now());
        userMessage.setSessionId(sessionId);
        chatMessageMapper.insert(userMessage);

        // 获取AI响应
        String aiResponse = deepSeekUtil.getResponse(message);

        // 保存AI响应
        ChatMessage aiMessage = new ChatMessage();
        aiMessage.setRole("assistant");
        aiMessage.setContent(aiResponse);
        aiMessage.setCreateTime(LocalDateTime.now());
        aiMessage.setSessionId(sessionId);
        chatMessageMapper.insert(aiMessage);

        return aiMessage;
    }

    @Override
    public List<ChatMessage> getHistory(String sessionId) {
        return chatMessageMapper.selectBySessionId(sessionId);
    }
} 
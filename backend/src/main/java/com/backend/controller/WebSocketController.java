package com.backend.controller;

import com.backend.model.entity.WebSocketMessage;
import com.backend.service.ChatService;
import com.backend.util.DeepSeekUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Controller
public class WebSocketController {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private DeepSeekUtil deepSeekUtil;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload WebSocketMessage message) {
        try {
            // 生成会话ID（如果不存在）
            if (message.getSessionId() == null || message.getSessionId().isEmpty()) {
                message.setSessionId(UUID.randomUUID().toString());
            }

            // 保存用户消息
            chatService.saveMessage(message.getContent(), message.getSessionId(), "user");

            // 发送用户消息到前端
            messagingTemplate.convertAndSend("/topic/public", message);

            // 创建AI响应消息
            WebSocketMessage aiMessage = new WebSocketMessage();
            aiMessage.setType("CHAT");
            aiMessage.setRole("assistant");
            aiMessage.setSessionId(message.getSessionId());
            aiMessage.setContent("");

            // 发送AI响应到前端
            messagingTemplate.convertAndSend("/topic/public", aiMessage);

            // 获取AI响应并逐字发送
            deepSeekUtil.getStreamResponse(message.getContent(), (chunk) -> {
                aiMessage.setContent(aiMessage.getContent() + chunk);
                messagingTemplate.convertAndSend("/topic/public", aiMessage);
            });

            // 保存完整的AI响应
            chatService.saveMessage(aiMessage.getContent(), message.getSessionId(), "assistant");

        } catch (Exception e) {
            logger.error("Error processing message", e);
            WebSocketMessage errorMessage = new WebSocketMessage();
            errorMessage.setType("SYSTEM");
            errorMessage.setContent("处理消息时发生错误");
            messagingTemplate.convertAndSend("/topic/public", errorMessage);
        }
    }
} 
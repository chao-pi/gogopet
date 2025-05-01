package com.backend.model.entity;

import lombok.Data;

@Data
public class WebSocketMessage {
    private String type; // 消息类型：CHAT, SYSTEM
    private String content; // 消息内容
    private String sessionId; // 会话ID
    private String role; // 角色：user, assistant
} 
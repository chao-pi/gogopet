package com.backend.model.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessage {
    private Long id;
    private String role;
    private String content;
    private LocalDateTime createTime;
    private String sessionId;
} 
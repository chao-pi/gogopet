package com.backend.controller;

import com.backend.model.entity.ChatMessage;
import com.backend.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @PostMapping("/send")
    public ChatMessage sendMessage(@RequestParam String message, @RequestParam(required = false) String sessionId) {
        if (sessionId == null || sessionId.isEmpty()) {
            sessionId = UUID.randomUUID().toString();
        }
        return chatService.sendMessage(message, sessionId);
    }

    @GetMapping("/history")
    public List<ChatMessage> getHistory(@RequestParam String sessionId) {
        return chatService.getHistory(sessionId);
    }
} 
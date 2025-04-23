package com.backend.service;

import com.backend.model.entity.User;

public interface AuthService {
    String login(String username, String password);
    void register(User user);
} 
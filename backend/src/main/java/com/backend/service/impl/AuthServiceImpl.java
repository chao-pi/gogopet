package com.backend.service.impl;

import com.backend.mapper.UserMapper;
import com.backend.model.entity.User;
import com.backend.service.AuthService;
import com.backend.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String login(String username, String password) {
        User user = userMapper.selectByUserName(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        return jwtUtil.generateToken(username);
    }

    @Override
    public void register(User user) {
        logger.info("开始注册用户: {}", user.getUserName());
        
        // 检查用户名是否已存在
        User existingUser = userMapper.selectByUserName(user.getUserName());
        if (existingUser != null) {
            logger.warn("用户名已存在: {}", user.getUserName());
            throw new RuntimeException("用户名已存在");
        }
        
        // 生成用户ID
        String userId = java.util.UUID.randomUUID().toString().substring(0, 18);
        user.setUserId(userId);
        logger.info("生成用户ID: {}", userId);
        
        try {
            // 保存用户
            int result = userMapper.insert(user);
            logger.info("用户注册结果: {}", result);
        } catch (Exception e) {
            logger.error("用户注册失败", e);
            throw new RuntimeException("注册失败: " + e.getMessage());
        }
    }
} 
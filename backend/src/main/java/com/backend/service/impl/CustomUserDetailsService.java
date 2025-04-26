package com.backend.service.impl;

import com.backend.mapper.UserMapper;
import com.backend.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 自定义UserDetailsService实现
 * 用于从数据库中加载用户信息，供Spring Security使用
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        System.out.println("尝试加载用户: " + userId);
        
        // 从数据库中查询用户
        User user = userMapper.selectById(userId);
        
        if (user == null) {
            System.out.println("用户不存在: " + userId);
            throw new UsernameNotFoundException("用户不存在: " + userId);
        }
        
        System.out.println("找到用户: " + user.getUserName() + ", 密码: " + user.getPassword());
        
        // 根据用户类型设置角色
        String role = "ROLE_" + user.getUserType();
        
        // 创建UserDetails对象，使用用户ID作为用户名
        return new org.springframework.security.core.userdetails.User(
                user.getUserId(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }
} 
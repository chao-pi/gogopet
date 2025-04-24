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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("尝试加载用户: " + username);
        
        // 从数据库中查询用户
        User user = userMapper.selectByUserName(username);
        
        if (user == null) {
            System.out.println("用户不存在: " + username);
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        
        System.out.println("找到用户: " + user.getUserName() + ", 密码: " + user.getPassword());
        
        // 根据用户类型设置角色
        String role = "ROLE_" + user.getUserType();
        
        // 创建UserDetails对象
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }
} 
package com.backend.service.impl;

import com.backend.mapper.UserMapper;
import com.backend.model.dto.UserDTO;
import com.backend.model.dto.UserRegisterDTO;
import com.backend.model.dto.UserLoginDTO;
import com.backend.model.entity.User;
import com.backend.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 生成18位用户ID
    private String generateUserId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 18);
    }

    @Override
    @Transactional
    public UserDTO register(UserRegisterDTO userRegisterDTO) {
        // 检查用户名是否已存在
        User existingUser = userMapper.selectByUserName(userRegisterDTO.getUserName());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建新用户
        User user = new User();
        user.setUserName(userRegisterDTO.getUserName());
        user.setUserType(userRegisterDTO.getUserType());
        user.setUserAddress(userRegisterDTO.getUserAddress());
        
        // 设置用户ID和加密密码
        user.setUserId(generateUserId());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));

        // 保存用户
        userMapper.insert(user);

        // 返回用户信息
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @Override
    public UserDTO login(UserLoginDTO userLoginDTO) {
        // 查询用户
        User user = userMapper.selectByUserName(userLoginDTO.getUserName());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 验证密码
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 返回用户信息
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @Override
    public UserDTO getUserInfo(String userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @Override
    @Transactional
    public UserDTO updateUserInfo(UserDTO userDTO) {
        User user = userMapper.selectById(userDTO.getUserId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 更新用户信息
        user.setUserName(userDTO.getUserName());
        user.setUserType(userDTO.getUserType());
        user.setUserAddress(userDTO.getUserAddress());
        user.setPictureId(userDTO.getPictureId());
        user.setCompanyId(userDTO.getCompanyId());
        
        userMapper.update(user);

        return userDTO;
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        userMapper.deleteById(userId);
    }
} 
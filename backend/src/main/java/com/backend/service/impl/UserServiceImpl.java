package com.backend.service.impl;

import com.backend.mapper.UserMapper;
import com.backend.model.dto.UserDTO;
import com.backend.model.dto.RegisterDTO;
import com.backend.model.dto.LoginDTO;
import com.backend.model.dto.LoginResultDTO;
import com.backend.model.dto.ChangePasswordDTO;
import com.backend.model.entity.User;
import com.backend.service.UserService;
import com.backend.util.JwtUtil;
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

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 生成18位用户ID
    private String generateUserId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 18);
    }

    @Override
    @Transactional
    public UserDTO register(RegisterDTO userRegisterDTO) {
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
    public LoginResultDTO login(LoginDTO userLoginDTO) {
        // 查询用户
        User user = userMapper.selectByUserName(userLoginDTO.getUserName());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 检查密码
        boolean passwordMatches = false;
        try {
            // 尝试使用 BCrypt 验证
            passwordMatches = passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword());
            
            // 如果密码不匹配且不是 BCrypt 格式，尝试直接比较
            if (!passwordMatches && !user.getPassword().startsWith("$2a$")) {
                passwordMatches = userLoginDTO.getPassword().equals(user.getPassword());
                
                // 如果是直接比较成功，更新密码为 BCrypt 格式
                if (passwordMatches) {
                    user.setPassword(passwordEncoder.encode(userLoginDTO.getPassword()));
                    userMapper.update(user);
                }
            }
        } catch (Exception e) {
            // 如果 BCrypt 验证失败，尝试直接比较
            passwordMatches = userLoginDTO.getPassword().equals(user.getPassword());
            
            // 如果是直接比较成功，更新密码为 BCrypt 格式
            if (passwordMatches) {
                user.setPassword(passwordEncoder.encode(userLoginDTO.getPassword()));
                userMapper.update(user);
            }
        }

        if (!passwordMatches) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 构建 UserDTO
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);

        // 生成 JWT Token，使用用户ID而不是用户名
        String token = jwtUtil.generateToken(user.getUserId());

        // 返回结果
        LoginResultDTO result = new LoginResultDTO();
        result.setToken(token);
        result.setUser(userDTO);
        return result;
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
        // 添加日志输出
        System.out.println("更新用户信息，用户ID: " + userDTO.getUserId());
        
        User user = userMapper.selectById(userDTO.getUserId());
        if (user == null) {
            System.out.println("用户不存在，用户ID: " + userDTO.getUserId());
            throw new RuntimeException("用户不存在");
        }

        // 更新用户信息
        user.setUserName(userDTO.getUserName());
        user.setUserAddress(userDTO.getUserAddress());
        
        // 执行更新
        int result = userMapper.update(user);
        System.out.println("更新结果: " + result);

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

    @Override
    @Transactional
    public boolean changePassword(ChangePasswordDTO changePasswordDTO) {
        // 验证新密码和确认密码是否一致
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new RuntimeException("新密码和确认密码不一致");
        }

        // 查询用户
        User user = userMapper.selectById(changePasswordDTO.getUserId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证旧密码
        boolean passwordMatches = false;
        try {
            // 尝试使用 BCrypt 验证
            passwordMatches = passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword());
            
            // 如果密码不匹配且不是 BCrypt 格式，尝试直接比较
            if (!passwordMatches && !user.getPassword().startsWith("$2a$")) {
                passwordMatches = changePasswordDTO.getOldPassword().equals(user.getPassword());
            }
        } catch (Exception e) {
            // 如果 BCrypt 验证失败，尝试直接比较
            passwordMatches = changePasswordDTO.getOldPassword().equals(user.getPassword());
        }

        if (!passwordMatches) {
            throw new RuntimeException("旧密码错误");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        int result = userMapper.update(user);

        return result > 0;
    }
}

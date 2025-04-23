package com.backend.service;

import com.backend.model.dto.UserDTO;
import com.backend.model.dto.LoginDTO;
import com.backend.model.dto.RegisterDTO;
import com.backend.model.dto.LoginResultDTO;

public interface UserService {
    /**
     * 用户注册
     * @param userRegisterDTO 用户注册信息
     * @return 注册后的用户信息
     */
    UserDTO register(RegisterDTO userRegisterDTO);

    /**
     * 用户登录
     * @param userLoginDTO 用户登录信息
     * @return 登录后的用户信息
     */
//    UserDTO login(UserLoginDTO userLoginDTO);
    LoginResultDTO login(LoginDTO userLoginDTO);

    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    UserDTO getUserInfo(String userId);

    /**
     * 更新用户信息
     * @param userDTO 用户信息
     * @return 更新后的用户信息
     */
    UserDTO updateUserInfo(UserDTO userDTO);

    /**
     * 删除用户
     * @param userId 用户ID
     */
    void deleteUser(String userId);
} 
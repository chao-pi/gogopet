package com.backend.controller;

import com.backend.model.dto.LoginDTO;
import com.backend.model.dto.LoginResultDTO;
import com.backend.model.dto.RegisterDTO;
import com.backend.model.dto.UserDTO;
import com.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 处理用户登录、注册等认证相关请求
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param registerDTO 注册信息
     * @return 注册成功的用户信息
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterDTO registerDTO) {
        UserDTO userDTO = userService.register(registerDTO);
        return ResponseEntity.ok(userDTO);
    }

    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return 登录结果，包含token和用户信息
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResultDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginResultDTO loginResult = userService.login(loginDTO);
        return ResponseEntity.ok(loginResult);
    }
}

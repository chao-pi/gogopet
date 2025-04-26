package com.backend.controller;

import com.backend.model.dto.UserDTO;
import com.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 * 处理用户信息相关的请求
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable String userId) {
        UserDTO userDTO = userService.getUserInfo(userId);
        return ResponseEntity.ok(userDTO);
    }

    /**
     * 更新用户信息
     * @param userDTO 用户信息
     * @return 更新后的用户信息
     */
    @PutMapping
    public ResponseEntity<UserDTO> updateUserInfo(@Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUserInfo(userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * 删除用户
     * @param userId 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
} 
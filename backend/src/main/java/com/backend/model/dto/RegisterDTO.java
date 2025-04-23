package com.backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotBlank(message = "用户名不能为空")
    @Size(max = 63, message = "用户名长度不能超过63个字符")
    private String userName;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    @NotBlank(message = "用户类型不能为空")
    @Size(max = 1, message = "用户类型长度必须为1个字符")
    private String userType;  // U-普通用户，C-托运公司，A-管理员

    @Size(max = 63, message = "用户地址长度不能超过63个字符")
    private String userAddress;
} 
package com.backend.model.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
    @Size(min = 18, max = 18, message = "用户ID长度必须为18个字符")
    private String userId;

    @Size(max = 63, message = "用户名长度不能超过63个字符")
    private String userName;

    @Size(max = 1, message = "用户类型长度必须为1个字符")
    private String userType;  // U-普通用户，C-托运公司，A-管理员

    @Size(max = 63, message = "用户地址长度不能超过63个字符")
    private String userAddress;

    @Size(min = 18, max = 18, message = "头像ID长度必须为18个字符")
    private String pictureId;

    @Size(min = 18, max = 18, message = "公司ID长度必须为18个字符")
    private String companyId;

    @Size(max = 255, message = "头像URL长度不能超过255个字符")
    private String avatarUrl;
}
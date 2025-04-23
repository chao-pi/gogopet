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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
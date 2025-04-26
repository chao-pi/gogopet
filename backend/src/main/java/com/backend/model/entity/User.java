package com.backend.model.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 用户实体类
 * 对应数据库表 t_user
 */
@TableName("t_user")
public class User {
    /**
     * 用户ID，唯一标识符
     */
    @TableId("user_id")
    private String userId;

    /**
     * 用户名，用于登录和显示
     */
    @TableField("user_name")
    private String userName;

    /**
     * 用户密码，加密存储
     */
    @TableField("password")
    private String password;

    /**
     * 用户类型：U-普通用户，C-托运公司，A-管理员
     */
    @TableField("user_type")
    private String userType;

    /**
     * 用户地址，用于托运服务
     */
    @TableField("user_address")
    private String userAddress;

    /**
     * 用户头像ID，关联图片表
     */
    @TableField("picture_id")
    private String pictureId;

    /**
     * 公司ID，关联公司表（仅托运公司用户有值）
     */
    @TableField("company_id")
    private String companyId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
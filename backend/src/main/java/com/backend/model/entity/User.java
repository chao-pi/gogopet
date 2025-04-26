package com.backend.model.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户实体类
 * 对应数据库表 t_user
 */
@Data
@Getter
@Setter
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
}
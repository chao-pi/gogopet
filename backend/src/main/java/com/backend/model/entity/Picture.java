package com.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;

/**
 * 图片实体类
 * 对应数据库表 t_picture
 */
@TableName("t_picture")
public class Picture {
    /**
     * 图片ID，唯一标识符
     */
    @TableId("picture_id")
    private String pictureId;

    /**
     * 图片用途
     * A-头像
     * P-宠物照片
     * O-订单照片
     */
    @TableField("picture_usage")
    private String pictureUsage;

    /**
     * 图片URL，存储图片的服务路径
     */
    @TableField("picture_url")
    private String pictureUrl;

    /**
     * 上传时间
     */
    @TableField("upload_time")
    private Date uploadTime;

    /**
     * 用户ID，关联上传者
     */
    @TableField("user_id")
    private String userId;

    // Getters and Setters
    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public String getPictureUsage() {
        return pictureUsage;
    }

    public void setPictureUsage(String pictureUsage) {
        this.pictureUsage = pictureUsage;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
} 
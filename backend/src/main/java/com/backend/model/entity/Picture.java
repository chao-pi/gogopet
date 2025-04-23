package com.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.util.Date;

/**
 * 图片实体类
 * 对应数据库表 t_picture
 */
@Data
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
} 
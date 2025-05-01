package com.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.util.Date;

/**
 * 帖子图片实体类
 * 对应数据库表 t_post_image
 */
@Data
@TableName("t_post_image")
public class PostImage {
    /**
     * 图片ID，唯一标识符
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String imageId;

    /**
     * 帖子ID，关联帖子
     */
    @TableField("post_id")
    private String postId;

    /**
     * 图片URL
     */
    @TableField("image_url")
    private String imageUrl;

    /**
     * 图片顺序
     */
    @TableField("image_order")
    private Integer imageOrder;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
} 
package com.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.util.Date;

/**
 * 帖子点赞实体类
 * 对应数据库表 t_post_like
 */
@Data
@TableName("t_post_like")
public class PostLike {
    /**
     * 点赞ID，唯一标识符
     */
    @TableId("like_id")
    private String likeId;

    /**
     * 帖子ID，关联帖子
     */
    @TableField("post_id")
    private String postId;

    /**
     * 用户ID，关联点赞用户
     */
    @TableField("user_id")
    private String userId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
} 
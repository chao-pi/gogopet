package com.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.util.Date;

/**
 * 帖子实体类
 * 对应数据库表 t_post
 */
@Data
@TableName("t_post")
public class Post {
    /**
     * 帖子ID，唯一标识符
     */
    @TableId("post_id")
    private String postId;

    /**
     * 用户ID，关联发帖人
     */
    @TableField("user_id")
    private String userId;

    /**
     * 帖子内容
     */
    @TableField("post_content")
    private String postContent;

    /**
     * 评论数量
     */
    @TableField("post_comment")
    private Integer postComment;

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
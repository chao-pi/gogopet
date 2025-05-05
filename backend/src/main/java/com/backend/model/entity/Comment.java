package com.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.util.Date;

/**
 * 评论实体类
 * 对应数据库表 t_comment
 */
@Data
@TableName("t_comment")
public class Comment {
    /**
     * 评论ID，唯一标识符
     */
    @TableId(value = "comment_id", type = IdType.ASSIGN_ID)
    private String commentId;

    /**
     * 帖子ID，关联帖子
     */
    @TableField("post_id")
    private String postId;

    /**
     * 用户ID，关联评论者
     */
    @TableField("user_id")
    private String userId;

    /**
     * 评论者用户名
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 评论者头像
     */
    @TableField(exist = false)
    private String userAvatar;

    /**
     * 评论内容
     */
    @TableField("comment_content")
    private String commentContent;

    /**
     * 父评论ID，用于回复功能
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 评论状态：0-已删除，1-正常
     */
    @TableField("comment_status")
    private Integer commentStatus;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
} 
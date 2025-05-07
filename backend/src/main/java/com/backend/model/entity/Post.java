package com.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    @TableId(value = "post_id", type = IdType.ASSIGN_ID)
    private String postId;

    /**
     * 用户ID，关联发帖人
     */
    @TableField("user_id")
    private String userId;

    /**
     * 发帖人用户名
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 发帖人头像
     */
    @TableField(exist = false)
    private String userAvatar;

    /**
     * 帖子标题
     */
    @TableField("post_title")
    private String postTitle;

    /**
     * 帖子内容
     */
    @TableField("post_content")
    private String postContent;

    /**
     * 帖子图片URL列表，以逗号分隔
     */
    @TableField("post_images")
    private String postImages;

    /**
     * 点赞数量
     */
    @TableField("post_likes")
    private Integer postLikes;

    /**
     * 评论数量
     */
    @TableField("post_comment")
    private Integer postComment;

    /**
     * 帖子状态：0-已删除，1-正常
     */
    @TableField("post_status")
    private Integer postStatus;

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

    @TableField(exist = false)
    private Double hotScore;  // 热度分数

    // Getters and Setters
    public Double getHotScore() {
        return hotScore;
    }

    public void setHotScore(Double hotScore) {
        this.hotScore = hotScore;
    }
} 
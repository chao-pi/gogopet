package com.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
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
    @TableId("comment_id")
    private String commentId;

    /**
     * 公司ID，关联被评论的公司
     */
    @TableField("company_id")
    private String companyId;

    /**
     * 用户ID，关联评论人
     */
    @TableField("user_id")
    private String userId;

    /**
     * 评论内容
     */
    @TableField("comment_content")
    private String commentContent;

    /**
     * 评分，1-5分
     */
    @TableField("rating")
    private Integer rating;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
} 
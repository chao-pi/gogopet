package com.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 评论实体类
 */
@Data
@TableName("t_comment")
public class Comment {
    /**
     * 评论ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String commentId;

    /**
     * 公司ID
     */
    private String companyId;

    /**
     * 评分
     */
    private Integer rating;

    /**
     * 评论内容
     */
    private String content;
} 
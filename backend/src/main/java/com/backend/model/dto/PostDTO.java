package com.backend.model.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 帖子数据传输对象
 */
@Data
public class PostDTO {
    /**
     * 帖子ID，唯一标识符
     */
    @Size(min = 18, max = 18, message = "帖子ID长度必须为18个字符")
    private String postId;

    /**
     * 用户ID，关联发帖用户
     */
    @Size(min = 18, max = 18, message = "用户ID长度必须为18个字符")
    private String userId;

    /**
     * 帖子内容
     */
    @Size(max = 1000, message = "帖子内容长度不能超过1000个字符")
    private String postContent;

    /**
     * 评论数量
     */
    private Integer postComment;
} 
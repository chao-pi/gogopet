package com.backend.model.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;

/**
 * 评论数据传输对象
 */
@Data
public class CommentDTO {
    /**
     * 评论ID，唯一标识符
     */
    @Size(min = 18, max = 18, message = "评论ID长度必须为18个字符")
    private String commentId;

    /**
     * 公司ID，关联被评论的公司
     */
    @Size(min = 18, max = 18, message = "公司ID长度必须为18个字符")
    private String companyId;

    /**
     * 帖子ID，关联被评论的帖子
     */
    @Size(min = 1, max = 18, message = "帖子ID长度必须在1-18个字符之间")
    private String postId;

    /**
     * 用户ID，关联评论用户
     */
    @Size(min = 18, max = 18, message = "用户ID长度必须为18个字符")
    private String userId;

    /**
     * 评论内容
     */
    @Size(max = 1000, message = "评论内容长度不能超过1000个字符")
    private String commentContent;

    /**
     * 评分，1-5分
     */
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最小为1分")
    @Max(value = 5, message = "评分最大为5分")
    private Integer rating;
} 
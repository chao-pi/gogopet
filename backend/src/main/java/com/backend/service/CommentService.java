package com.backend.service;

import com.backend.model.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 评论服务接口
 */
public interface CommentService extends IService<Comment> {
    /**
     * 根据公司ID获取评论列表
     *
     * @param companyId 公司ID
     * @return 评论列表
     */
    List<Comment> getCommentsByCompanyId(String companyId);

    /**
     * 添加评论
     *
     * @param comment 评论信息
     * @return 是否添加成功
     */
    boolean addComment(Comment comment);

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     * @return 是否删除成功
     */
    boolean deleteComment(String commentId);
} 
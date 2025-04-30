package com.backend.service;

import com.backend.model.entity.Comment;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CommentService extends IService<Comment> {
    /**
     * 创建评论
     */
    String createComment(Comment comment);

    /**
     * 分页获取帖子的评论列表
     */
    Page<Comment> getCommentList(String postId, int page, int size);

    /**
     * 删除评论
     */
    boolean deleteComment(String commentId);

    /**
     * 获取评论的回复列表
     */
    Page<Comment> getReplyList(String commentId, int page, int size);
} 
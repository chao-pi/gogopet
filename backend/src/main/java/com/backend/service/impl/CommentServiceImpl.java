package com.backend.service.impl;

import com.backend.mapper.CommentMapper;
import com.backend.model.entity.Comment;
import com.backend.service.CommentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论服务实现类
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Override
    public List<Comment> getCommentsByCompanyId(String companyId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getCompanyId, companyId);
        return list(queryWrapper);
    }

    @Override
    public boolean addComment(Comment comment) {
        return save(comment);
    }

    @Override
    public boolean deleteComment(String commentId) {
        return removeById(commentId);
    }
} 
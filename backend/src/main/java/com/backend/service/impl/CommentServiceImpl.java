package com.backend.service.impl;

import com.backend.mapper.CommentMapper;
import com.backend.mapper.PostMapper;
import com.backend.mapper.UserMapper;
import com.backend.model.entity.Comment;
import com.backend.model.entity.User;
import com.backend.service.CommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 生成18位的ID
     */
    private String generateId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 18);
    }

    @Override
    @Transactional
    public String createComment(Comment comment) {
        try {
            logger.debug("开始创建评论，评论数据: {}", comment);
            
            // 验证必要字段
            if (comment.getPostId() == null || comment.getPostId().trim().isEmpty()) {
                throw new IllegalArgumentException("帖子ID不能为空");
            }
            if (comment.getPostId().length() != 18) {
                throw new IllegalArgumentException("帖子ID长度必须为18个字符");
            }
            if (comment.getUserId() == null || comment.getUserId().trim().isEmpty()) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            if (comment.getCommentContent() == null || comment.getCommentContent().trim().isEmpty()) {
                throw new IllegalArgumentException("评论内容不能为空");
            }

            // 检查帖子是否存在
            if (postMapper.selectById(comment.getPostId()) == null) {
                throw new IllegalArgumentException("帖子不存在");
            }

        // 生成评论ID
            String commentId = generateId();
            comment.setCommentId(commentId);
            
        // 设置初始值
        comment.setCommentStatus(1);
        comment.setCreateTime(new Date());
            
            logger.debug("准备保存评论，评论ID: {}, 评论数据: {}", commentId, comment);
            
        // 保存评论
            boolean saveResult = save(comment);
            if (!saveResult) {
                logger.error("保存评论失败，评论数据: {}", comment);
                throw new RuntimeException("保存评论失败");
            }
            
            logger.debug("评论保存成功，准备更新帖子评论数");
            
        // 更新帖子评论数
            int updateResult = postMapper.updateComments(comment.getPostId(), 1);
            if (updateResult <= 0) {
                logger.error("更新帖子评论数失败，帖子ID: {}", comment.getPostId());
                throw new RuntimeException("更新帖子评论数失败");
            }
            
            logger.debug("评论创建成功，评论ID: {}", commentId);
            return commentId;
        } catch (IllegalArgumentException e) {
            logger.error("评论创建参数错误: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("创建评论失败", e);
            throw new RuntimeException("创建评论失败: " + e.getMessage());
        }
    }

    @Override
    public Page<Comment> getCommentList(String postId, int page, int size) {
        logger.debug("获取评论列表，帖子ID: {}, 页码: {}, 每页大小: {}", postId, page, size);
        try {
        Page<Comment> pageParam = new Page<>(page, size);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId)
                   .eq("comment_status", 1)
                   .isNull("parent_id")
                   .orderByDesc("create_time");
            
            // 获取评论列表
            Page<Comment> result = page(pageParam, queryWrapper);
            
            // 获取所有评论的用户ID
            List<String> userIds = result.getRecords().stream()
                .map(Comment::getUserId)
                .distinct()
                .collect(Collectors.toList());
            
            if (!userIds.isEmpty()) {
                // 批量查询用户信息
                for (String userId : userIds) {
                    User user = userMapper.selectById(userId);
                    if (user != null) {
                        // 更新所有该用户的评论
                        result.getRecords().stream()
                            .filter(comment -> comment.getUserId().equals(userId))
                            .forEach(comment -> {
                                comment.setUserName(user.getUserName());
                                String avatarUrl = user.getAvatarUrl();
                                if (avatarUrl != null && !avatarUrl.isEmpty()) {
                                    comment.setUserAvatar(avatarUrl);
                                } else {
                                    comment.setUserAvatar(user.getUserAvatar());
                                }
                            });
                    }
                }
            }
            
            logger.debug("获取评论列表成功，评论数量: {}", result.getRecords().size());
            return result;
        } catch (Exception e) {
            logger.error("获取评论列表失败", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean deleteComment(String commentId) {
        logger.debug("删除评论，评论ID: {}", commentId);
        try {
            // 获取评论信息
        Comment comment = getById(commentId);
        if (comment != null) {
                // 直接从数据库中删除评论
                boolean deleteResult = removeById(commentId);
                if (deleteResult) {
            // 更新帖子评论数
            postMapper.updateComments(comment.getPostId(), -1);
                    logger.debug("评论删除成功，评论ID: {}", commentId);
                    return true;
                }
            }
            logger.warn("评论不存在或删除失败，评论ID: {}", commentId);
        return false;
        } catch (Exception e) {
            logger.error("删除评论失败", e);
            throw e;
        }
    }

    @Override
    public Page<Comment> getReplyList(String commentId, int page, int size) {
        logger.debug("获取回复列表，评论ID: {}, 页码: {}, 每页大小: {}", commentId, page, size);
        try {
        Page<Comment> pageParam = new Page<>(page, size);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", commentId)
                   .eq("comment_status", 1)
                   .orderByAsc("create_time");
        return page(pageParam, queryWrapper);
        } catch (Exception e) {
            logger.error("获取回复列表失败", e);
            throw e;
        }
    }
} 
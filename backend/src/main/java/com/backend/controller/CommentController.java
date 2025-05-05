package com.backend.controller;

import com.backend.model.entity.Comment;
import com.backend.service.CommentService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    /**
     * 创建评论
     */
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody Comment comment) {
        try {
            logger.debug("收到创建评论请求: {}", comment);
        String commentId = commentService.createComment(comment);
        return ResponseEntity.ok(commentId);
        } catch (IllegalArgumentException e) {
            logger.error("评论创建参数错误: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("创建评论失败", e);
            String errorMessage = e.getMessage();
            if (e.getCause() != null) {
                errorMessage += "\n原因: " + e.getCause().getMessage();
            }
            return ResponseEntity.internalServerError().body("创建评论失败: " + errorMessage);
        }
    }

    /**
     * 获取帖子的评论列表
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getCommentList(
            @PathVariable String postId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.debug("获取评论列表，帖子ID: {}, 页码: {}, 每页大小: {}", postId, page, size);
        Page<Comment> commentPage = commentService.getCommentList(postId, page, size);
        return ResponseEntity.ok(commentPage);
        } catch (Exception e) {
            logger.error("获取评论列表失败", e);
            String errorMessage = e.getMessage();
            if (e.getCause() != null) {
                errorMessage += "\n原因: " + e.getCause().getMessage();
            }
            return ResponseEntity.internalServerError().body("获取评论列表失败: " + errorMessage);
        }
    }

    /**
     * 获取评论的回复列表
     */
    @GetMapping("/reply/{commentId}")
    public ResponseEntity<?> getReplyList(
            @PathVariable String commentId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.debug("获取回复列表，评论ID: {}, 页码: {}, 每页大小: {}", commentId, page, size);
        Page<Comment> replyPage = commentService.getReplyList(commentId, page, size);
        return ResponseEntity.ok(replyPage);
        } catch (Exception e) {
            logger.error("获取回复列表失败", e);
            String errorMessage = e.getMessage();
            if (e.getCause() != null) {
                errorMessage += "\n原因: " + e.getCause().getMessage();
            }
            return ResponseEntity.internalServerError().body("获取回复列表失败: " + errorMessage);
        }
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable String commentId) {
        try {
            logger.debug("删除评论，评论ID: {}", commentId);
        boolean result = commentService.deleteComment(commentId);
        return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("删除评论失败", e);
            String errorMessage = e.getMessage();
            if (e.getCause() != null) {
                errorMessage += "\n原因: " + e.getCause().getMessage();
            }
            return ResponseEntity.internalServerError().body("删除评论失败: " + errorMessage);
        }
    }
} 
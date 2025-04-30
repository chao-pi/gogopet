package com.backend.controller;

import com.backend.service.PostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/likes")
public class PostLikeController {

    @Autowired
    private PostLikeService postLikeService;

    /**
     * 点赞帖子
     */
    @PostMapping("/{postId}")
    public ResponseEntity<Boolean> likePost(
            @PathVariable String postId,
            @RequestParam String userId) {
        boolean result = postLikeService.likePost(postId, userId);
        return ResponseEntity.ok(result);
    }

    /**
     * 取消点赞
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<Boolean> unlikePost(
            @PathVariable String postId,
            @RequestParam String userId) {
        boolean result = postLikeService.unlikePost(postId, userId);
        return ResponseEntity.ok(result);
    }

    /**
     * 检查用户是否已点赞
     */
    @GetMapping("/check/{postId}")
    public ResponseEntity<Boolean> checkUserLiked(
            @PathVariable String postId,
            @RequestParam String userId) {
        boolean result = postLikeService.checkUserLiked(postId, userId);
        return ResponseEntity.ok(result);
    }
} 
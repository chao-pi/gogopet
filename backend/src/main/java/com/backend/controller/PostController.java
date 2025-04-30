package com.backend.controller;

import com.backend.model.entity.Post;
import com.backend.service.PostService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.backend.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;

    /**
     * 创建帖子
     */
    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody Post post) {
        String postId = postService.createPost(post);
        return ResponseEntity.ok(postId);
    }

    /**
     * 获取帖子列表
     */
    @GetMapping
    public ResponseEntity<List<Post>> getPostList() {
        List<Post> posts = postService.getPostList();
        return ResponseEntity.ok(posts);
    }

    /**
     * 获取帖子详情
     */
    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostDetail(@PathVariable String postId) {
        Post post = postService.getPostDetail(postId);
        return ResponseEntity.ok(post);
    }

    /**
     * 删除帖子
     * @param postId 帖子ID
     * @return 删除结果
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable String postId) {
        try {
        boolean result = postService.deletePost(postId);
            if (result) {
                return ResponseEntity.ok().body("删除成功");
            } else {
                return ResponseEntity.badRequest().body("删除失败：帖子不存在或已被删除");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("删除失败：" + e.getMessage());
        }
    }

    /**
     * 更新帖子
     * @param postId 帖子ID
     * @param post 帖子信息
     * @return 更新结果
     */
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(
            @PathVariable String postId,
            @RequestBody Post post) {
        try {
            // 设置帖子ID
        post.setPostId(postId);
            
            // 更新帖子
        boolean result = postService.updatePost(post);
            if (result) {
                return ResponseEntity.ok().body("更新成功");
            } else {
                return ResponseEntity.badRequest().body("更新失败：帖子不存在或无权限修改");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("更新失败：" + e.getMessage());
        }
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Post>> getMostPopularPosts(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<Post> posts = postService.getMostPopularPosts(limit);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            logger.error("获取最受欢迎帖子失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 
package com.backend.service;

import com.backend.model.entity.Post;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface PostService extends IService<Post> {
    /**
     * 创建帖子
     */
    String createPost(Post post);

    /**
     * 获取帖子列表
     */
    List<Post> getPostList();

    /**
     * 获取帖子详情
     */
    Post getPostDetail(String postId);

    /**
     * 删除帖子
     */
    boolean deletePost(String postId);

    /**
     * 更新帖子
     */
    boolean updatePost(Post post);

    /**
     * 获取最受欢迎的帖子
     * @param limit 返回的帖子数量
     * @return 帖子列表
     */
    List<Post> getMostPopularPosts(int limit);
} 
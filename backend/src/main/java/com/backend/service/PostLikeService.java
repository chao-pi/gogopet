package com.backend.service;

import com.backend.model.entity.PostLike;
import com.baomidou.mybatisplus.extension.service.IService;

public interface PostLikeService extends IService<PostLike> {
    /**
     * 点赞帖子
     */
    boolean likePost(String postId, String userId);

    /**
     * 取消点赞
     */
    boolean unlikePost(String postId, String userId);

    /**
     * 检查用户是否已点赞
     */
    boolean checkUserLiked(String postId, String userId);
} 
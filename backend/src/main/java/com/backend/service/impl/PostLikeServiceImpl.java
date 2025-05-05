package com.backend.service.impl;

import com.backend.mapper.PostLikeMapper;
import com.backend.mapper.PostMapper;
import com.backend.model.entity.PostLike;
import com.backend.service.PostLikeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
public class PostLikeServiceImpl extends ServiceImpl<PostLikeMapper, PostLike> implements PostLikeService {

    @Autowired
    private PostMapper postMapper;

    @Override
    @Transactional
    public boolean likePost(String postId, String userId) {
        // 检查是否已点赞
        if (checkUserLiked(postId, userId)) {
            return false;
        }
        // 创建点赞记录
        PostLike postLike = new PostLike();
        postLike.setLikeId(UUID.randomUUID().toString().replace("-", ""));
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLike.setCreateTime(new Date());
        // 保存点赞记录
        boolean result = save(postLike);
        if (result) {
            // 更新帖子点赞数
            postMapper.updateLikes(postId, 1);
        }
        return result;
    }

    @Override
    @Transactional
    public boolean unlikePost(String postId, String userId) {
        // 删除点赞记录
        QueryWrapper<PostLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId)
                   .eq("user_id", userId);
        boolean result = remove(queryWrapper);
        if (result) {
            // 更新帖子点赞数
            postMapper.updateLikes(postId, -1);
        }
        return result;
    }

    @Override
    public boolean checkUserLiked(String postId, String userId) {
        return baseMapper.checkUserLiked(postId, userId) > 0;
    }
} 
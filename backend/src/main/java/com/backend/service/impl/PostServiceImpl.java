package com.backend.service.impl;

import com.backend.mapper.PostMapper;
import com.backend.mapper.UserMapper;
import com.backend.mapper.CommentMapper;
import com.backend.mapper.PostLikeMapper;
import com.backend.mapper.PostImageMapper;
import com.backend.model.entity.Post;
import com.backend.model.entity.User;
import com.backend.model.entity.Comment;
import com.backend.model.entity.PostLike;
import com.backend.model.entity.PostImage;
import com.backend.service.PostService;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private PostLikeMapper postLikeMapper;

    @Autowired
    private PostImageMapper postImageMapper;

    @Override
    @Transactional
    public String createPost(Post post) {
        // 验证用户ID
        if (post.getUserId() == null || post.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("用户ID不能为空");
        }

        // 生成帖子ID
        post.setPostId(UUID.randomUUID().toString().replace("-", "").substring(0, 18));
        // 设置初始值
        post.setPostLikes(0);
        post.setPostComment(0);
        post.setPostStatus(1);
        post.setCreateTime(new Date());
        post.setUpdateTime(new Date());
        // 保存帖子
        save(post);
        return post.getPostId();
    }

    @Override
    public List<Post> getPostList() {
        logger.debug("开始获取帖子列表");
        try {
            QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("post_status", 1)
                       .orderByDesc("create_time");
            
            List<Post> posts = list(queryWrapper);
            
            // 填充用户信息
            posts.forEach(this::populatePostInfo);
            
            logger.debug("获取帖子列表成功，总记录数：{}", posts.size());
            return posts;
        } catch (Exception e) {
            logger.error("获取帖子列表失败", e);
            throw e;
        }
    }

    @Override
    public Post getPostDetail(String postId) {
        Post post = getById(postId);
        if (post != null) {
            populatePostInfo(post);
        }
        return post;
    }

    /**
     * 填充帖子信息（用户信息）
     */
    private void populatePostInfo(Post post) {
        // 获取用户信息
        User user = userMapper.selectById(post.getUserId());
        if (user != null) {
            post.setUserName(user.getUserName());
            post.setUserAvatar(user.getUserAvatar());
        }
    }

    @Override
    @Transactional
    public boolean deletePost(String postId) {
        logger.debug("开始删除帖子，帖子ID：{}", postId);
        try {
            // 检查帖子是否存在
        Post post = getById(postId);
            if (post == null) {
                logger.warn("删除帖子失败：帖子不存在，ID：{}", postId);
                return false;
            }

            logger.debug("找到帖子：{}", post);

            // 1. 删除关联的评论
            try {
                QueryWrapper<Comment> commentWrapper = new QueryWrapper<>();
                commentWrapper.eq("post_id", postId);
                int commentCount = commentMapper.delete(commentWrapper);
                logger.debug("已删除帖子关联的评论，数量：{}，帖子ID：{}", commentCount, postId);
            } catch (Exception e) {
                logger.error("删除评论时发生异常，帖子ID：{}", postId, e);
                // 继续执行，不中断删除流程
            }

            // 2. 删除关联的点赞
            try {
                QueryWrapper<PostLike> likeWrapper = new QueryWrapper<>();
                likeWrapper.eq("post_id", postId);
                int likeCount = postLikeMapper.delete(likeWrapper);
                logger.debug("已删除帖子关联的点赞，数量：{}，帖子ID：{}", likeCount, postId);
            } catch (Exception e) {
                logger.error("删除点赞时发生异常，帖子ID：{}", postId, e);
                // 继续执行，不中断删除流程
            }

            // 3. 删除关联的图片
            try {
                QueryWrapper<PostImage> imageWrapper = new QueryWrapper<>();
                imageWrapper.eq("post_id", postId);
                int imageCount = postImageMapper.delete(imageWrapper);
                logger.debug("已删除帖子关联的图片，数量：{}，帖子ID：{}", imageCount, postId);
            } catch (Exception e) {
                logger.error("删除图片时发生异常，帖子ID：{}", postId, e);
                // 继续执行，不中断删除流程
            }

            // 4. 最后删除帖子本身
            try {
                boolean result = removeById(postId);
                if (result) {
                    logger.info("删除帖子成功，ID：{}", postId);
                } else {
                    logger.error("删除帖子失败：数据库删除失败，ID：{}", postId);
                }
                return result;
            } catch (Exception e) {
                logger.error("删除帖子时发生异常，ID：{}", postId, e);
                throw new RuntimeException("删除帖子失败：" + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("删除帖子时发生异常，ID：{}", postId, e);
            throw new RuntimeException("删除帖子失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean updatePost(Post post) {
        logger.debug("开始更新帖子，帖子ID：{}", post.getPostId());
        try {
            // 检查帖子是否存在
            Post existingPost = getById(post.getPostId());
            if (existingPost == null) {
                logger.warn("更新帖子失败：帖子不存在，ID：{}", post.getPostId());
                return false;
            }

            // 检查帖子状态
            if (existingPost.getPostStatus() == 0) {
                logger.warn("更新帖子失败：帖子已被删除，ID：{}", post.getPostId());
                return false;
            }

            // 检查是否有权限修改（只有发帖人才能修改）
            if (!existingPost.getUserId().equals(post.getUserId())) {
                logger.warn("更新帖子失败：无权限修改，帖子ID：{}，用户ID：{}", post.getPostId(), post.getUserId());
                return false;
            }

            // 更新帖子信息
        post.setUpdateTime(new Date());
            boolean result = updateById(post);
            
            if (result) {
                logger.info("更新帖子成功，ID：{}", post.getPostId());
            } else {
                logger.error("更新帖子失败：数据库更新失败，ID：{}", post.getPostId());
            }
            
            return result;
        } catch (Exception e) {
            logger.error("更新帖子时发生异常，ID：{}", post.getPostId(), e);
            throw e;
        }
    }

    @Override
    public List<Post> getMostPopularPosts(int limit) {
        logger.debug("获取最受欢迎的帖子，数量限制: {}", limit);
        try {
            // 使用子查询确保获取最新的点赞数
            QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("post_status", 1)
                       .orderByDesc("post_likes")
                       .last("LIMIT " + limit);
            
            List<Post> posts = list(queryWrapper);
            
            // 填充用户信息
            posts.forEach(this::populatePostInfo);
            
            // 记录日志
            logger.debug("获取最受欢迎帖子成功，数量：{}", posts.size());
            posts.forEach(post -> logger.debug("帖子ID：{}，点赞数：{}", post.getPostId(), post.getPostLikes()));
            
            return posts;
        } catch (Exception e) {
            logger.error("获取最受欢迎帖子失败", e);
            throw e;
        }
    }
} 
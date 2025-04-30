package com.backend.mapper;

import com.backend.model.entity.PostLike;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostLikeMapper extends BaseMapper<PostLike> {
    /**
     * 检查用户是否已点赞
     */
    @Select("SELECT COUNT(*) FROM t_post_like WHERE post_id = #{postId} AND user_id = #{userId}")
    int checkUserLiked(@Param("postId") String postId, @Param("userId") String userId);
} 
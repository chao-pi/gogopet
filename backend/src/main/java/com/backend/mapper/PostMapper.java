package com.backend.mapper;

import com.backend.model.entity.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
    /**
     * 更新帖子点赞数
     */
    @Update("UPDATE t_post SET post_likes = post_likes + #{increment} WHERE post_id = #{postId}")
    int updateLikes(@Param("postId") String postId, @Param("increment") int increment);

    /**
     * 更新帖子评论数
     */
    @Update("UPDATE t_post SET post_comment = post_comment + #{increment} WHERE post_id = #{postId}")
    int updateComments(@Param("postId") String postId, @Param("increment") int increment);
} 
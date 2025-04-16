package com.backend.mapper;

import com.backend.model.t_post_model;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface t_post_mapper {

    // 查询 t_chat 表中所有数据
    @Select("select * " +
            "from petgogo.t_post")
    List<t_post_model> t_post_mapper_selectAll();

    // 查询表中共有多少条数据
    @Select("select count(*) " +
            "from petgogo.t_post")
    int t_post_mapper_countAll();

    // 根据 post_id 查询 t_post 表中的数据
    @Select("select * " +
            "from petgogo.t_post " +
            "where post_id = #{post_id}")
    List<t_post_model> t_post_mapper_selectByPostId(
            @Param("post_id") String post_id
    );

    // 根据 user_id 查询 t_post 表中的数据
    @Select("select * " +
            "from petgogo.t_post " +
            "where user_id = #{user_id}")
    List<t_post_model> t_post_mapper_selectByUserId(
            @Param("user_id") String user_id
    );

    // 根据 post_content 片段内容查询 t_post 表中的数据
    @Select("select * " +
            "from petgogo.t_post " +
            "where post_content " +
            "like concat('%', #{post_content}, '%')")
    List<t_post_model> t_post_mapper_selectByPostContent(
            @Param("post_content") String post_content
    );

    // 根据 post_comment 片段内容查询 t_post 表中的数据
    @Select("select * " +
            "from petgogo.t_post " +
            "where post_comment " +
            "like concat('%', #{post_comment}, '%')")
    List<t_post_model> t_post_mapper_selectByPostComment(
            @Param("post_comment") String post_comment
    );

    // 向 t_post 表中插入数据
    @Insert("insert into petgogo.t_post (post_id, user_id, post_content, post_comment) " +
            "values (#{post_id}, #{user_id}, #{post_content}, #{post_comment})")
    int t_post_mapper_insert(
            t_post_model post
    );

    // 根据 post_id 删除 t_post 表中的数据
    @Delete("delete from petgogo.t_post " +
            "where post_id = #{post_id}")
    int t_post_mapper_deleteByPostId(
            @Param("post_id") String post_id
    );

    // 根据 post_id 更新 t_post 表中的 post_content
    @Update("update petgogo.t_post " +
            "set post_content = #{post_content} " +
            "where post_id = #{post_id}")
    int t_post_mapper_updatePostContent(
            @Param("post_id") String post_id,
            @Param("post_content") String post_content
    );

    // 根据 post_id 更新 t_post 表中的 post_content
    @Update("update petgogo.t_post " +
            "set post_comment = #{post_comment} " +
            "where post_id = #{post_id}")
    int t_post_mapper_updatePostComment(
            @Param("post_id") String post_id,
            @Param("post_content") String post_content
    );
}

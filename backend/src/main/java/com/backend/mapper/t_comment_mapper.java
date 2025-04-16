package com.backend.mapper;

import com.backend.model.t_comment_model;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface t_comment_mapper {

    // 查询 t_comment 表中所有数据
    @Select("select * " +
            "from petgogo.t_comment")
    List<t_comment_model> t_comment_mapper_selectAll();

    // 查询 t_comment 表中共有多少条数据
    @Select("select count(*) " +
            "from petgogo.t_comment")
    int t_comment_mapper_countAll();

    // 根据 comment_id 查询 t_comment 表中的数据
    @Select("select * " +
            "from petgogo.t_comment " +
            "where comment_id = #{comment_id}")
    List<t_comment_model> t_comment_mapper_selectByCommentID(
            @Param("comment_id") String comment_id
    );

    // 根据 company_id 查询 t_comment 表中的数据
    @Select("select * " +
            "from petgogo.t_comment " +
            "where company_id = #{company_id}")
    List<t_comment_model> t_comment_mapper_selectByCompanyID(
            @Param("company_id") String company_id
    );

    // 根据 user_id 查询 t_comment 表中的数据
    @Select("select * " +
            "from petgogo.t_comment " +
            "where user_id = #{user_id}")
    List<t_comment_model> t_comment_mapper_selectByUserID(
            @Param("user_id") String user_id
    );

    // 根据 comment_content 片段内容查询 t_comment 表中的数据
    @Select("select * " +
            "from petgogo.t_comment " +
            "where comment_content " +
            "like concat('%', #{comment_content}, '%')")
    List<t_comment_model> t_comment_mapper_selectByCommentContent(
            @Param("comment_content") String comment_content
    );

    // 根据 comment_id 删除 t_comment 表中的数据
    @Delete("delete from petgogo.t_comment " +
            "where comment_id = #{comment_id}")
    int t_comment_mapper_deleteByCommentID(
            @Param("comment_id") String comment_id
    );

    // 根据 company_id 删除 t_comment 表中的数据
    @Delete("delete from petgogo.t_comment " +
            "where company_id = #{company_id}")
    int t_comment_mapper_deleteByCompanyID(
            @Param("company_id") String company_id
    );

    // 根据 user_id 删除 t_comment 表中的数据
    @Delete("delete from petgogo.t_comment " +
            "where user_id = #{user_id}")
    int t_comment_mapper_deleteByUserID(
            @Param("user_id") String user_id
    );

    // 根据 comment_id 更新 t_comment 表中的 comment_content 数据
    @Update("update petgogo.t_comment " +
            "set comment_content = #{comment_content} " +
            "where comment_id = #{comment_id}")
    int t_comment_mapper_updateCommentContent(
            @Param("comment_content") String comment_content,
            @Param("comment_id") String comment_id
    );

    // 向 t_comment 表中插入数据
    @Insert("insert into petgogo.t_comment (comment_id, company_id, user_id, comment_content) " +
            "values (#{comment_id}, #{company_id}, #{user_id}, #{comment_content})")
    int t_comment_mapper_insert(
            t_comment_model t_comment_model
    );
}

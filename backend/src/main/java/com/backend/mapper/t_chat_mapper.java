package com.backend.mapper;

import com.backend.model.t_chat_model;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface t_chat_mapper {

    // 查询表中共有多少条数据
    @Select("select count(*) " +
            "from petgogo.t_chat")
    int t_chat_mapper_countAll();

    // 查询 t_chat 表中所有数据
    @Select("select * " +
            "from petgogo.t_chat")
    List<t_chat_model> t_chat_mapper_selectAll();

    // 根据 chat_id 查询 t_chat 表中的数据
    @Select("select * " +
            "from petgogo.t_chat " +
            "where chat_id = #{chat_id}")
    List<t_chat_model> t_chat_mapper_selectByChatID(
            @Param("chat_id") String chat_id
    );

    // 根据 user_id 查询 t_chat 表中的数据
    @Select("select * " +
            "from petgogo.t_chat " +
            "where user_id = #{user_id}")
    List<t_chat_model> t_chat_mapper_selectByUserID(
            @Param("user_id") String user_id
    );

    // 根据 chat_content 片段内容查询 t_chat 表中的数据
    @Select("select * " +
            "from petgogo.t_chat " +
            "where chat_content " +
            "like concat('%', #{chat_content}, '%')")
    List<t_chat_model> t_chat_mapper_selectByChatContent(
            @Param("chat_content") String chat_content
    );

    // 根据 chat_id 删除 t_chat 表中的数据
    @Delete("delete " +
            "from petgogo.t_chat " +
            "where chat_id = #{chat_id}")
    int t_chat_mapper_deleteByChatID(
            @Param("chat_id") String chat_id
    );

    // 根据 user_id 删除 t_chat 表中的数据
    @Delete("delete " +
            "from petgogo.t_chat " +
            "where user_id = #{user_id}")
    int t_chat_mapper_deleteByUserID(
            @Param("user_id") String user_id
    );

    // 根据 chat_id 更新 t_chat 表中的 chat_content
    @Update("update petgogo.t_chat " +
            "set chat_content = #{chat_content} " +
            "where chat_id = #{chat_id}")
    int t_chat_mapper_updateChatContent(
            @Param("chat_content") String chat_content,
            @Param("chat_id") String chat_id);

    // 向 t_chat 表中插入数据
    @Insert("insert into petgogo.t_chat (chat_id, user_id, chat_content) " +
            "values (#{chat_id}, #{user_id}, #{chat_content})")
    int t_chat_mapper_insert(
            t_chat_model t_chat_model
    );

}

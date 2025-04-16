package com.backend.mapper;

import com.backend.model.t_picture_model;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface t_picture_mapper {

    // 查询表中共有多少条数据
    @Select("select * " +
            "from petgogo.t_picture")
    List<t_picture_model> t_picture_mapper_selectAll();

    // 查询 t_picture 表中所有数据
    @Select("select count(*) " +
            "from petgogo.t_picture")
    int t_picture_mapper_countAll();

    // 根据 picture_id 查询 t_picture 表中的数据
    @Select("select * " +
            "from petgogo.t_picture " +
            "where picture_id = #{picture_id}")
    List<t_picture_model> t_picture_mapper_selectByPictureId(
            @Param("picture_id") String picture_id
    );

    // 根据 picture_usage 查询 t_picture 表中的数据
    @Select("select * " +
            "from petgogo.t_picture " +
            "where picture_usage = #{picture_usage}")
    List<t_picture_model> t_picture_mapper_selectByPictureUsage(
            @Param("picture_usage") char picture_usage
    );

    // 根据 picture_id 删除 t_picture 表中的数据
    @Delete("delete from petgogo.t_picture " +
            "where picture_id = #{picture_id}")
    int t_picture_mapper_deleteByPictureId(
            @Param("picture_id") String picture_id
    );

    // 向 t_picture 表中插入数据
    @Insert("insert into petgogo.t_picture (picture_id, picture_usage) " +
            "values (#{picture_id}, #{picture_usage})")
    int t_picture_mapper_insert(
            t_picture_model t_picture_model
    );
}

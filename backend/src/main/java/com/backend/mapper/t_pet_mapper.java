package com.backend.mapper;

import com.backend.model.t_pet_model;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface t_pet_mapper {

    // 查询表中共有多少条数据
    @Select("select * " +
            "from petgogo.t_pet")
    List<t_pet_model> t_pet_mapper_selectAll();

    // 查询 t_pet 表中所有数据
    @Select("select count(*) " +
            "from petgogo.t_pet")
    int t_pet_mapper_countAll();

    // 根据 pet_id 查询 t_pet 表中的数据
    @Select("select * " +
            "from petgogo.t_pet " +
            "where pet_id = #{pet_id}")
    List<t_pet_model> t_pet_mapper_selectByPetID(
            @Param("pet_id") String pet_id
    );

    // 根据 user_id 查询 t_pet 表中的数据
    @Select("select * " +
            "from petgogo.t_pet " +
            "where user_id = #{user_id}")
    List<t_pet_model> t_pet_mapper_selectByUserID(
            @Param("user_id") String user_id
    );

    // 根据 pet_name 查询 t_pet 表中的数据
    @Select("select * " +
            "from petgogo.t_pet " +
            "where pet_name = #{pet_name}")
    List<t_pet_model> t_pet_mapper_selectByPetName(
            @Param("pet_name") String pet_name
    );

    // 根据 pet_breed 查询 t_pet 表中的数据
    @Select("select * " +
            "from petgogo.t_pet " +
            "where pet_breed = #{pet_breed}")
    List<t_pet_model> t_pet_mapper_selectByPetBreed(
            @Param("pet_breed") String pet_breed
    );

    // 根据 pet_weight 查询 t_pet 表中的数据
    @Select("select * " +
            "from petgogo.t_pet " +
            "where pet_weight = #{pet_weight}")
    List<t_pet_model> t_pet_mapper_selectByPetWeight(
            @Param("pet_weight") String pet_weight
    );

    // 根据 pet_health_status 查询 t_pet 表中的数据
    @Select("select * " +
            "from petgogo.t_pet" +
            " where pet_health_status = #{pet_health_status}")
    List<t_pet_model> t_pet_mapper_selectByPetHealthStatus(
            @Param("pet_health_status") char pet_health_status
    );

    // 根据 pet_id 删除 t_pet 表中的数据
    @Delete("delete from petgogo.t_pet " +
            "where pet_id = #{pet_id}")
    int t_pet_mapper_deleteByPetID(
            @Param("pet_id") String pet_id
    );

    // 根据 pet_id 更新 t_pet 表中的 pet_name
    @Update("update petgogo.t_pet " +
            "set pet_name = #{pet_name} " +
            "where pet_id = #{pet_id}")
    int t_pet_mapper_updatePetName(
            @Param("pet_id") String pet_id,
            @Param("pet_name") String pet_name
    );

    // 根据 pet_id 更新 t_pet 表中的 pet_breed
    @Update("update petgogo.t_pet " +
            "set pet_breed = #{pet_breed} " +
            "where pet_id = #{pet_id}")
    int t_pet_mapper_updatePetBreed(
            @Param("pet_id") String pet_id,
            @Param("pet_breed") String pet_breed
    );

    // 根据 pet_id 更新 t_pet 表中的 pet_weight
    @Update("update petgogo.t_pet " +
            "set pet_weight = #{pet_weight} " +
            "where pet_id = #{pet_id}")
    int t_pet_mapper_updatePetWeight(
            @Param("pet_id") String pet_id,
            @Param("pet_weight") String pet_weight
    );

    // 根据 pet_id 更新 t_pet 表中的 pet_health_status
    @Update("update petgogo.t_pet " +
            "set pet_health_status = #{pet_health_status} " +
            "where pet_id = #{pet_id}")
    int t_pet_mapper_updatePetHealthStatus(
            @Param("pet_id") String pet_id,
            @Param("pet_health_status") String pet_health_status
    );

    // 向 t_pet 表中插入数据
    @Insert("insert into petgogo.t_pet (pet_id, user_id, pet_name, pet_breed, pet_weight, pet_health_status) " +
            "values (#{pet_id}, #{user_id}, #{pet_name}, #{pet_breed}, #{pet_weight}, #{pet_health_status})")
    int t_pet_mapper_insert(
            t_pet_model t_pet_model
    );
}

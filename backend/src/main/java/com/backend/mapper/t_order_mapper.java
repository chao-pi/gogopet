package com.backend.mapper;

import com.backend.model.t_order_model;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface t_order_mapper {

    // 查询 t_order 表中所有数据
    @Select("select * " +
            "from petgogo.t_order")
    List<t_order_model> t_order_mapper_selectAll();

    // 查询 t_order 表中共有多少条数据
    @Select("select count(*) " +
            "from petgogo.t_order")
    int t_order_mapper_countAll();

    // 根据 order_id 查询 t_order 表中的数据
    @Select("select * " +
            "from petgogo.t_order " +
            "where order_id = #{order_id}")
    List<t_order_model> t_order_mapper_selectByOrderID(
            @Param("order_id") String order_id
    );

    // 根据 pet_id 查询 t_order 表中的数据
    @Select("select * " +
            "from petgogo.t_order " +
            "where pet_id = #{pet_id}")
    List<t_order_model> t_order_mapper_selectByPetID(
            @Param("pet_id") String pet_id
    );

    // 根据 picture_id 查询 t_order 表中的数据
    @Select("select * " +
            "from petgogo.t_order " +
            "where picture_id = #{picture_id}")
    List<t_order_model> t_order_mapper_selectByPictureID(
            @Param("picture_id") String picture_id
    );

    // 根据 order_status 查询 t_order 表中的数据
    @Select("select * " +
            "from petgogo.t_order " +
            "where order_status = #{order_status}")
    List<t_order_model> t_order_mapper_selectByOrderStatus(
            @Param("order_status") char order_status
    );

    // 根据 pet_status 查询 t_order 表中的数据
    @Select("select * " +
            "from petgogo.t_order " +
            "where pet_status = #{pet_status}")
    List<t_order_model> t_order_mapper_selectByPetStatus(
            @Param("pet_status") char pet_status
    );

    // 根据 delivery_status 查询 t_order 表中的数据
    @Select("select * " +
            "from petgogo.t_order " +
            "where delivery_status = #{delivery_status}")
    List<t_order_model> t_order_mapper_selectByDeliveryStatus(
            @Param("delivery_status") char delivery_status
    );

    // 根据 order_id 删除 t_order 表中的数据
    @Delete("delete from petgogo.t_order " +
            "where order_id = #{order_id}")
    int t_order_mapper_deleteByOrderID(
            @Param("order_id") String order_id
    );

    // 根据 order_id 更新 t_order 表中的 picture_id
    @Update("update petgogo.t_order " +
            "set picture_id = #{picture_id} " +
            "where order_id = #{order_id}")
    int t_order_mapper_updatePictureID(
            @Param("order_id") String order_id,
            @Param("picture_id") String picture_id
    );

    // 根据 order_id 更新 t_order 表中的 order_status
    @Update("update petgogo.t_order " +
            "set order_status = #{order_status} " +
            "where order_id = #{order_id}")
    int t_order_mapper_updateOrderStatus(
            @Param("order_id") String order_id,
            @Param("order_status") char order_status
    );

    // 根据 order_id 更新 t_order 表中的 pet_status
    @Update("update petgogo.t_order " +
            "set pet_status = #{pet_status} " +
            "where order_id = #{order_id}")
    int t_order_mapper_updatePetStatus(
            @Param("order_id") String order_id,
            @Param("pet_status") char pet_status
    );

    // 根据 order_id 更新 t_order 表中的 delivery_status
    @Update("update petgogo.t_order " +
            "set delivery_status = #{delivery_status} " +
            "where order_id = #{order_id}")
    int t_order_mapper_updateDeliveryStatus(
            @Param("order_id") String order_id,
            @Param("delivery_status") char delivery_status
    );

    // 向 t_order 表中插入数据
    @Insert("insert into petgogo.t_order (order_id, pet_id, picture_id, order_status, pet_status, delivery_status) " +
            "values (#{order_id}, #{pet_id}, #{picture_id}, #{order_status}, #{pet_status}, #{delivery_status})")
    int t_order_mapper_insert(
            t_order_model t_order_model
    );
}

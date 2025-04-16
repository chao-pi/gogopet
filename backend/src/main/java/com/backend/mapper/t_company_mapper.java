package com.backend.mapper;

import com.backend.model.t_company_model;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface t_company_mapper {

    // 查询 t_company 表中所有数据
    @Select("select * " +
            "from petgogo.t_company")
    List<t_company_model> t_company_mapper_selectAll();

    // 查询表中共有多少条数据
    @Select("select count(*) " +
            "from petgogo.t_company")
    int t_company_mapper_countAll();

    // 根据 company_id 查询 t_company 表中的数据
    @Select("select * " +
            "from petgogo.t_company " +
            "where company_id = #{company_id}")
    List<t_company_model> t_company_mapper_selectByCompanyID(
            @Param("company_id") String company_id
    );

    // 根据 company_intro 片段内容查询 t_company 表中的数据
    @Select("select * " +
            "from petgogo.t_company " +
            "where company_intro " +
            "like concat('%', #{company_intro}, '%')")
    List<t_company_model> t_company_mapper_selectByCompanyIntro(
            @Param("company_intro") String company_intro
    );

    // 根据 company_local 片段内容查询 t_company 表中的数据
    @Select("select * " +
            "from petgogo.t_company " +
            "where company_local " +
            "like concat('%', #{company_local}, '%')")
    List<t_company_model> t_company_mapper_selectByCompanyLocal(
            @Param("company_local") String company_local
    );

    // 根据 company_id 删除 t_company 表中的数据
    @Delete("delete from petgogo.t_company " +
            "where company_id = #{company_id}")
    int t_company_mapper_deleteByCompanyID(
            @Param("company_id") String company_id
    );

    // 根据 company_id 更新 company_intro 字段
    @Update("update petgogo.t_company " +
            "set company_intro = #{company_intro} " +
            "where company_id = #{company_id}")
    int t_company_mapper_updateCompanyIntro(
            @Param("company_id") String company_id,
            @Param("company_intro") String company_intro
    );

    // 根据 company_id 更新 company_local 字段
    @Update("update petgogo.t_company " +
            "set company_local = #{company_local} " +
            "where company_id = #{company_id}")
    int t_company_mapper_updateCompanyLocal(
            @Param("company_id") String company_id,
            @Param("company_local") String company_local
    );

    // 向 t_company 表中插入数据
    @Insert("insert into petgogo.t_company (company_id, company_intro, company_local) " +
            "values (#{company_id}, #{company_intro}, #{company_local})")
    int t_company_mapper_insert(
            t_company_model t_company_model
    );
}

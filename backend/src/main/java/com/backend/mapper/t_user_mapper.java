package com.backend.mapper;

import com.backend.model.t_user_model;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface t_user_mapper {

    @Select("select * from petgogo.t_user where user_id = #{user_id}")
    List<t_user_model> t_user_mapper_selectByUserId(String user_id);

    @Select("select * from petgogo.t_user where user_name like concat('%'+user_name+'%')")
    List<t_user_model> t_user_mapper_selectByUserName(String user_name);

    @Select("select * from petgogo.t_user where password = #{password}")
    List<t_user_model> t_user_mapper_selectByPassword(String password);

    @Select("select * from petgogo.t_user where user_type = #{user_type}")
    List<t_user_model> t_user_mapper_selectByUserType(char user_type);

    @Select("select * from petgogo.t_user where user_address like concat('%'+user_address+'%')")
    List<t_user_model> t_user_mapper_selectByUserAddress(String user_address);

    @Select("select * from petgogo.t_user where picture_id = #{picture_id}")
    List<t_user_model> t_user_mapper_selectByPictureId(String picture_id);

    @Select("select * from petgogo.t_user where company_id = #{company_id}")
    List<t_user_model> t_user_mapper_selectByCompanyId(String company_id);


}

package com.backend.mapper;

import com.backend.model.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM gogopet.t_user WHERE user_name = #{userName}")
    User selectByUserName(@Param("userName") String userName);

    @Select("SELECT * FROM gogopet.t_user WHERE user_id = #{userId}")
    User selectById(@Param("userId") String userId);

    @Insert("INSERT INTO gogopet.t_user (user_id, user_name, password, user_type, user_address) " +
            "VALUES (#{userId}, #{userName}, #{password}, #{userType}, #{userAddress})")
    int insert(User user);

    @Update("UPDATE gogopet.t_user SET user_name = #{userName}, user_type = #{userType}, " +
            "user_address = #{userAddress}, picture_id = #{pictureId}, company_id = #{companyId} " +
            "WHERE user_id = #{userId}")
    int update(User user);

    @Delete("DELETE FROM gogopet.t_user WHERE user_id = #{userId}")
    int deleteById(@Param("userId") String userId);
} 
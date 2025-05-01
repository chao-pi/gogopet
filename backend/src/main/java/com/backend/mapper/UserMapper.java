package com.backend.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.backend.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据用户名查询用户
     * @param userName 用户名
     * @return 用户信息
     */
    @Select("SELECT u.*, p.picture_url as avatar_url FROM t_user u LEFT JOIN t_picture p ON u.picture_id = p.picture_id WHERE u.user_name = #{userName}")
    User selectByUserName(String userName);

    /**
     * 根据用户ID查询用户
     * @param userId 用户ID
     * @return 用户信息
     */
    @Select("SELECT u.*, p.picture_url as avatar_url FROM t_user u LEFT JOIN t_picture p ON u.picture_id = p.picture_id WHERE u.user_id = #{userId}")
    User selectById(String userId);

    @Insert("INSERT INTO gogopet.t_user (user_id, user_name, password, user_type, user_address, company_id) " +
            "VALUES (#{userId}, #{userName}, #{password}, #{userType}, #{userAddress}, #{companyId})")
    int insert(User user);

    @Update("UPDATE gogopet.t_user SET user_name = #{userName}, password = #{password}, user_type = #{userType}, " +
            "user_address = #{userAddress}, picture_id = #{pictureId}, company_id = #{companyId} " +
            "WHERE user_id = #{userId}")
    int update(User user);

    @Delete("DELETE FROM gogopet.t_user WHERE user_id = #{userId}")
    int deleteById(@Param("userId") String userId);

    /**
     * 根据公司ID查询用户
     * @param companyId 公司ID
     * @return 用户信息
     */
    @Select("SELECT * FROM t_user WHERE company_id = #{companyId}")
    User selectByCompanyId(String companyId);
} 
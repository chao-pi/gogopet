package com.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.backend.model.entity.Picture;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PictureMapper extends BaseMapper<Picture> {
    @Insert("INSERT INTO gogopet.t_picture (picture_id, picture_usage, picture_url, user_id, pet_id, upload_time) " +
            "VALUES (#{pictureId}, #{pictureUsage}, #{pictureUrl}, #{userId}, #{petId}, #{uploadTime})")
    int insert(Picture picture);

    @Select("SELECT * FROM gogopet.t_picture WHERE picture_id = #{pictureId}")
    Picture selectById(String pictureId);

    @Select("SELECT * FROM gogopet.t_picture WHERE pet_id = #{petId} AND picture_usage = 'P'")
    Picture selectByPetId(String petId);

    @Update("UPDATE gogopet.t_picture SET picture_usage = #{pictureUsage}, picture_url = #{pictureUrl}, " +
            "user_id = #{userId}, pet_id = #{petId}, upload_time = #{uploadTime} WHERE picture_id = #{pictureId}")
    int update(Picture picture);

    @Delete("DELETE FROM t_picture WHERE pet_id = #{petId}")
    int deleteByPetId(@Param("petId") String petId);
} 
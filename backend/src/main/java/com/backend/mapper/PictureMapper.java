package com.backend.mapper;

import com.backend.model.entity.Picture;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PictureMapper {
    @Insert("INSERT INTO gogopet.t_picture (picture_id, picture_usage, picture_url, user_id) " +
            "VALUES (#{pictureId}, #{pictureUsage}, #{pictureUrl}, #{userId})")
    int insert(Picture picture);

    @Select("SELECT * FROM gogopet.t_picture WHERE picture_id = #{pictureId}")
    Picture selectById(String pictureId);
} 
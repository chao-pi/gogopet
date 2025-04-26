package com.backend.mapper;

import com.backend.model.entity.Picture;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PictureMapper {
    @Insert("INSERT INTO gogopet.t_picture (picture_id, picture_usage, picture_url, user_id, upload_time) " +
            "VALUES (#{pictureId}, #{pictureUsage}, #{pictureUrl}, #{userId}, #{uploadTime})")
    int insert(Picture picture);

    @Select("SELECT * FROM gogopet.t_picture WHERE picture_id = #{pictureId}")
    Picture selectById(String pictureId);

    @Update("UPDATE gogopet.t_picture SET picture_usage = #{pictureUsage}, picture_url = #{pictureUrl}, " +
            "user_id = #{userId}, upload_time = #{uploadTime} WHERE picture_id = #{pictureId}")
    int update(Picture picture);
} 
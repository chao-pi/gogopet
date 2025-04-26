package com.backend.mapper;

import com.backend.model.entity.Pet;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PetMapper {
    @Insert("INSERT INTO gogopet.t_pet (pet_id, user_id, pet_name, pet_breed, pet_weight, pet_health_status, pet_age, pet_gender, create_time, update_time) " +
            "VALUES (#{petId}, #{userId}, #{petName}, #{petBreed}, #{petWeight}, #{petHealthStatus}, #{petAge}, #{petGender}, #{createTime}, #{updateTime})")
    int insert(Pet pet);

    @Select("SELECT * FROM gogopet.t_pet WHERE pet_id = #{petId}")
    Pet selectById(String petId);

    @Select("SELECT * FROM gogopet.t_pet WHERE user_id = #{userId}")
    List<Pet> selectByUserId(String userId);

    @Update("UPDATE gogopet.t_pet SET pet_name = #{petName}, pet_breed = #{petBreed}, pet_weight = #{petWeight}, " +
            "pet_health_status = #{petHealthStatus}, pet_age = #{petAge}, pet_gender = #{petGender}, update_time = #{updateTime} WHERE pet_id = #{petId}")
    int update(Pet pet);

    @Delete("DELETE FROM gogopet.t_pet WHERE pet_id = #{petId}")
    int deleteById(String petId);
} 
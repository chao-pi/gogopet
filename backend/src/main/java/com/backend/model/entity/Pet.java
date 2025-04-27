package com.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 宠物实体类
 * 对应数据库表 t_pet
 */
@Data
@TableName("t_pet")
public class Pet {
    /**
     * 宠物ID，唯一标识符
     */
    @TableId("pet_id")
    private String petId;

    /**
     * 用户ID，关联宠物主人
     */
    @TableField("user_id")
    private String userId;

    /**
     * 宠物名称
     */
    @TableField("pet_name")
    private String petName;

    /**
     * 宠物品种
     */
    @TableField("pet_breed")
    private String petBreed;

    /**
     * 宠物体重，单位：千克
     */
    @TableField("pet_weight")
    private BigDecimal petWeight;

    /**
     * 宠物健康状况描述
     */
    @TableField("pet_health_status")
    private String petHealthStatus;

    /**
     * 宠物年龄，单位：岁
     */
    @TableField("pet_age")
    private Integer petAge;

    /**
     * 宠物性别：M-公，F-母
     */
    @TableField("pet_gender")
    private String petGender;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
} 
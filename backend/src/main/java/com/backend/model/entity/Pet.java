package com.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
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
    @TableId(type = IdType.ASSIGN_ID)
    @Size(max = 18, message = "宠物ID长度不能超过18个字符")
    private String petId;

    /**
     * 用户ID，关联宠物主人
     */
    @TableField("user_id")
    @Size(max = 18, message = "用户ID长度不能超过18个字符")
    private String userId;

    /**
     * 宠物名称
     */
    @TableField("pet_name")
    @Size(max = 63, message = "宠物名称长度不能超过63个字符")
    private String petName;

    /**
     * 宠物品种
     */
    @TableField("pet_breed")
    @Size(max = 63, message = "宠物品种长度不能超过63个字符")
    private String petBreed;

    /**
     * 宠物体重，单位：千克
     */
    @TableField("pet_weight")
    @NotNull(message = "宠物体重不能为空")
    @DecimalMin(value = "0.0", inclusive = false, message = "宠物体重必须大于0")
    private BigDecimal petWeight;

    /**
     * 宠物健康状况描述
     */
    @TableField("pet_health_status")
    @Size(max = 255, message = "宠物健康状况描述长度不能超过255个字符")
    private String petHealthStatus;

    /**
     * 宠物年龄，单位：岁
     */
    @TableField("pet_age")
    @NotNull(message = "宠物年龄不能为空")
    @Min(value = 0, message = "宠物年龄不能小于0")
    private Integer petAge;

    /**
     * 宠物性别：M-公，F-母
     */
    @TableField("pet_gender")
    @NotNull(message = "宠物性别不能为空")
    @Size(min = 1, max = 1, message = "宠物性别长度必须为1个字符")
    private String petGender;

    /**
     * 宠物图片URL
     */
    @TableField("pet_image")
    private String petImage;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
} 
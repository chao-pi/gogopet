package com.backend.model.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 宠物数据传输对象
 */
@Data
public class PetDTO {
    /**
     * 宠物ID，唯一标识符
     */
    @Size(min = 18, max = 18, message = "宠物ID长度必须为18个字符")
    private String petId;

    /**
     * 用户ID，关联宠物主人
     */
    @Size(min = 18, max = 18, message = "用户ID长度必须为18个字符")
    private String userId;

    /**
     * 宠物名称
     */
    @Size(max = 63, message = "宠物名称长度不能超过63个字符")
    private String petName;

    /**
     * 宠物品种
     */
    @Size(max = 63, message = "宠物品种长度不能超过63个字符")
    private String petBreed;

    /**
     * 宠物体重，单位：千克
     */
    @NotNull(message = "宠物体重不能为空")
    @DecimalMin(value = "0.01", message = "宠物体重必须大于0")
    private BigDecimal petWeight;

    /**
     * 宠物健康状态描述
     */
    @Size(max = 255, message = "健康状态描述长度不能超过255个字符")
    private String petHealthStatus;

    /**
     * 宠物年龄，单位：岁
     */
    @NotNull(message = "宠物年龄不能为空")
    @DecimalMin(value = "0", message = "宠物年龄不能小于0")
    private Integer petAge;

    /**
     * 宠物性别：M-公，F-母
     */
    @NotNull(message = "宠物性别不能为空")
    @Size(max = 1, message = "宠物性别长度必须为1个字符")
    private String petGender;

    /**
     * 宠物头像URL
     */
    private String avatarUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 宠物描述
     */
    @Size(max = 500, message = "宠物描述长度不能超过500个字符")
    private String petDescription;
} 
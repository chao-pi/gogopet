package com.backend.model.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import java.math.BigDecimal;

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
} 
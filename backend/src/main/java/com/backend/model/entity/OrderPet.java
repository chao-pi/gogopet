package com.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 订单宠物关联实体类
 * 对应数据库表 t_order_pet
 */
@Data
@TableName("t_order_pet")
public class OrderPet {
    /**
     * 订单宠物关联ID，唯一标识符
     */
    @TableId("order_pet_id")
    private String orderPetId;

    /**
     * 订单ID，关联订单表
     */
    @TableField("order_id")
    private String orderId;

    /**
     * 宠物ID，关联宠物表
     */
    @TableField("pet_id")
    private String petId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
} 
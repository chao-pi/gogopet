package com.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单宠物关联实体类
 */
@Data
@TableName("t_order_pet")
public class OrderPet {
    
    /**
     * 订单宠物关联ID
     */
    @TableId("order_pet_id")
    private String orderPetId;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 宠物ID
     */
    private String petId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 
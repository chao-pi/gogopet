package com.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 * 对应数据库表 t_order
 */
@Data
@TableName("`order`")
public class Order {
    /**
     * 订单ID，唯一标识符
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID，关联用户表，表示下单用户
     */
    private Long userId;

    /**
     * 宠物ID，关联宠物表，表示下单的宠物
     */
    private Long petId;

    /**
     * 订单金额，单位：元
     */
    private BigDecimal amount;

    /**
     * 订单状态
     * 0-待支付
     * 1-已支付
     * 2-已发货
     * 3-已完成
     * 4-已取消
     */
    private Integer status;

    /**
     * 订单地址
     */
    private String address;

    /**
     * 用户电话
     */
    private String phone;

    /**
     * 收货人
     */
    private String receiver;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 
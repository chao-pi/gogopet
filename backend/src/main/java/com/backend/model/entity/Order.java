package com.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 订单实体类
 * 对应数据库表 t_order
 */
@Data
@TableName("t_order")
public class Order {
    /**
     * 订单ID，唯一标识符
     */
    @TableId("order_id")
    private String orderId;

    /**
     * 宠物ID，关联托运的宠物
     */
    @TableField("pet_id")
    private String petId;

    /**
     * 用户ID，关联订单创建者
     */
    @TableField("user_id")
    private String userId;

    /**
     * 公司ID，关联承运公司
     */
    @TableField("company_id")
    private String companyId;

    /**
     * 订单状态
     * P-待支付
     * W-待接单
     * T-运输中
     * C-已完成
     * X-已取消
     */
    @TableField("order_status")
    private String orderStatus;

    /**
     * 宠物状态
     * N-正常
     * A-异常
     */
    @TableField("pet_status")
    private String petStatus;

    /**
     * 运输状态
     * P-待接单
     * T-运输中
     * D-已送达
     */
    @TableField("delivery_status")
    private String deliveryStatus;

    /**
     * 托运开始时间
     */
    @TableField("start_time")
    private Date startTime;

    /**
     * 托运结束时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 起始地点
     */
    @TableField("start_location")
    private String startLocation;

    /**
     * 目的地
     */
    @TableField("end_location")
    private String endLocation;

    /**
     * 订单价格，单位：元
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 支付状态
     * U-未支付
     * P-已支付
     * R-已退款
     */
    @TableField("payment_status")
    private String paymentStatus;

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
package com.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.util.Date;

/**
 * 订单追踪实体类
 * 对应数据库表 t_order_tracking
 */
@Data
@TableName("t_order_tracking")
public class OrderTracking {
    /**
     * 追踪ID，唯一标识符
     */
    @TableId("tracking_id")
    private String trackingId;

    /**
     * 订单ID，关联订单
     */
    @TableField("order_id")
    private String orderId;

    /**
     * 当前位置
     */
    @TableField("location")
    private String location;

    /**
     * 状态
     * T-运输中
     * R-休息中
     */
    @TableField("status")
    private String status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
} 
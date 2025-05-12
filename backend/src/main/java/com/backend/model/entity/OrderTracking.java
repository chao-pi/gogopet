package com.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单追踪实体类
 * 对应数据库表 t_order_tracking
 */
@Setter
@Getter
@Data
@TableName("t_order_tracking")
public class OrderTracking {
    /**
     * 追踪ID，唯一标识符
     */
    @TableId("tracking_id")
    private String trackingId;

    /**
     * 订单ID，关联订单表
     */
    @TableField("order_id")
    private String orderId;

    /**
     * 当前位置
     */
    @TableField("location")
    private String location;

    /**
     * 纬度
     */
    @TableField("latitude")
    private BigDecimal latitude;

    /**
     * 经度
     */
    @TableField("longitude")
    private BigDecimal longitude;

    /**
     * 状态
     * T-运输中
     * R-休息中
     * L-装卸中
     * D-已送达
     */
    @TableField("status")
    private String status;

    /**
     * 备注信息，如异常情况说明
     */
    @TableField("remark")
    private String remark;

    /**
     * 预计到达时间，仅供参考
     */
    @TableField("estimated_arrival_time")
    private LocalDateTime estimatedArrivalTime;

    /**
     * 更新时间间隔（分钟）
     */
    @TableField("update_frequency")
    private Integer updateFrequency;

    /**
     * 最后更新时间
     */
    @TableField("last_update_time")
    private LocalDateTime lastUpdateTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 完整的订单信息
     */
    @TableField(exist = false)
    private Order order;
} 
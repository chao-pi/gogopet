package com.backend.model.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 订单追踪数据传输对象
 */
@Data
public class OrderTrackingDTO {
    /**
     * 追踪ID，唯一标识符
     */
    @Size(min = 18, max = 18, message = "追踪ID长度必须为18个字符")
    private String trackingId;

    /**
     * 订单ID，关联订单
     */
    @Size(min = 18, max = 18, message = "订单ID长度必须为18个字符")
    private String orderId;

    /**
     * 当前位置
     */
    @Size(max = 255, message = "位置信息长度不能超过255个字符")
    private String location;

    /**
     * 状态：T-运输中，R-休息中
     */
    @Size(max = 1, message = "状态长度必须为1个字符")
    private String status;
} 
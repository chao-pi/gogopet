package com.backend.service;

import com.backend.model.entity.OrderTracking;
import com.backend.model.entity.Picture;
import com.backend.mapper.PictureMapper;

public interface OrderTrackingService {
    /**
     * 获取订单追踪信息
     * @param orderId 订单ID
     * @return 订单追踪信息
     */
    OrderTracking getOrderTracking(String orderId);

    /**
     * 更新订单追踪信息
     * @param orderId 订单ID
     * @param tracking 追踪信息
     * @return 更新后的追踪信息
     */
    OrderTracking updateOrderTracking(String orderId, OrderTracking tracking);

    /**
     * 检查当前用户是否为订单所有者
     * @param orderId 订单ID
     * @return 是否为订单所有者
     */
    boolean isOrderOwner(String orderId);

    /**
     * 检查当前用户是否为普通用户（user_type = 'U'）
     * @return 是否为普通用户
     */
    boolean isNormalUser();
} 
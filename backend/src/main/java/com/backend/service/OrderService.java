package com.backend.service;

import com.backend.model.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface OrderService extends IService<Order> {
    // 创建订单
    Order createOrder(Order order);
    
    /**
     * 更新订单状态
     * @param orderId 订单ID
     * @param status 新状态
     * @return 是否更新成功
     */
    boolean updateOrderStatus(String orderId, String status);
    
    // 获取订单详情
    Order getOrderDetail(String orderId);
    
    // 取消订单
    boolean cancelOrder(String orderId);
    
    // 获取用户订单列表
    List<Order> listByUserId(String userId);
} 
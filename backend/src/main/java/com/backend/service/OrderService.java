package com.backend.service;

import com.backend.model.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrderService extends IService<Order> {
    // 创建订单
    Order createOrder(Order order);
    
    // 更新订单状态
    boolean updateOrderStatus(Long orderId, Integer status);
    
    // 获取订单详情
    Order getOrderDetail(Long orderId);
    
    // 取消订单
    boolean cancelOrder(Long orderId);
} 
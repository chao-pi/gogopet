package com.backend.service;

import com.backend.model.entity.Order;
import com.backend.model.dto.PetDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
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

    List<Order> listByCompanyId(String companyId);

    /**
     * 获取订单的宠物信息
     * @param orderId 订单ID
     * @return 宠物信息列表
     */
    List<PetDTO> getOrderPets(String orderId);

    // 在 OrderService.java 中添加
    boolean updateOrderEndTime(String orderId);

    boolean evaluateOrder(String orderId, BigDecimal rating, String ratingComment);
} 
package com.backend.service;

import com.backend.model.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface OrderService extends IService<Order> {
    // 创建订单
    Order createOrder(Order order);
    
    /**
     * 更新订单状态
     * @param orderId 订单ID
     * @param status 新状态
     * @param petStatus 宠物状态
     * @param location 当前位置
     * @return 是否更新成功
     */
    boolean updateOrderStatus(String orderId, String status, String petStatus, String location);
    
    // 获取订单详情
    Order getOrderDetail(String orderId);
    
    // 取消订单
    boolean cancelOrder(String orderId);
    
    // 获取用户订单列表
    List<Order> listByUserId(String userId);
    
    /**
     * 获取用户订单列表（分页）
     * @param userId 用户ID
     * @param pageNum 页码（从0开始）
     * @param pageSize 每页大小
     * @return 分页订单列表
     */
    Page<Order> listByUserId(String userId, int pageNum, int pageSize);
    
    /**
     * 获取公司订单列表（分页）
     * @param companyId 公司ID
     * @param pageNum 页码（从0开始）
     * @param pageSize 每页大小
     * @return 分页订单列表
     */
    Page<Order> listByCompanyId(String companyId, int pageNum, int pageSize);
} 
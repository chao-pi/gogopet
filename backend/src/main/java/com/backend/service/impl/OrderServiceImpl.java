package com.backend.service.impl;

import com.backend.model.entity.Order;
import com.backend.mapper.OrderMapper;
import com.backend.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Order order) {
        if (order == null) {
            throw new RuntimeException("订单信息不能为空");
        }
        if (order.getUserId() == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        if (order.getPetId() == null) {
            throw new RuntimeException("宠物ID不能为空");
        }
        if (order.getAmount() == null || order.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("订单金额必须大于0");
        }
        if (order.getAddress() == null || order.getAddress().trim().isEmpty()) {
            throw new RuntimeException("收货地址不能为空");
        }
        if (order.getPhone() == null || order.getPhone().trim().isEmpty()) {
            throw new RuntimeException("联系电话不能为空");
        }
        if (order.getReceiver() == null || order.getReceiver().trim().isEmpty()) {
            throw new RuntimeException("收货人不能为空");
        }
        
        order.setStatus(0); // 初始状态为待支付
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        save(order);
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderStatus(Long orderId, Integer status) {
        if (orderId == null) {
            throw new RuntimeException("订单ID不能为空");
        }
        if (status == null || status < 0 || status > 4) {
            throw new RuntimeException("无效的订单状态");
        }
        
        Order order = getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 检查状态转换是否合法
        if (!isValidStatusTransition(order.getStatus(), status)) {
            throw new RuntimeException("非法的状态转换");
        }
        
        order.setStatus(status);
        order.setUpdateTime(LocalDateTime.now());
        return updateById(order);
    }

    @Override
    public Order getOrderDetail(Long orderId) {
        if (orderId == null) {
            throw new RuntimeException("订单ID不能为空");
        }
        
        Order order = getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(Long orderId) {
        if (orderId == null) {
            throw new RuntimeException("订单ID不能为空");
        }
        
        Order order = getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != 0) {
            throw new RuntimeException("只有待支付的订单才能取消");
        }
        order.setStatus(4); // 设置为已取消
        order.setUpdateTime(LocalDateTime.now());
        return updateById(order);
    }
    
    /**
     * 检查订单状态转换是否合法
     * @param currentStatus 当前状态
     * @param newStatus 新状态
     * @return 是否合法
     */
    private boolean isValidStatusTransition(Integer currentStatus, Integer newStatus) {
        // 待支付(0) -> 已支付(1)
        if (currentStatus == 0 && newStatus == 1) {
            return true;
        }
        // 已支付(1) -> 已发货(2)
        if (currentStatus == 1 && newStatus == 2) {
            return true;
        }
        // 已发货(2) -> 已完成(3)
        if (currentStatus == 2 && newStatus == 3) {
            return true;
        }
        // 待支付(0) -> 已取消(4)
        if (currentStatus == 0 && newStatus == 4) {
            return true;
        }
        return false;
    }
} 
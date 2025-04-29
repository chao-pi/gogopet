package com.backend.service.impl;

import com.backend.model.entity.Order;
import com.backend.mapper.OrderMapper;
import com.backend.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.lang.reflect.Field;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Order order) {
        if (order == null) {
            throw new RuntimeException("订单信息不能为空");
        }
        
        try {
            // 验证运输方式
            Field transportMethodField = Order.class.getDeclaredField("transportMethod");
            transportMethodField.setAccessible(true);
            String transportMethod = (String) transportMethodField.get(order);
            
            System.out.println("Received transport method: " + transportMethod); // 添加日志
            
            if (transportMethod == null || transportMethod.trim().isEmpty()) {
                System.out.println("Transport method is empty"); // 添加日志
                throw new RuntimeException("运输方式不能为空");
            }
            
            transportMethod = transportMethod.trim();
            System.out.println("Trimmed transport method: " + transportMethod); // 添加日志
            
            if (!Order.TransportMethod.SPECIAL.equals(transportMethod) && 
                !Order.TransportMethod.SHARE.equals(transportMethod) && 
                !Order.TransportMethod.AIR.equals(transportMethod)) {
                System.out.println("Invalid transport method: " + transportMethod); // 添加日志
                throw new RuntimeException("非法的运输方式，必须是SPECIAL(专车)、SHARE(拼车)或AIR(空运)");
            }
            
            // 生成订单ID
            String orderId = generateOrderId();
            Field orderIdField = Order.class.getDeclaredField("orderId");
            orderIdField.setAccessible(true);
            orderIdField.set(order, orderId);
            
            // 设置初始状态
            Field orderStatusField = Order.class.getDeclaredField("orderStatus");
            orderStatusField.setAccessible(true);
            orderStatusField.set(order, "P"); // 待支付
            
            Field petStatusField = Order.class.getDeclaredField("petStatus");
            petStatusField.setAccessible(true);
            petStatusField.set(order, "N"); // 正常
            
            Field createTimeField = Order.class.getDeclaredField("createTime");
            createTimeField.setAccessible(true);
            createTimeField.set(order, LocalDateTime.now());
            
            Field updateTimeField = Order.class.getDeclaredField("updateTime");
            updateTimeField.setAccessible(true);
            updateTimeField.set(order, LocalDateTime.now());
            
            System.out.println("Final order data: " + order); // 添加日志
            
            save(order);
            return order;
        } catch (Exception e) {
            throw new RuntimeException("创建订单失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderStatus(String orderId, String status) {
        if (orderId == null) {
            throw new RuntimeException("订单ID不能为空");
        }
        if (status == null) {
            throw new RuntimeException("订单状态不能为空");
        }
        
        Order order = getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 检查状态转换是否合法
        if (!isValidStatusTransition(order.getOrderStatus(), status)) {
            throw new RuntimeException("非法的状态转换");
        }
        
        order.setOrderStatus(status);
        order.setUpdateTime(LocalDateTime.now());
        return updateById(order);
    }

    @Override
    public Order getOrderDetail(String orderId) {
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
    public boolean cancelOrder(String orderId) {
        if (orderId == null) {
            throw new RuntimeException("订单ID不能为空");
        }
        
        Order order = getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!"P".equals(order.getOrderStatus())) {
            throw new RuntimeException("只有待支付的订单才能取消");
        }
        order.setOrderStatus("X"); // 设置为已取消
        order.setUpdateTime(LocalDateTime.now());
        return updateById(order);
    }
    
    @Override
    public List<Order> listByUserId(String userId) {
        if (userId == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, userId)
                   .orderByDesc(Order::getCreateTime);
        return list(queryWrapper);
    }
    
    /**
     * 检查订单状态转换是否合法
     * @param currentStatus 当前状态
     * @param newStatus 新状态
     * @return 是否合法
     */
    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        // P(待支付) -> W(待接单)
        if ("P".equals(currentStatus) && "W".equals(newStatus)) {
            return true;
        }
        // W(待接单) -> T(进行中)
        if ("W".equals(currentStatus) && "T".equals(newStatus)) {
            return true;
        }
        // T(进行中) -> C(已完成)
        if ("T".equals(currentStatus) && "C".equals(newStatus)) {
            return true;
        }
        // P(待支付) -> X(已取消)
        if ("P".equals(currentStatus) && "X".equals(newStatus)) {
            return true;
        }
        return false;
    }

    /**
     * 生成18位订单ID
     * 格式：时间戳(13位) + 随机数(5位)
     * @return 订单ID
     */
    private String generateOrderId() {
        long timestamp = System.currentTimeMillis();
        Random random = new Random();
        int randomNum = random.nextInt(90000) + 10000; // 生成5位随机数
        return timestamp + String.valueOf(randomNum);
    }
} 
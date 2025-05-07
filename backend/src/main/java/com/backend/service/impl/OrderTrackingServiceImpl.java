package com.backend.service.impl;

import com.backend.model.entity.Order;
import com.backend.model.entity.OrderTracking;
import com.backend.model.entity.User;
import com.backend.model.entity.Pet;
import com.backend.model.entity.Company;
import com.backend.model.entity.OrderPet;
import com.backend.mapper.OrderMapper;
import com.backend.mapper.OrderTrackingMapper;
import com.backend.mapper.UserMapper;
import com.backend.mapper.PetMapper;
import com.backend.mapper.CompanyMapper;
import com.backend.mapper.OrderPetMapper;
import com.backend.service.OrderTrackingService;
import com.backend.common.exception.UnauthorizedException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class OrderTrackingServiceImpl implements OrderTrackingService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderTrackingMapper orderTrackingMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PetMapper petMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private OrderPetMapper orderPetMapper;

    @Override
    public OrderTracking getOrderTracking(String orderId) {
        // 获取订单追踪信息
        LambdaQueryWrapper<OrderTracking> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderTracking::getOrderId, orderId);
        OrderTracking tracking = orderTrackingMapper.selectOne(wrapper);
        if (tracking == null) {
            throw new RuntimeException("未找到订单追踪信息");
        }

        // 获取订单基本信息
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 获取订单关联的宠物信息
        LambdaQueryWrapper<OrderPet> petWrapper = new LambdaQueryWrapper<>();
        petWrapper.eq(OrderPet::getOrderId, orderId);
        List<OrderPet> orderPets = orderPetMapper.selectList(petWrapper);
        
        if (!orderPets.isEmpty()) {
            // 获取第一个宠物的信息（目前系统只支持一个宠物）
            String petId = orderPets.get(0).getPetId();
            Pet pet = petMapper.selectById(petId);
            if (pet != null) {
                // 设置宠物信息到订单对象
                order.setPetName(pet.getPetName());
                order.setPetBreed(pet.getPetBreed());
                order.setPetAge(pet.getPetAge());
                order.setPetImage(pet.getPetImage());
            }
        }

        // 获取公司信息
        if (order.getCompanyId() != null) {
            Company company = companyMapper.selectById(order.getCompanyId());
            if (company != null) {
                // 获取公司对应的用户信息
                User companyUser = userMapper.selectByCompanyId(company.getCompanyId());
                if (companyUser != null) {
                    order.setCompanyName(companyUser.getUserName());
                }
            }
        }

        // 构建完整的地址信息
        StringBuilder startLocation = new StringBuilder();
        if (order.getStartProvince() != null) {
            startLocation.append(order.getStartProvince());
        }
        if (order.getStartCity() != null) {
            startLocation.append(order.getStartCity());
        }
        if (order.getStartDistrict() != null) {
            startLocation.append(order.getStartDistrict());
        }
        if (order.getStartLocation() != null && !order.getStartLocation().trim().isEmpty()) {
            startLocation.append(order.getStartLocation());
        }
        order.setStartLocation(startLocation.toString());

        StringBuilder endLocation = new StringBuilder();
        if (order.getEndProvince() != null) {
            endLocation.append(order.getEndProvince());
        }
        if (order.getEndCity() != null) {
            endLocation.append(order.getEndCity());
        }
        if (order.getEndDistrict() != null) {
            endLocation.append(order.getEndDistrict());
        }
        if (order.getEndLocation() != null && !order.getEndLocation().trim().isEmpty()) {
            endLocation.append(order.getEndLocation());
        }
        order.setEndLocation(endLocation.toString());

        // 将订单信息设置到追踪对象中
        tracking.setOrder(order);

        return tracking;
    }

    @Override
    @Transactional
    public OrderTracking updateOrderTracking(String orderId, OrderTracking tracking) {
        // 验证订单是否存在
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 更新追踪信息
        tracking.setOrderId(orderId);
        int updated = orderTrackingMapper.updateById(tracking);
        if (updated == 0) {
            throw new RuntimeException("更新订单追踪信息失败");
        }
        return tracking;
    }

    @Override
    public boolean isOrderOwner(String orderId) {
        // 获取当前登录用户ID
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (currentUserId == null) {
            throw new UnauthorizedException("用户未登录");
        }
        
        // 查询订单信息
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 检查当前用户是否为订单所有者
        return order.getUserId().equals(currentUserId);
    }

    @Override
    public boolean isNormalUser() {
        // 获取当前登录用户ID
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (currentUserId == null) {
            throw new UnauthorizedException("用户未登录");
        }

        // 查询用户信息
        User user = userMapper.selectById(currentUserId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查用户类型是否为普通用户（U）
        return "U".equals(user.getUserType());
    }
} 
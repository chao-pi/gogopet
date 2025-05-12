package com.backend.service.impl;

import com.backend.model.entity.Order;
import com.backend.model.entity.OrderTracking;
import com.backend.model.entity.User;
import com.backend.model.entity.Pet;
import com.backend.model.entity.Company;
import com.backend.model.entity.OrderPet;
import com.backend.model.entity.Picture;
import com.backend.mapper.OrderMapper;
import com.backend.mapper.OrderTrackingMapper;
import com.backend.mapper.UserMapper;
import com.backend.mapper.PetMapper;
import com.backend.mapper.CompanyMapper;
import com.backend.mapper.OrderPetMapper;
import com.backend.mapper.PictureMapper;
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

    @Autowired
    private PictureMapper pictureMapper;

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
                // 设置宠物基本信息到订单对象
                order.setPetName(pet.getPetName());
                order.setPetBreed(pet.getPetBreed());
                order.setPetAge(pet.getPetAge());
                
                // 从t_picture表获取宠物图片URL
                Picture picture = pictureMapper.selectByPetId(petId);
                if (picture != null && picture.getPictureUrl() != null) {
                    order.setPetImage(picture.getPictureUrl());
                }
            }
        }

        // 获取公司信息
        Company company = companyMapper.selectById(order.getCompanyId());
        if (company != null) {
            order.setCompanyName(company.getCompanyId());
        }

        // 设置订单信息到追踪对象
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
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        Order order = orderMapper.selectById(orderId);
        return order != null && order.getUserId().equals(currentUserId);
    }

    @Override
    public boolean isNormalUser() {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userMapper.selectById(currentUserId);
        return user != null && "U".equals(user.getUserType());
    }
} 
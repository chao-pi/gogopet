package com.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.backend.mapper.OrderPetMapper;
import com.backend.model.entity.OrderPet;
import com.backend.service.OrderPetService;
import org.springframework.stereotype.Service;

/**
 * 订单宠物关联Service实现类
 */
@Service
public class OrderPetServiceImpl extends ServiceImpl<OrderPetMapper, OrderPet> implements OrderPetService {
} 
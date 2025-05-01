package com.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.backend.model.entity.OrderPet;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单宠物关联Mapper接口
 */
@Mapper
public interface OrderPetMapper extends BaseMapper<OrderPet> {
} 
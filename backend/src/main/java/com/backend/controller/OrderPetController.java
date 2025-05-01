package com.backend.controller;

import com.backend.model.entity.OrderPet;
import com.backend.service.OrderPetService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单-宠物关联控制器
 */
@RestController
@RequestMapping("/order-pet")
public class OrderPetController {

    @Autowired
    private OrderPetService orderPetService;

    /**
     * 创建订单-宠物关联
     */
    @PostMapping
    public OrderPet create(@RequestBody OrderPet orderPet) {
        orderPetService.save(orderPet);
        return orderPet;
    }

    /**
     * 批量创建订单-宠物关联
     */
    @PostMapping("/batch")
    public List<OrderPet> createBatch(@RequestBody List<OrderPet> orderPets) {
        orderPetService.saveBatch(orderPets);
        return orderPets;
    }

    /**
     * 根据订单ID查询关联的宠物
     */
    @GetMapping("/order/{orderId}")
    public List<OrderPet> getByOrderId(@PathVariable String orderId) {
        LambdaQueryWrapper<OrderPet> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderPet::getOrderId, orderId);
        return orderPetService.list(wrapper);
    }

    /**
     * 根据宠物ID查询关联的订单
     */
    @GetMapping("/pet/{petId}")
    public List<OrderPet> getByPetId(@PathVariable String petId) {
        LambdaQueryWrapper<OrderPet> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderPet::getPetId, petId);
        return orderPetService.list(wrapper);
    }

    /**
     * 删除订单-宠物关联
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable String id) {
        return orderPetService.removeById(id);
    }

    /**
     * 根据订单ID删除所有关联
     */
    @DeleteMapping("/order/{orderId}")
    public boolean deleteByOrderId(@PathVariable String orderId) {
        LambdaUpdateWrapper<OrderPet> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(OrderPet::getOrderId, orderId);
        return orderPetService.remove(wrapper);
    }

    /**
     * 根据宠物ID删除所有关联
     */
    @DeleteMapping("/pet/{petId}")
    public boolean deleteByPetId(@PathVariable String petId) {
        LambdaUpdateWrapper<OrderPet> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(OrderPet::getPetId, petId);
        return orderPetService.remove(wrapper);
    }
} 
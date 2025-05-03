package com.backend.controller;

import com.backend.common.Result;
import com.backend.model.dto.OrderDTO;
import com.backend.model.dto.PetDTO;
import com.backend.model.entity.Order;
import com.backend.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping("/create")
    public Result<Order> createOrder(@RequestBody OrderDTO orderDTO) {
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);
        // 设置初始状态
        order.setOrderStatus("P"); // 待支付
        Order createdOrder = orderService.createOrder(order);
        return Result.success(createdOrder);
    }

    @GetMapping("/list")
    public Result<List<Order>> getOrders(@RequestParam String userId) {
        List<Order> orders = orderService.listByUserId(userId);
        return Result.success(orders);
    }

    @GetMapping("/listcompanyid")
    public Result<List<Order>> getOrdersByCompanyId(@RequestParam String companyId) {
        List<Order> orders = orderService.listByCompanyId(companyId);
        return Result.success(orders);
    }

    @GetMapping("/detail/{orderId}")
    public Result<Order> getOrderDetail(@PathVariable String orderId) {
        Order order = orderService.getOrderDetail(orderId);
        return Result.success(order);
    }

    @PostMapping("/cancel/{orderId}")
    public Result<Boolean> cancelOrder(@PathVariable String orderId) {
        boolean result = orderService.cancelOrder(orderId);
        return Result.success(result);
    }

    @PostMapping("/pay/{orderId}")
    public Result<Boolean> payOrder(@PathVariable String orderId) {
        boolean result = orderService.updateOrderStatus(orderId, "W"); // 已支付
        return Result.success(result);
    }

    @PostMapping("/confirm/{orderId}")
    public Result<Boolean> confirmOrder(@PathVariable String orderId) {
        boolean result = orderService.updateOrderStatus(orderId, "C"); // 已完成
        return Result.success(result);
    }

    @PostMapping("/payment/qrcode")
    public Result<String> getPaymentQRCode(@RequestParam String orderId) {
        // 这里应该调用支付服务生成二维码
        // 为了演示，我们返回一个模拟的二维码URL
        String qrCodeUrl = "https://example.com/payment/qrcode/" + orderId;
        return Result.success(qrCodeUrl);
    }

    @PostMapping("/status/update")
    public Result<Boolean> updateOrderStatus(@RequestParam String orderId, @RequestParam String status) {
        boolean success = orderService.updateOrderStatus(orderId, status);
        return Result.success(success);
    }

    /**
     * 获取订单的宠物信息
     * @param orderId 订单ID
     * @return 宠物信息列表
     */
    @GetMapping("/{orderId}/pets")
    public Result<List<PetDTO>> getOrderPets(@PathVariable String orderId) {
        List<PetDTO> pets = orderService.getOrderPets(orderId);
        return Result.success(pets);
    }

    // 在 OrderController.java 中添加
    @PostMapping("/endtime/update")
    public Result<Boolean> updateOrderEndTime(
            @RequestParam String orderId
    ) {
        boolean success = orderService.updateOrderEndTime(orderId);
        return Result.success(success);
    }

    @PostMapping("/evaluate")
    public Result<Boolean> evaluateOrder(
            @RequestParam String orderId,
            @RequestParam BigDecimal rating,
            @RequestParam String ratingComment
    ) {
        boolean success = orderService.evaluateOrder(orderId, rating, ratingComment);
        return Result.success(success);
    }
} 
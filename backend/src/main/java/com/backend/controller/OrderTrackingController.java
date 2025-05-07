package com.backend.controller;

import com.backend.model.entity.OrderTracking;
import com.backend.service.OrderTrackingService;
import com.backend.common.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-tracking")
public class OrderTrackingController {

    @Autowired
    private OrderTrackingService orderTrackingService;

    /**
     * 获取订单追踪信息
     * 只有普通用户（user_type = 'U'）可以访问
     */
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_USER') and @orderTrackingService.isNormalUser()")
    public ResponseEntity<OrderTracking> getOrderTracking(@PathVariable String orderId) {
        // 验证当前用户是否为订单所有者
        if (!orderTrackingService.isOrderOwner(orderId)) {
            throw new UnauthorizedException("您没有权限查看此订单的追踪信息");
        }
        OrderTracking tracking = orderTrackingService.getOrderTracking(orderId);
        return ResponseEntity.ok(tracking);
    }

    /**
     * 更新订单追踪信息
     * 只有普通用户（user_type = 'U'）可以访问
     */
    @PutMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_USER') and @orderTrackingService.isNormalUser()")
    public ResponseEntity<OrderTracking> updateOrderTracking(@PathVariable String orderId, @RequestBody OrderTracking tracking) {
        // 验证当前用户是否为订单所有者
        if (!orderTrackingService.isOrderOwner(orderId)) {
            throw new UnauthorizedException("您没有权限更新此订单的追踪信息");
        }
        OrderTracking updatedTracking = orderTrackingService.updateOrderTracking(orderId, tracking);
        return ResponseEntity.ok(updatedTracking);
    }
} 
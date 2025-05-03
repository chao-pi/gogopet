package com.backend.service.impl;

import com.backend.model.entity.Order;
import com.backend.model.entity.OrderPet;
import com.backend.model.entity.OrderTracking;
import com.backend.mapper.OrderMapper;
import com.backend.mapper.OrderPetMapper;
import com.backend.mapper.OrderTrackingMapper;
import com.backend.mapper.UserMapper;
import com.backend.mapper.CompanyMapper;
import com.backend.mapper.PetMapper;
import com.backend.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import org.springframework.beans.BeanUtils;
import java.util.stream.Collectors;
import com.backend.model.dto.PetDTO;
import com.backend.model.entity.Pet;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final OrderPetMapper orderPetMapper;
    private final OrderTrackingMapper orderTrackingMapper;
    private final UserMapper userMapper;
    private final CompanyMapper companyMapper;
    private final PetMapper petMapper;

    public OrderServiceImpl(OrderPetMapper orderPetMapper, 
                          OrderTrackingMapper orderTrackingMapper,
                          UserMapper userMapper,
                          CompanyMapper companyMapper,
                          PetMapper petMapper) {
        this.orderPetMapper = orderPetMapper;
        this.orderTrackingMapper = orderTrackingMapper;
        this.userMapper = userMapper;
        this.companyMapper = companyMapper;
        this.petMapper = petMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Order order) {
        if (order == null) {
            throw new RuntimeException("订单信息不能为空");
        }
        
        try {
            // 1. 验证用户信息
            if (order.getUserId() == null || userMapper.selectById(order.getUserId()) == null) {
                throw new RuntimeException("用户ID无效或用户不存在");
            }
            
            // 2. 验证公司信息
            if (order.getCompanyId() == null || companyMapper.selectById(order.getCompanyId()) == null) {
                throw new RuntimeException("公司ID无效或公司不存在");
            }
            
            // 3. 验证运输方式
            String transportMethod = order.getTransportMethod();
            if (transportMethod == null || transportMethod.trim().isEmpty()) {
                throw new RuntimeException("运输方式不能为空");
            }
            
            transportMethod = transportMethod.trim();
            if (!Order.TransportMethod.SPECIAL.equals(transportMethod) && 
                !Order.TransportMethod.SHARE.equals(transportMethod) && 
                !Order.TransportMethod.AIR.equals(transportMethod)) {
                throw new RuntimeException("非法的运输方式，必须是SPECIAL(专车)、SHARE(拼车)或AIR(空运)");
            }
            
            // 4. 验证宠物信息
            if (order.getPetIds() != null && !order.getPetIds().isEmpty()) {
                for (String petId : order.getPetIds()) {
                    if (petMapper.selectById(petId) == null) {
                        throw new RuntimeException("宠物ID无效或宠物不存在: " + petId);
                    }
                }
            }
            
            // 5. 验证价格信息
            if (order.getPrice() == null || order.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("订单价格必须大于0");
            }
            
            // 6. 验证地理位置信息
            if (order.getStartLatitude() == null || order.getStartLongitude() == null ||
                order.getEndLatitude() == null || order.getEndLongitude() == null) {
                throw new RuntimeException("起点和终点位置信息不能为空");
            }
            
            // 7. 生成订单ID
            order.setOrderId(generateOrderId());
            
            // 8. 设置初始状态
            order.setOrderStatus("P"); // 待支付
            order.setPetStatus("N"); // 正常
            
            // 9. 设置时间相关字段
            LocalDateTime now = LocalDateTime.now();
            order.setCreateTime(now);
            order.setUpdateTime(now);
            order.setStartTime(now);
            order.setEndTime(null);
            order.setCompleteTime(null);
            
            // 10. 计算距离
            double distance = calculateDistance(
                order.getStartLatitude().doubleValue(),
                order.getStartLongitude().doubleValue(),
                order.getEndLatitude().doubleValue(),
                order.getEndLongitude().doubleValue()
            );
            order.setDistance(BigDecimal.valueOf(distance));
            
            // 11. 处理订单备注
            if (order.getOrderRemark() != null) {
                System.out.println("原始订单备注: " + order.getOrderRemark());
                order.setOrderRemark(order.getOrderRemark().trim());
                System.out.println("处理后的订单备注: " + order.getOrderRemark());
            } else {
                System.out.println("订单备注为空");
            }
            
            // 12. 保存订单
            System.out.println("保存订单前的完整数据: " + order);
            save(order);
            System.out.println("订单保存成功，订单ID: " + order.getOrderId());
            
            // 13. 创建订单宠物关联记录
            if (order.getPetIds() != null && !order.getPetIds().isEmpty()) {
                for (String petId : order.getPetIds()) {
                    OrderPet orderPet = new OrderPet();
                    orderPet.setOrderPetId(generateOrderId());
                    orderPet.setOrderId(order.getOrderId());
                    orderPet.setPetId(petId);
                    orderPet.setCreateTime(now);
                    orderPetMapper.insert(orderPet);
                }
            }
            
            // 14. 创建订单追踪记录
            OrderTracking orderTracking = new OrderTracking();
            orderTracking.setTrackingId(generateOrderId());
            orderTracking.setOrderId(order.getOrderId());
            orderTracking.setLocation(order.getStartLocation());
            orderTracking.setLatitude(order.getStartLatitude());
            orderTracking.setLongitude(order.getStartLongitude());
            orderTracking.setStatus("T"); // 运输中
            orderTracking.setCreateTime(now);
            orderTracking.setUpdateFrequency(30); // 默认30分钟更新一次
            orderTracking.setLastUpdateTime(now);
            orderTracking.setRemark(order.getOrderRemark()); // 设置订单备注
            orderTrackingMapper.insert(orderTracking);
            
            return order;
        } catch (Exception e) {
            throw new RuntimeException("创建订单失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderStatus(String orderId, String status) {
        if (orderId == null || status == null) {
            return false;
        }
        
        Order order = getById(orderId);
        if (order == null) {
            return false;
        }
        
        // 检查状态转换是否合法
        if (!isValidStatusTransition(order.getOrderStatus(), status)) {
            throw new RuntimeException("非法的状态转换");
        }
        
        order.setOrderStatus(status);
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

    @Override
    public List<Order> listByCompanyId(String companyId) {
        if (companyId == null) {
            throw new RuntimeException("公司ID不能为空");
        }

        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getCompanyId, companyId)
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

    /**
     * 计算两点之间的距离（单位：公里）
     * 使用Haversine公式计算球面距离
     * @param lat1 起点纬度
     * @param lon1 起点经度
     * @param lat2 终点纬度
     * @param lon2 终点经度
     * @return 距离（公里）
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 地球半径（公里）
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }

    @Override
    public List<PetDTO> getOrderPets(String orderId) {
        if (orderId == null) {
            throw new RuntimeException("订单ID不能为空");
        }

        // 1. 查询订单是否存在
        Order order = getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 2. 查询订单-宠物关联记录
        LambdaQueryWrapper<OrderPet> orderPetWrapper = new LambdaQueryWrapper<>();
        orderPetWrapper.eq(OrderPet::getOrderId, orderId);
        List<OrderPet> orderPets = orderPetMapper.selectList(orderPetWrapper);

        // 3. 获取所有宠物ID
        List<String> petIds = orderPets.stream()
                .map(OrderPet::getPetId)
                .collect(Collectors.toList());

        // 4. 查询宠物详细信息
        List<PetDTO> petDTOs = new ArrayList<>();
        for (String petId : petIds) {
            try {
                Pet pet = petMapper.selectById(petId);
                if (pet != null) {
                    PetDTO petDTO = new PetDTO();
                    BeanUtils.copyProperties(pet, petDTO);
                    petDTOs.add(petDTO);
                }
            } catch (Exception e) {
                System.err.println("获取宠物信息失败，宠物ID: " + petId + ", 错误: " + e.getMessage());
            }
        }

        return petDTOs;
    }

    // 在 OrderServiceImpl.java 中添加
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderEndTime(String orderId) {
        if (orderId == null) {
            return false;
        }
        Order order = getById(orderId);
        if (order == null) {
            return false;
        }
        order.setEndTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        return updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean evaluateOrder(String orderId, BigDecimal rating, String ratingComment) {
        if (orderId == null || rating == null || ratingComment == null) {
            return false;
        }
        Order order = getById(orderId);
        if (order == null) {
            return false;
        }
        order.setRating(rating);
        order.setRatingComment(ratingComment);
        order.setUpdateTime(LocalDateTime.now());
        order.setCompleteTime(LocalDateTime.now());
        return updateById(order);
    }
} 
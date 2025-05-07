package com.backend.service.impl;

import com.backend.model.entity.Order;
import com.backend.model.entity.OrderPet;
import com.backend.model.entity.OrderTracking;
import com.backend.model.entity.Pet;
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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.backend.model.entity.Company;
import com.backend.model.entity.User;

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
    public boolean updateOrderStatus(String orderId, String status, String petStatus, String location) {
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
        
        // 更新订单状态
        order.setOrderStatus(status);
        order.setUpdateTime(LocalDateTime.now());
        
        // 更新宠物状态（如果提供）
        if (petStatus != null) {
            order.setPetStatus(petStatus);
        }
        
        // 更新订单追踪信息
        LambdaQueryWrapper<OrderTracking> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderTracking::getOrderId, orderId);
        OrderTracking tracking = orderTrackingMapper.selectOne(wrapper);
        
        if (tracking != null) {
            tracking.setStatus(status);
            if (location != null) {
                tracking.setLocation(location);
            }
            tracking.setLastUpdateTime(LocalDateTime.now());
            orderTrackingMapper.updateById(tracking);
        }
        
        return updateById(order);
    }

    @Override
    public Order getOrderDetail(String orderId) {
        System.out.println("=== 开始获取订单详情 ===");
        System.out.println("订单ID: " + orderId);
        
        if (orderId == null) {
            System.out.println("订单ID为空，抛出异常");
            throw new RuntimeException("订单ID不能为空");
        }
        
        // 获取订单基本信息
        Order order = getById(orderId);
        System.out.println("查询到的订单信息: " + (order != null ? 
            "订单ID: " + order.getOrderId() + ", 公司ID: " + order.getCompanyId() : 
            "订单不存在"));
        
        if (order == null) {
            System.out.println("订单不存在，抛出异常");
            throw new RuntimeException("订单不存在");
        }

        // 获取订单关联的宠物信息
        LambdaQueryWrapper<OrderPet> petWrapper = new LambdaQueryWrapper<>();
        petWrapper.eq(OrderPet::getOrderId, orderId);
        List<OrderPet> orderPets = orderPetMapper.selectList(petWrapper);
        System.out.println("查询到的宠物关联信息数量: " + orderPets.size());
        
        if (!orderPets.isEmpty()) {
            // 获取第一个宠物的信息（目前系统只支持一个宠物）
            String petId = orderPets.get(0).getPetId();
            Pet pet = petMapper.selectById(petId);
            System.out.println("查询到的宠物信息: " + (pet != null ? 
                "宠物ID: " + pet.getPetId() + ", 宠物名称: " + pet.getPetName() : 
                "宠物不存在"));
            
            if (pet != null) {
                // 设置宠物信息到订单对象
                order.setPetName(pet.getPetName());
                order.setPetBreed(pet.getPetBreed());
                order.setPetAge(pet.getPetAge());
                order.setPetImage(pet.getPetImage());
                System.out.println("已设置宠物信息到订单");
            }
        }

        // 获取公司信息
        System.out.println("=== 开始查询公司信息 ===");
        System.out.println("订单中的公司ID: " + order.getCompanyId());
        
        if (order.getCompanyId() != null) {
            System.out.println("开始查询公司信息 - 公司ID: " + order.getCompanyId());
            
            Company company = companyMapper.selectById(order.getCompanyId());
            System.out.println("查询到的公司信息: " + (company != null ? 
                "公司ID: " + company.getCompanyId() : 
                "公司不存在"));
            
            if (company != null) {
                System.out.println("开始查询公司对应的用户信息");
                User companyUser = userMapper.selectByCompanyId(company.getCompanyId());
                System.out.println("查询到的公司用户信息: " + (companyUser != null ? 
                    "用户ID: " + companyUser.getUserId() + ", 用户名: " + companyUser.getUserName() : 
                    "用户不存在"));
                
                if (companyUser != null) {
                    order.setCompanyName(companyUser.getUserName());
                    System.out.println("设置公司名称: " + companyUser.getUserName());
                }
            }
        } else {
            System.out.println("订单中没有公司ID，跳过公司信息查询");
        }

        // 构建完整的地址信息
        System.out.println("=== 开始构建地址信息 ===");
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
        if (order.getStartLocation() != null) {
            startLocation.append(order.getStartLocation());
        }
        order.setStartLocation(startLocation.toString());
        System.out.println("构建的起始地址: " + startLocation.toString());

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
        if (order.getEndLocation() != null) {
            endLocation.append(order.getEndLocation());
        }
        order.setEndLocation(endLocation.toString());
        System.out.println("构建的目的地址: " + endLocation.toString());

        System.out.println("=== 订单详情获取完成 ===");
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
    public Page<Order> listByUserId(String userId, int pageNum, int pageSize) {
        if (userId == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        
        System.out.println("查询用户订单 - 用户ID: " + userId + ", 页码: " + pageNum + ", 每页大小: " + pageSize);
        
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, userId)
                   .orderByDesc(Order::getCreateTime);
        
        Page<Order> result = page(page, queryWrapper);
        System.out.println("查询结果 - 总记录数: " + result.getTotal() + ", 当前页记录数: " + result.getRecords().size());
        
        // 为每个订单添加公司信息
        for (Order order : result.getRecords()) {
            System.out.println("处理订单: " + order.getOrderId() + ", 公司ID: " + order.getCompanyId());
            if (order.getCompanyId() != null) {
                // 查询公司对应的用户（司机）信息
                User driver = userMapper.selectByCompanyId(order.getCompanyId());
                System.out.println("查询到的司机信息: " + (driver != null ? 
                    "司机ID: " + driver.getUserId() + ", 司机名称: " + driver.getUserName() : 
                    "司机不存在"));
                
                if (driver != null) {
                    order.setCompanyName(driver.getUserName());
                    System.out.println("设置公司名称: " + driver.getUserName());
                }
            }
        }
        
        return result;
    }
    
    @Override
    public Page<Order> listByCompanyId(String companyId, int pageNum, int pageSize) {
        if (companyId == null) {
            throw new RuntimeException("公司ID不能为空");
        }
        
        System.out.println("查询公司订单 - 公司ID: " + companyId + ", 页码: " + pageNum + ", 每页大小: " + pageSize);
        
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getCompanyId, companyId)
                   .orderByDesc(Order::getCreateTime);
        
        Page<Order> result = page(page, queryWrapper);
        System.out.println("查询结果 - 总记录数: " + result.getTotal() + ", 当前页记录数: " + result.getRecords().size());
        
        // 为每个订单添加用户和宠物信息
        for (Order order : result.getRecords()) {
            // 获取用户信息
            User user = userMapper.selectById(order.getUserId());
            if (user != null) {
                order.setUserName(user.getUserName());
            }
            
            // 获取宠物信息
            LambdaQueryWrapper<OrderPet> petWrapper = new LambdaQueryWrapper<>();
            petWrapper.eq(OrderPet::getOrderId, order.getOrderId());
            List<OrderPet> orderPets = orderPetMapper.selectList(petWrapper);
            
            if (!orderPets.isEmpty()) {
                String petId = orderPets.get(0).getPetId();
                Pet pet = petMapper.selectById(petId);
                if (pet != null) {
                    order.setPetName(pet.getPetName());
                    order.setPetBreed(pet.getPetBreed());
                    order.setPetAge(pet.getPetAge());
                }
            }
            
            // 构建完整地址信息
            StringBuilder startLocation = new StringBuilder();
            if (order.getStartProvince() != null) startLocation.append(order.getStartProvince());
            if (order.getStartCity() != null) startLocation.append(order.getStartCity());
            if (order.getStartDistrict() != null) startLocation.append(order.getStartDistrict());
            if (order.getStartLocation() != null) startLocation.append(order.getStartLocation());
            order.setStartLocation(startLocation.toString());

            StringBuilder endLocation = new StringBuilder();
            if (order.getEndProvince() != null) endLocation.append(order.getEndProvince());
            if (order.getEndCity() != null) endLocation.append(order.getEndCity());
            if (order.getEndDistrict() != null) endLocation.append(order.getEndDistrict());
            if (order.getEndLocation() != null) endLocation.append(order.getEndLocation());
            order.setEndLocation(endLocation.toString());
        }
        
        return result;
    }
    
    /**
     * 检查订单状态转换是否合法
     * @param currentStatus 当前状态
     * @param newStatus 新状态
     * @return 是否合法
     */
    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        System.out.println("检查状态转换 - 当前状态: " + currentStatus + ", 新状态: " + newStatus);
        
        // P(待支付) -> W(待接单)
        if ("P".equals(currentStatus) && "W".equals(newStatus)) {
            System.out.println("状态转换: 待支付 -> 待接单");
            return true;
        }
        // W(待接单) -> 1(运输中-起点)
        if ("W".equals(currentStatus) && "1".equals(newStatus)) {
            System.out.println("状态转换: 待接单 -> 运输中-起点");
            return true;
        }
        // 1 -> 2 -> 3 -> 4 -> 5 (运输中的各个阶段)
        if (currentStatus.matches("[1-4]") && newStatus.matches("[2-5]")) {
            int currentStage = Integer.parseInt(currentStatus);
            int newStage = Integer.parseInt(newStatus);
            boolean isValid = newStage == currentStage + 1;
            System.out.println("状态转换: 运输阶段 " + currentStage + " -> " + newStage + ", 是否有效: " + isValid);
            return isValid;
        }
        // 5(运输中-终点) -> C(已完成)
        if ("5".equals(currentStatus) && "C".equals(newStatus)) {
            System.out.println("状态转换: 运输中-终点 -> 已完成");
            return true;
        }
        // P(待支付) -> X(已取消)
        if ("P".equals(currentStatus) && "X".equals(newStatus)) {
            System.out.println("状态转换: 待支付 -> 已取消");
            return true;
        }
        // 允许在运输过程中休息或装卸
        if (currentStatus.matches("[1-5]") && ("R".equals(newStatus) || "L".equals(newStatus))) {
            System.out.println("状态转换: 运输中 -> 休息/装卸");
            return true;
        }
        // 允许从休息或装卸状态返回运输状态
        if (("R".equals(currentStatus) || "L".equals(currentStatus)) && currentStatus.matches("[1-5]")) {
            System.out.println("状态转换: 休息/装卸 -> 运输中");
            return true;
        }
        
        System.out.println("状态转换无效");
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
} 
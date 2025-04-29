package com.backend.model.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单数据传输对象
 */
@Data
public class OrderDTO {
    /**
     * 订单ID，唯一标识符
     */
    @Size(min = 18, max = 18, message = "订单ID长度必须为18个字符")
    private String orderId;

    /**
     * 宠物ID列表，关联托运的宠物
     */
    @NotNull(message = "宠物ID列表不能为空")
    private List<String> petIds;

    /**
     * 用户ID，关联下单用户
     */
    @Size(min = 18, max = 18, message = "用户ID长度必须为18个字符")
    private String userId;

    /**
     * 公司ID，关联承运公司
     */
    @Size(min = 18, max = 18, message = "公司ID长度必须为18个字符")
    private String companyId;

    /**
     * 订单状态：P-待支付，W-待接单，T-运输中，C-已完成，X-已取消
     */
    @Size(max = 1, message = "订单状态长度必须为1个字符")
    private String orderStatus;

    /**
     * 宠物状态：N-正常，A-异常
     */
    @Size(max = 1, message = "宠物状态长度必须为1个字符")
    private String petStatus;

    /**
     * 运输状态：P-待接单，T-运输中，D-已送达
     */
    @Size(max = 1, message = "运输状态长度必须为1个字符")
    private String deliveryStatus;

    /**
     * 运输方式
     * SPECIAL-专车托运
     * SHARE-拼车托运
     * AIR-空运托运
     */
    @NotNull(message = "运输方式不能为空")
    @Size(max = 10, message = "运输方式长度不能超过10个字符")
    private String transportMethod;

    /**
     * 起始省份
     */
    @NotNull(message = "起始省份不能为空")
    private String startProvince;

    /**
     * 起始城市
     */
    @NotNull(message = "起始城市不能为空")
    private String startCity;

    /**
     * 起始区县
     */
    @NotNull(message = "起始区县不能为空")
    private String startDistrict;

    /**
     * 起始详细地址
     */
    @NotNull(message = "起始详细地址不能为空")
    private String startLocation;

    /**
     * 起始地纬度
     */
    private BigDecimal startLatitude;

    /**
     * 起始地经度
     */
    private BigDecimal startLongitude;

    /**
     * 目的省份
     */
    @NotNull(message = "目的省份不能为空")
    private String endProvince;

    /**
     * 目的城市
     */
    @NotNull(message = "目的城市不能为空")
    private String endCity;

    /**
     * 目的区县
     */
    @NotNull(message = "目的区县不能为空")
    private String endDistrict;

    /**
     * 目的详细地址
     */
    @NotNull(message = "目的详细地址不能为空")
    private String endLocation;

    /**
     * 目的地纬度
     */
    private BigDecimal endLatitude;

    /**
     * 目的地经度
     */
    private BigDecimal endLongitude;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 订单价格，单位：元
     */
    @NotNull(message = "订单价格不能为空")
    @DecimalMin(value = "0.01", message = "订单价格必须大于0")
    private BigDecimal price;

    /**
     * 支付状态：U-未支付，P-已支付，R-已退款
     */
    @Size(max = 1, message = "支付状态长度必须为1个字符")
    private String paymentStatus;

    /**
     * 订单备注，用户特殊要求
     */
    private String orderRemark;
} 
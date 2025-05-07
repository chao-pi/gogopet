package com.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单实体类
 * 对应数据库表 t_order
 */
@Data
@Getter
@Setter
@TableName("t_order")
public class Order {
    /**
     * 订单ID，唯一标识符
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String orderId;

    /**
     * 用户ID，关联用户表，表示下单用户
     */
    private String userId;

    /**
     * 公司ID，关联运输公司
     */
    @TableField("company_id")
    @Size(max = 18, message = "公司ID长度不能超过18个字符")
    private String companyId;

    /**
     * 公司名称
     */
    @TableField(exist = false)
    private String companyName;

    /**
     * 用户名称
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 订单状态：P-待支付，W-待接单，T-进行中，C-已完成，X-已取消
     */
    private String orderStatus;

    /**
     * 宠物状态：N-正常，A-异常
     */
    private String petStatus;

    /**
     * 运输方式
     * SPECIAL-专车托运
     * SHARE-拼车托运
     * AIR-空运托运
     */
    @TableField("transport_method")
    private String transportMethod;

    /**
     * 起始省份
     */
    private String startProvince;

    /**
     * 起始城市
     */
    private String startCity;

    /**
     * 起始区县
     */
    private String startDistrict;

    /**
     * 起始详细地址
     */
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
    private String endProvince;

    /**
     * 目的城市
     */
    private String endCity;

    /**
     * 目的区县
     */
    private String endDistrict;

    /**
     * 目的详细地址
     */
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
     * 托运开始时间
     */
    private LocalDateTime startTime;

    /**
     * 托运结束时间
     */
    private LocalDateTime endTime;

    /**
     * 订单完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 运输距离，单位：公里
     */
    private BigDecimal distance;

    /**
     * 订单价格，单位：元
     */
    private BigDecimal price;

    /**
     * 订单备注，用户特殊要求
     */
    private String orderRemark;

    /**
     * 用户评分，1-5分
     */
    private BigDecimal rating;

    /**
     * 用户评价内容
     */
    private String ratingComment;

    @TableField(exist = false)
    private List<String> petIds;

    /**
     * 宠物名称
     */
    @TableField(exist = false)
    private String petName;

    /**
     * 宠物品种
     */
    @TableField(exist = false)
    private String petBreed;

    /**
     * 宠物年龄
     */
    @TableField(exist = false)
    private Integer petAge;

    /**
     * 宠物图片
     */
    @TableField(exist = false)
    private String petImage;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 运输方式枚举
     */
    public static class TransportMethod {
        public static final String SPECIAL = "SPECIAL";
        public static final String SHARE = "SHARE";
        public static final String AIR = "AIR";
    }
} 
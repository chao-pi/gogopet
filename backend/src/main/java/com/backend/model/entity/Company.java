package com.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 托运公司实体类
 * 对应数据库表 t_company
 */
@Data
@TableName("t_company")
public class Company {
    /**
     * 公司ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String companyId;

    /**
     * 公司介绍
     */
    private String companyIntro;

    /**
     * 公司地址
     */
    private String companyLocal;

    /**
     * 公司平均评分
     */
    private Double rating;

    /**
     * 评分总数
     */
    private Integer ratingCount;

    /**
     * 托运次数
     */
    private Integer transportCount;

    /**
     * 服务区域：P-省内，D-国内，I-国际
     */
    private String serviceArea;

    /**
     * 每公里价格
     */
    private BigDecimal transportPricePerKm;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
} 
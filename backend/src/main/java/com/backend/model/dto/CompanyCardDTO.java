package com.backend.model.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 托运公司卡片展示DTO
 */
@Data
public class CompanyCardDTO {
    /**
     * 公司ID
     */
    private String companyId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 公司logo URL
     */
    private String logoUrl;

    /**
     * 公司评分
     */
    private Double rating;

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
     * 公司地址
     */
    private String address;
} 
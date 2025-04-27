package com.backend.model.dto;

import lombok.Data;

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
     * 服务范围
     */
    private String serviceRange;

    /**
     * 公司地址
     */
    private String address;
} 
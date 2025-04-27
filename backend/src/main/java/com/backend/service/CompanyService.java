package com.backend.service;

import com.backend.model.dto.CompanyCardDTO;
import java.util.List;

/**
 * 托运公司服务接口
 */
public interface CompanyService {
    /**
     * 获取所有托运公司卡片信息
     * @return 托运公司卡片列表
     */
    List<CompanyCardDTO> getAllCompanyCards();

    /**
     * 根据公司ID获取托运公司卡片信息
     * @param companyId 公司ID
     * @return 托运公司卡片信息
     */
    CompanyCardDTO getCompanyCardById(String companyId);
} 
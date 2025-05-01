package com.backend.controller;

import com.backend.model.dto.CompanyCardDTO;
import com.backend.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 托运公司控制器
 */
@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    /**
     * 获取所有托运公司卡片信息
     * @return 托运公司卡片列表
     */
    @GetMapping("/cards")
    public List<CompanyCardDTO> getAllCompanyCards() {
        return companyService.getAllCompanyCards();
    }

    /**
     * 根据公司ID获取托运公司卡片信息
     * @param companyId 公司ID
     * @return 托运公司卡片信息
     */
    @GetMapping("/card/{companyId}")
    public CompanyCardDTO getCompanyCardById(@PathVariable String companyId) {
        return companyService.getCompanyCardById(companyId);
    }
} 
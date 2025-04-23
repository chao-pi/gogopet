package com.backend.model.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 托运公司数据传输对象
 */
@Data
public class CompanyDTO {
    /**
     * 公司ID，唯一标识符
     */
    @Size(min = 18, max = 18, message = "公司ID长度必须为18个字符")
    private String companyId;

    /**
     * 公司介绍，详细描述公司情况
     */
    @Size(max = 1000, message = "公司介绍长度不能超过1000个字符")
    private String companyIntro;

    /**
     * 公司地址，用于托运服务
     */
    @Size(max = 63, message = "公司地址长度不能超过63个字符")
    private String companyLocal;
} 
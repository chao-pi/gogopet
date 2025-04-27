package com.backend.mapper;

import com.backend.model.entity.Company;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 托运公司Mapper接口
 */
@Mapper
public interface CompanyMapper extends BaseMapper<Company> {
} 
package com.backend.service.impl;

import com.backend.mapper.CompanyMapper;
import com.backend.mapper.CommentMapper;
import com.backend.mapper.PictureMapper;
import com.backend.mapper.UserMapper;
import com.backend.mapper.OrderMapper;
import com.backend.model.dto.CompanyCardDTO;
import com.backend.model.entity.Company;
import com.backend.model.entity.Comment;
import com.backend.model.entity.Picture;
import com.backend.model.entity.User;
import com.backend.model.entity.Order;
import com.backend.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 托运公司服务实现类
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PictureMapper pictureMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<CompanyCardDTO> getAllCompanyCards() {
        // 获取所有托运公司
        List<Company> companies = companyMapper.selectList(null);
        
        return companies.stream().map(company -> {
            // 获取公司对应的用户信息
            User user = userMapper.selectByCompanyId(company.getCompanyId());
            if (user == null) {
                return null;
            }

            // 获取公司logo
            String logoUrl = null;
            if (user.getPictureId() != null) {
                Picture picture = pictureMapper.selectById(user.getPictureId());
                if (picture != null) {
                    logoUrl = picture.getPictureUrl();
                }
            }

            // 构建DTO
            CompanyCardDTO dto = new CompanyCardDTO();
            dto.setCompanyId(company.getCompanyId());
            dto.setCompanyName(user.getUserName());
            dto.setLogoUrl(logoUrl);
            dto.setRating(company.getRating() != null ? company.getRating().doubleValue() : 0.0);
            dto.setTransportCount(company.getTransportCount() != null ? company.getTransportCount() : 0);
            dto.setServiceArea(company.getServiceArea() != null ? company.getServiceArea() : "D");
            dto.setTransportPricePerKm(company.getTransportPricePerKm() != null ? company.getTransportPricePerKm() : new BigDecimal("0.5"));
            dto.setAddress(company.getCompanyLocal() != null ? company.getCompanyLocal() : "");

            return dto;
        }).filter(dto -> dto != null).collect(Collectors.toList());
    }

    @Override
    public CompanyCardDTO getCompanyCardById(String companyId) {
        // 获取公司信息
        Company company = companyMapper.selectById(companyId);
        if (company == null) {
            return null;
        }

        // 获取公司对应的用户信息
        User user = userMapper.selectByCompanyId(companyId);
        if (user == null) {
            return null;
        }

        // 获取公司logo
        String logoUrl = null;
        if (user.getPictureId() != null) {
            Picture picture = pictureMapper.selectById(user.getPictureId());
            if (picture != null) {
                logoUrl = picture.getPictureUrl();
            }
        }

        // 构建DTO
        CompanyCardDTO dto = new CompanyCardDTO();
        dto.setCompanyId(companyId);
        dto.setCompanyName(user.getUserName());
        dto.setLogoUrl(logoUrl);
        dto.setRating(company.getRating() != null ? company.getRating().doubleValue() : 0.0);
        dto.setTransportCount(company.getTransportCount() != null ? company.getTransportCount() : 0);
        dto.setServiceArea(company.getServiceArea() != null ? company.getServiceArea() : "D");
        dto.setTransportPricePerKm(company.getTransportPricePerKm() != null ? company.getTransportPricePerKm() : new BigDecimal("0.5"));
        dto.setAddress(company.getCompanyLocal() != null ? company.getCompanyLocal() : "");

        return dto;
    }

    /**
     * 计算公司评分
     * @param companyId 公司ID
     * @return 平均评分
     */
    private Double calculateCompanyRating(String companyId) {
        // 查询该公司的所有已完成订单
        List<Order> orders = orderMapper.selectByCompanyIdAndStatus(companyId, "C");
        if (orders.isEmpty()) {
            return 0.0;
        }

        // 计算平均评分
        double sum = orders.stream()
                .filter(order -> order.getRating() != null)
                .mapToDouble(order -> order.getRating().doubleValue())
                .sum();
        
        return sum / orders.size();
    }
} 
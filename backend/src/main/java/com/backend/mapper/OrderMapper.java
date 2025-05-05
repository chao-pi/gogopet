package com.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.backend.model.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    
    /**
     * 根据公司ID和订单状态查询订单列表
     * @param companyId 公司ID
     * @param status 订单状态
     * @return 订单列表
     */
    @Select("SELECT * FROM t_order WHERE company_id = #{companyId} AND order_status = #{status}")
    List<Order> selectByCompanyIdAndStatus(String companyId, String status);
} 
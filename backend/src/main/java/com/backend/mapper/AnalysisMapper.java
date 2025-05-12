package com.backend.mapper;


import com.backend.model.vo.AnalysisVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnalysisMapper {
    /**
     * 获取公司订单量排行数据
     * @return 公司订单量排行数据
     */
    @Select("SELECT "+
    "c.company_id, "+
    "u.user_name, "+
    "COUNT(o.order_id) AS count " +
    "FROM t_company c "+ 
    "JOIN t_user u " +
    "ON c.company_id = u.company_id "+
    "AND u.user_type = 'C' "+
    "LEFT JOIN t_order o "+ 
    "ON c.company_id = o.company_id " +
    "GROUP BY c.company_id, u.user_name " +
    "ORDER BY count DESC")
    List<AnalysisVO.CompanyOrderCount> getCompanyOrderCounts();

    /**
     * 获取订单状态分布数据
     * @return 订单状态分布数据
     */
    @Select("SELECT "+ 
    "CASE "+ 
    "WHEN order_status = 'P' THEN '待支付' "+ 
    "WHEN order_status = 'W' THEN '待接单' "+ 
    "WHEN order_status IN ('1', '2', '3', '4', '5') THEN '运输中' "+ 
    "WHEN order_status = 'C' THEN '已完成' "+ 
    "WHEN order_status = 'X' THEN '已取消' "+ 
    "ELSE '未知状态'  "+ 
    "END AS orderStatus, "+ 
    "COUNT(*) AS count "+ 
    "FROM t_order "+ 
    "GROUP BY CASE "+ 
    "WHEN order_status = 'P' THEN '待支付' "+ 
    "WHEN order_status = 'W' THEN '待接单' "+ 
    "WHEN order_status IN ('1', '2', '3', '4', '5') THEN '运输中' "+ 
    "WHEN order_status = 'C' THEN '已完成' "+ 
    "WHEN order_status = 'X' THEN '已取消' "+ 
    "ELSE '未知状态' "+ 
    "END")
    List<AnalysisVO.OrderStatusCount> getOrderStatusCounts();

    /**
     * 获取运输方式分布数据
     * @return 运输方式分布数据
     */
    @Select("SELECT "+ 
    "CASE "+ 
    "WHEN transport_method = 'SPECIAL' THEN '专车托运' "+ 
    "WHEN transport_method = 'SHARE' THEN '拼车托运' "+ 
    "WHEN transport_method = 'AIR' THEN '空运托运' "+ 
    "ELSE '未知运输方式' "+ 
    "END AS transportMethod, "+ 
    "COUNT(*) AS count "+ 
    "FROM t_order "+ 
    "GROUP BY transport_method")
    List<AnalysisVO.TransportMethodCount> getTransportMethodCounts();

    /**
     * 获取宠物状态统计数据
     * @return 宠物状态统计数据
     */
    @Select("SELECT " +
    "CASE " +
    "WHEN pet_status = 'N' THEN '正常' " +
    "WHEN pet_status = 'A' THEN '异常' " +
    "ELSE '未知状态' " +
    "END AS petStatus, " +
    "COUNT(*) AS count " +
    "FROM t_order " +
    "GROUP BY pet_status")
    List<AnalysisVO.PetStatusCount> getPetStatusCounts();

    /**
     * 获取用户评价词云数据
     * @return 用户评价词云数据
     */
    @Select("SELECT " +
    "word, " +
    "COUNT(*) AS value " +
    "FROM ( " +
    "  SELECT " +
    "  REGEXP_SUBSTR(rating_comment, '[^\\s,，。.!！?？]+') AS word " +
    "  FROM t_order " +
    "  WHERE rating_comment IS NOT NULL " +
    "  AND LENGTH(rating_comment) > 0 " +
    ") t " +
    "WHERE LENGTH(word) > 1 " +
    "GROUP BY word " +
    "ORDER BY value DESC " +
    "LIMIT 100")
    List<AnalysisVO.CommentWord> getCommentWords();

    /**
     * 获取所有用户评价
     * @return 用户评价列表
     */
    @Select("SELECT rating_comment " +
    "FROM t_order " +
    "WHERE rating_comment IS NOT NULL " +
    "AND LENGTH(rating_comment) > 0")
    List<String> getAllComments();

    /**
     * 获取总订单数
     * @return 总订单数
     */
    @Select("SELECT COUNT(*) FROM t_order")
    Integer getTotalOrders();

    /**
     * 获取好评率
     * @return 好评率
     */
    @Select("SELECT "+
    "ROUND(AVG(CASE WHEN rating >= 3.5 THEN 1 ELSE 0 END) * 100, 1) "+
    "FROM "+
    "t_order "+
    "WHERE "+
    "rating IS NOT NULL")
    Double getGoodRate();

    /**
     * 获取活跃公司数，根据托运开始时间（create_time）计算
     * @return 活跃公司数
     */
    @Select("SELECT "+
    "COUNT(DISTINCT company_id) "+
    "FROM "+
    "t_order "+
    "WHERE "+
    "create_time >= DATE_SUB(CURDATE(), "+
    "INTERVAL 30 DAY)")
    Integer getActiveCompanies();
}
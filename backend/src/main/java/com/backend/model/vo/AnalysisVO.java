package com.backend.model.vo;

import lombok.Data;
import java.util.List;

@Data
public class AnalysisVO {
    // 关键指标数据
    private Integer totalOrders;
    private Double goodRate;
    private Integer activeCompanies;
    private Double abnormalRate;
    
    // 公司订单量排行数据
    private List<CompanyOrderCount> companyOrderCounts;
    
    // 订单状态分布数据
    private List<OrderStatusCount> orderStatusCounts;
    
    // 运输方式分布数据
    private List<TransportMethodCount> transportMethodCounts;
    
    // 宠物状态统计数据
    private List<PetStatusCount> petStatusCounts;
    
    // 用户评价词云数据
    private List<CommentWord> commentWords;
    
    @Data
    public static class CompanyOrderCount {
        private String companyId;
        private String userName;
        private Integer count;
    }
    
    @Data
    public static class OrderStatusCount {
        private String orderStatus;
        private Integer count;
    }
    
    @Data
    public static class TransportMethodCount {
        private String transportMethod;
        private Integer count;
    }
    
    @Data
    public static class PetStatusCount {
        private String petStatus;
        private Integer count;
    }
    
    @Data
    public static class CommentWord {
        private String word;
        private Integer value;
    }
} 
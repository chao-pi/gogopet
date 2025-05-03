package com.backend.service;

import com.backend.model.vo.AnalysisVO;

/**
 * 可视化分析服务接口
 */
public interface AnalysisService {
    /**
     * 获取分析数据
     * @return 分析数据
     */
    AnalysisVO getAnalysisData();
} 
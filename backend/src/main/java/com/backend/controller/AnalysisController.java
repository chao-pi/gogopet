package com.backend.controller;

import com.backend.common.Result;
import com.backend.model.vo.AnalysisVO;
import com.backend.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 可视化分析控制器
 */
@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    /**
     * 获取分析数据
     * @return 分析数据
     */
    @GetMapping("/data")
    public Result<AnalysisVO> getAnalysisData() {
        AnalysisVO analysisData = analysisService.getAnalysisData();
        return Result.success(analysisData);
    }
}
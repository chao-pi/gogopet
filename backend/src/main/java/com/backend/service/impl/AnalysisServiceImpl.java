package com.backend.service.impl;

import com.backend.mapper.AnalysisMapper;
import com.backend.model.vo.AnalysisVO;
import com.backend.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AnalysisServiceImpl implements AnalysisService {

    @Autowired
    private AnalysisMapper analysisMapper;

    @Override
    public AnalysisVO getAnalysisData() {
        AnalysisVO analysisVO = new AnalysisVO();

        // 获取关键指标数据
        analysisVO.setTotalOrders(analysisMapper.getTotalOrders());
        analysisVO.setGoodRate(analysisMapper.getGoodRate());
        analysisVO.setActiveCompanies(analysisMapper.getActiveCompanies());
        analysisVO.setAbnormalRate(analysisMapper.getAbnormalRate());

        // 获取公司订单量排行数据
        List<AnalysisVO.CompanyOrderCount> companyOrderCounts = analysisMapper.getCompanyOrderCounts();
        analysisVO.setCompanyOrderCounts(companyOrderCounts);

        // 获取订单状态分布数据
        List<AnalysisVO.OrderStatusCount> orderStatusCounts = analysisMapper.getOrderStatusCounts();
        analysisVO.setOrderStatusCounts(orderStatusCounts);

        // 获取运输方式分布数据
        List<AnalysisVO.TransportMethodCount> transportMethodCounts = analysisMapper.getTransportMethodCounts();
        analysisVO.setTransportMethodCounts(transportMethodCounts);

        // 获取宠物状态统计数据
        List<AnalysisVO.PetStatusCount> petStatusCounts = analysisMapper.getPetStatusCounts();
        analysisVO.setPetStatusCounts(petStatusCounts);

        // 获取用户评价词云数据
        // List<AnalysisVO.CommentWord> commentWords = analysisMapper.getCommentWords();
        List<AnalysisVO.CommentWord> commentWords = processCommentWords();
        analysisVO.setCommentWords(commentWords);

        return analysisVO;
    }

    private List<AnalysisVO.CommentWord> processCommentWords() {
        try {
            // 获取所有评论
            List<String> comments = analysisMapper.getAllComments();
            
            // 将评论写入临时文件，使用UTF-8编码
            File tempFile = File.createTempFile("comments", ".txt");
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(tempFile), StandardCharsets.UTF_8))) {
                for (String comment : comments) {
                    writer.write(comment);
                    writer.newLine();
                }
            }

            // 构建Python命令
            String pythonScript = new File("backend/src/main/resources/scripts/word_cloud_processor.py").getAbsolutePath();
            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScript);
            
            // 设置工作目录
            processBuilder.directory(new File("."));
            
            // 设置环境变量
            processBuilder.environment().put("PYTHONIOENCODING", "utf-8");
            
            // 重定向输入和错误输出
            processBuilder.redirectInput(tempFile);
            processBuilder.redirectErrorStream(true);
            
            // 执行Python脚本
            Process process = processBuilder.start();
            
            // 读取Python脚本的输出
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            }
            
            // 等待进程完成
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Python脚本执行失败: " + output.toString());
            }
            
            // 解析输出结果
            String jsonOutput = output.toString();
            List<AnalysisVO.CommentWord> commentWords = parseJsonOutput(jsonOutput);
            
            // 删除临时文件
            tempFile.delete();
            
            return commentWords;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<AnalysisVO.CommentWord> parseJsonOutput(String jsonOutput) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return Arrays.asList(mapper.readValue(jsonOutput, AnalysisVO.CommentWord[].class));
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
} 
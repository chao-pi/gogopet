package com.backend.service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class MLService {
    
    public Map<String, Object> analyzeImage(String imagePath) {
        try {
            // 构建命令
            ProcessBuilder processBuilder = new ProcessBuilder(
                "python",
                "ml_service/predict_model.py",
                imagePath
            );
            
            // 设置工作目录
            processBuilder.directory(new File(System.getProperty("user.dir")));
            
            // 启动进程
            Process process = processBuilder.start();
            
            // 读取输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            String lastLine = null;
            
            // 读取所有输出，只保留最后一行
            while ((line = reader.readLine()) != null) {
                lastLine = line;
            }
            
            // 等待进程完成
            int exitCode = process.waitFor();
            
            if (exitCode != 0) {
                throw new RuntimeException("Python script failed with exit code: " + exitCode);
            }
            
            if (lastLine == null || !lastLine.contains(":")) {
                throw new RuntimeException("Invalid output format from Python script: No valid prediction result found");
            }
            
            // 解析结果
            String[] parts = lastLine.split(":");
            if (parts.length != 2) {
                throw new RuntimeException("Invalid output format from Python script: Expected 'emotion:confidence'");
            }
            
            String emotion = parts[0];
            double confidence = Double.parseDouble(parts[1]);
            
            Map<String, Object> result = new HashMap<>();
            result.put("emotion", emotion);
            result.put("confidence", confidence);
            
            return result;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to analyze image: " + e.getMessage(), e);
        }
    }
} 
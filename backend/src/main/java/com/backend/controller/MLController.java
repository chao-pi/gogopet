package com.backend.controller;

import com.backend.common.Result;
import com.backend.service.MLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/ml")
public class MLController {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private MLService mlService;

    @PostMapping("/analyze")
    public Result<Map<String, Object>> analyzeImage(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.error("请选择要上传的图片");
        }

        try {
            // 确保上传目录存在并设置权限
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                if (!created) {
                    return Result.error("无法创建上传目录");
                }
            }

            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Result.error("只支持上传图片文件");
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                return Result.error("文件名不能为空");
            }
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;
            Path filePath = Paths.get(UPLOAD_DIR + filename);

            // 保存文件
            try {
                Files.copy(file.getInputStream(), filePath);
            } catch (IOException e) {
                return Result.error("文件保存失败: " + e.getMessage());
            }

            // 调用ML服务分析图片
            try {
                Map<String, Object> result = mlService.analyzeImage(filePath.toString());
                return Result.success(result);
            } catch (Exception e) {
                return Result.error("图片分析失败: " + e.getMessage());
            } finally {
                // 删除临时文件
                try {
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    System.err.println("临时文件删除失败: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("图片处理失败: " + e.getMessage());
        }
    }
} 
package com.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.url-prefix}")
    private String uploadUrlPrefix;

    /**
     * 上传用户头像和宠物图片
     */
    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") String userId) {
        try {
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;

            // 创建上传目录
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 保存文件
            File destFile = new File(uploadPath + File.separator + filename);
            file.transferTo(destFile);

            // 生成图片URL
            String imageUrl = uploadUrlPrefix + "/" + filename;
            
            // 返回图片URL
            Map<String, String> response = new HashMap<>();
            response.put("imageUrl", imageUrl);
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("图片上传失败: " + e.getMessage());
        }
    }

    /**
     * 上传帖子图片
     */
    @PostMapping("/post-image")
    public ResponseEntity<?> uploadPostImage(
            @RequestParam("file") MultipartFile file) {
        try {
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;

            // 创建帖子图片上传目录
            String postImagePath = uploadPath + File.separator + "posts";
            File uploadDir = new File(postImagePath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 保存文件
            File destFile = new File(postImagePath + File.separator + filename);
            file.transferTo(destFile);

            // 生成图片URL
            String imageUrl = uploadUrlPrefix + "/posts/" + filename;
            
            // 返回图片URL
            Map<String, String> response = new HashMap<>();
            response.put("imageUrl", imageUrl);
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("帖子图片上传失败: " + e.getMessage());
        }
    }
} 
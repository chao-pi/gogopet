package com.backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface PostImageService {
    /**
     * 保存图片
     */
    String saveImage(MultipartFile file, String imageId, String userId);
} 
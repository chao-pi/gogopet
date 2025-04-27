package com.backend.service;

import com.backend.model.dto.PictureDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片服务接口
 */
public interface PictureService {
    /**
     * 上传头像
     * @param file 图片文件
     * @param userId 用户ID
     * @return 图片信息
     */
    PictureDTO uploadAvatar(MultipartFile file, String userId);
} 
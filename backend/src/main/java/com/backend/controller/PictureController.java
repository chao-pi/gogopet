package com.backend.controller;

import com.backend.model.dto.PictureDTO;
import com.backend.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片控制器
 * 处理图片上传等请求
 */
@RestController
@RequestMapping("/api/picture")
public class PictureController {

    @Autowired
    private PictureService pictureService;

    /**
     * 上传头像
     * @param file 图片文件
     * @param userId 用户ID
     * @return 图片信息
     */
    @PostMapping("/avatar")
    public ResponseEntity<PictureDTO> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") String userId) {
        PictureDTO pictureDTO = pictureService.uploadAvatar(file, userId);
        return ResponseEntity.ok(pictureDTO);
    }
} 
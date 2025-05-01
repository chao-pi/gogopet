package com.backend.service.impl;

import com.backend.mapper.PostImageMapper;
import com.backend.model.entity.PostImage;
import com.backend.service.PostImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
public class PostImageServiceImpl implements PostImageService {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.url}")
    private String uploadUrl;

    @Autowired
    private PostImageMapper postImageMapper;

    @Override
    public String saveImage(MultipartFile file, String imageId, String userId) {
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
            String imageUrl = uploadUrl + "/" + filename;

            // 保存图片信息到数据库
            PostImage postImage = new PostImage();
            postImage.setImageId(imageId);
            postImage.setImageUrl(imageUrl);
            postImage.setCreateTime(new Date());
            postImageMapper.insert(postImage);

            return imageUrl;
        } catch (IOException e) {
            throw new RuntimeException("图片上传失败", e);
        }
    }
} 
package com.backend.service.impl;

import com.backend.mapper.PictureMapper;
import com.backend.mapper.UserMapper;
import com.backend.model.dto.PictureDTO;
import com.backend.model.entity.Picture;
import com.backend.model.entity.User;
import com.backend.service.PictureService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class PictureServiceImpl implements PictureService {

    @Autowired
    private PictureMapper pictureMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.url-prefix}")
    private String urlPrefix;

    @Override
    @Transactional
    public PictureDTO uploadAvatar(MultipartFile file, String userId) {
        try {
            // 检查用户是否存在
            User user = userMapper.selectById(userId);
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + extension;

            // 获取项目根目录
            Resource resource = resourceLoader.getResource("classpath:");
            File projectRoot = resource.getFile().getParentFile().getParentFile();
            
            // 构建上传目录的绝对路径
            Path uploadDirPath = Paths.get(projectRoot.getAbsolutePath(), uploadPath, "avatars");
            File uploadDir = uploadDirPath.toFile();

            // 确保上传目录存在
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                if (!created) {
                    throw new RuntimeException("无法创建上传目录: " + uploadDir.getAbsolutePath());
                }
            }

            // 保存文件
            File destFile = new File(uploadDir, fileName);
            file.transferTo(destFile);

            // 创建图片记录
            Picture picture = new Picture();
            picture.setPictureId(UUID.randomUUID().toString().replace("-", "").substring(0, 18));
            picture.setPictureUsage("A"); // A-头像
            picture.setPictureUrl(urlPrefix + "/avatars/" + fileName);
            picture.setUserId(userId);

            // 保存图片记录
            pictureMapper.insert(picture);

            // 更新用户头像
            user.setPictureId(picture.getPictureId());
            userMapper.update(user);

            // 返回图片信息
            PictureDTO pictureDTO = new PictureDTO();
            BeanUtils.copyProperties(picture, pictureDTO);
            return pictureDTO;

        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }
} 
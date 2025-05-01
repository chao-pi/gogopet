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
import java.time.LocalDateTime;
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
            System.out.println("开始上传头像，用户ID: " + userId);
            
            // 检查用户是否存在
            User user = userMapper.selectById(userId);
            if (user == null) {
                System.out.println("用户不存在: " + userId);
                throw new RuntimeException("用户不存在");
            }
            System.out.println("找到用户: " + user.getUserName());

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + extension;
            System.out.println("生成文件名: " + fileName);

            // 获取项目根目录
            Resource resource = resourceLoader.getResource("classpath:");
            File projectRoot = resource.getFile().getParentFile().getParentFile();
            
            // 构建上传目录的绝对路径
            Path uploadDirPath = Paths.get(projectRoot.getAbsolutePath(), uploadPath, "avatars");
            File uploadDir = uploadDirPath.toFile();
            System.out.println("上传目录: " + uploadDir.getAbsolutePath());

            // 确保上传目录存在
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                if (!created) {
                    System.out.println("创建上传目录失败: " + uploadDir.getAbsolutePath());
                    throw new RuntimeException("无法创建上传目录: " + uploadDir.getAbsolutePath());
                }
                System.out.println("创建上传目录成功");
            }

            // 保存文件
            File destFile = new File(uploadDir, fileName);
            file.transferTo(destFile);
            System.out.println("文件保存成功: " + destFile.getAbsolutePath());

            // 检查用户是否已有头像
            Picture picture;
            if (user.getPictureId() != null) {
                System.out.println("用户已有头像，ID: " + user.getPictureId());
                // 如果已有头像，更新现有记录
                picture = pictureMapper.selectById(user.getPictureId());
                if (picture != null) {
                    System.out.println("找到现有头像记录");
                    // 删除旧文件
                    String oldFileName = picture.getPictureUrl().substring(picture.getPictureUrl().lastIndexOf("/") + 1);
                    File oldFile = new File(uploadDir, oldFileName);
                    if (oldFile.exists()) {
                        oldFile.delete();
                        System.out.println("删除旧文件: " + oldFile.getAbsolutePath());
                    }
                    // 更新图片信息
                    picture.setPictureUrl(urlPrefix + "/avatars/" + fileName);
                    picture.setUploadTime(LocalDateTime.now());
                    System.out.println("更新图片记录");
                    pictureMapper.update(picture);
                } else {
                    System.out.println("未找到现有头像记录，创建新记录");
                    // 如果找不到记录，创建新记录
                    picture = createNewPicture(fileName, userId);
                    pictureMapper.insert(picture);
                }
            } else {
                System.out.println("用户没有头像，创建新记录");
                // 如果没有头像，创建新记录
                picture = createNewPicture(fileName, userId);
                pictureMapper.insert(picture);
            }

            // 更新用户头像
            user.setPictureId(picture.getPictureId());
            userMapper.update(user);
            System.out.println("更新用户头像关联");

            // 返回图片信息
            PictureDTO pictureDTO = new PictureDTO();
            BeanUtils.copyProperties(picture, pictureDTO);
            System.out.println("头像上传完成");
            return pictureDTO;

        } catch (IOException e) {
            System.out.println("文件上传失败: " + e.getMessage());
            throw new RuntimeException("文件上传失败", e);
        }
    }

    private Picture createNewPicture(String fileName, String userId) {
        Picture picture = new Picture();
        picture.setPictureId(UUID.randomUUID().toString().replace("-", "").substring(0, 18));
        picture.setPictureUsage("A"); // A-头像
        picture.setPictureUrl(urlPrefix + "/avatars/" + fileName);
        picture.setUserId(userId);
        picture.setUploadTime(LocalDateTime.now());
        return picture;
    }
} 
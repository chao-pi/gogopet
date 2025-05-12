package com.backend.service.impl;

import com.backend.mapper.PetMapper;
import com.backend.mapper.PictureMapper;
import com.backend.model.dto.PetDTO;
import com.backend.model.entity.Pet;
import com.backend.model.entity.Picture;
import com.backend.service.PetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 宠物服务实现类
 */
@Service
public class PetServiceImpl extends ServiceImpl<PetMapper, Pet> implements PetService {

    private final PictureMapper pictureMapper;
    private final ResourceLoader resourceLoader;

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.url-prefix}")
    private String urlPrefix;

    @Autowired
    public PetServiceImpl(PictureMapper pictureMapper, ResourceLoader resourceLoader) {
        this.pictureMapper = pictureMapper;
        this.resourceLoader = resourceLoader;
    }

    @Override
    @Transactional
    public PetDTO addPet(PetDTO petDTO) {
        System.out.println("开始添加宠物");

        // 创建宠物实体
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);

        // 设置宠物ID和时间
        String petId = UUID.randomUUID().toString().replace("-", "").substring(0, 18);
        pet.setPetId(petId);
        pet.setCreateTime(LocalDateTime.now());
        pet.setUpdateTime(LocalDateTime.now());

        // 保存宠物信息
        save(pet);
        System.out.println("宠物信息保存成功，ID: " + petId);

        // 返回宠物信息
        petDTO.setPetId(petId);
        return petDTO;
    }

    @Override
    @Transactional
    public PetDTO updatePet(PetDTO petDTO) {
        System.out.println("开始更新宠物信息，ID: " + petDTO.getPetId());

        // 检查宠物是否存在
        Pet pet = getById(petDTO.getPetId());
        if (pet == null) {
            System.out.println("宠物不存在，ID: " + petDTO.getPetId());
            throw new RuntimeException("宠物不存在");
        }

        // 更新宠物信息
        BeanUtils.copyProperties(petDTO, pet);
        pet.setUpdateTime(LocalDateTime.now());
        updateById(pet);
        System.out.println("宠物信息更新成功");

        return petDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePet(String petId) {
        // 先删除相关的图片记录
        pictureMapper.deleteByPetId(petId);
        // 再删除宠物记录
        return removeById(petId);
    }

    @Override
    public PetDTO getPetById(String petId) {
        System.out.println("查询宠物信息，ID: " + petId);

        // 查询宠物信息
        Pet pet = getById(petId);
        if (pet == null) {
            System.out.println("宠物不存在，ID: " + petId);
            throw new RuntimeException("宠物不存在");
        }

        // 转换为DTO
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        return petDTO;
    }

    @Override
    public List<PetDTO> getPetsByUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("用户ID不能为空");
        }

        System.out.println("开始查询用户的宠物列表，用户ID: " + userId);

        try {
            // 查询用户的所有宠物
            List<Pet> pets = baseMapper.selectByUserId(userId);
            if (pets == null || pets.isEmpty()) {
                System.out.println("未找到用户的宠物，用户ID: " + userId);
                return new ArrayList<>();
            }

            List<PetDTO> petDTOs = new ArrayList<>();
            System.out.println("找到 " + pets.size() + " 个宠物，开始处理图片信息");

            // 转换为DTO列表
            for (Pet pet : pets) {
                try {
                    PetDTO petDTO = new PetDTO();
                    BeanUtils.copyProperties(pet, petDTO);

                    // 查询宠物的图片
                    Picture picture = pictureMapper.selectByPetId(pet.getPetId());
                    if (picture != null) {
                        String pictureUrl = picture.getPictureUrl();
                        if (pictureUrl != null && !pictureUrl.trim().isEmpty()) {
                            petDTO.setAvatarUrl(pictureUrl);
                            System.out.println("设置宠物图片URL成功，宠物ID: " + pet.getPetId() + ", URL: " + pictureUrl);
                        } else {
                            System.out.println("宠物图片URL为空，宠物ID: " + pet.getPetId());
                        }
                    } else {
                        System.out.println("未找到宠物图片，宠物ID: " + pet.getPetId());
                    }

                    petDTOs.add(petDTO);
                } catch (Exception e) {
                    System.err.println("处理宠物信息时发生错误，宠物ID: " + pet.getPetId() + ", 错误: " + e.getMessage());
                    // 继续处理下一个宠物，不中断整个流程
                }
            }

            System.out.println("宠物列表处理完成，共处理 " + petDTOs.size() + " 个宠物");
            return petDTOs;
        } catch (Exception e) {
            System.err.println("查询宠物列表时发生错误，用户ID: " + userId + ", 错误: " + e.getMessage());
            throw new RuntimeException("获取宠物列表失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public PetDTO uploadPetPhoto(MultipartFile file, String petId) {
        try {
            System.out.println("开始上传宠物照片，宠物ID: " + petId);

            // 检查宠物是否存在
            Pet pet = getById(petId);
            if (pet == null) {
                System.out.println("宠物不存在，ID: " + petId);
                throw new RuntimeException("宠物不存在");
            }

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + extension;
            System.out.println("生成文件名: " + fileName);

            // 获取项目根目录
            Resource resource = resourceLoader.getResource("classpath:");
            File projectRoot = resource.getFile().getParentFile().getParentFile();

            // 构建上传目录的绝对路径
            Path uploadDirPath = Paths.get(projectRoot.getAbsolutePath(), uploadPath, "pets");
            File uploadDir = uploadDirPath.toFile();
            System.out.println("上传目录: " + uploadDir.getAbsolutePath());

            // 确保上传目录存在
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                if (!created) {
                    System.out.println("创建上传目录失败: " + uploadDir.getAbsolutePath());
                    throw new RuntimeException("无法创建上传目录");
                }
                System.out.println("创建上传目录成功");
            }

            // 检查宠物是否已有图片
            Picture existingPicture = pictureMapper.selectByPetId(petId);
            String pictureUrl = urlPrefix + "/pets/" + fileName;
            
            // 如果已有图片，先删除旧文件
            if (existingPicture != null) {
                String oldFileName = existingPicture.getPictureUrl().substring(existingPicture.getPictureUrl().lastIndexOf("/") + 1);
                File oldFile = new File(uploadDir, oldFileName);
                if (oldFile.exists()) {
                    boolean deleted = oldFile.delete();
                    if (deleted) {
                        System.out.println("删除旧图片文件成功: " + oldFile.getAbsolutePath());
                    } else {
                        System.out.println("删除旧图片文件失败: " + oldFile.getAbsolutePath());
                    }
                }
            }

            // 保存新文件
            File destFile = new File(uploadDir, fileName);
            file.transferTo(destFile);
            System.out.println("文件保存成功: " + destFile.getAbsolutePath());
            
            if (existingPicture != null) {
                // 更新现有图片记录
                existingPicture.setPictureUrl(pictureUrl);
                existingPicture.setUploadTime(LocalDateTime.now());
                pictureMapper.update(existingPicture);
                System.out.println("更新图片记录成功");
            } else {
                // 创建新的图片记录
                Picture picture = new Picture();
                picture.setPictureId(UUID.randomUUID().toString().replace("-", "").substring(0, 18));
                picture.setPictureUsage("P"); // P-宠物照片
                picture.setPictureUrl(pictureUrl);
                picture.setUserId(pet.getUserId());
                picture.setPetId(petId);
                picture.setUploadTime(LocalDateTime.now());
                pictureMapper.insert(picture);
                System.out.println("创建图片记录成功");
            }

            // 返回宠物信息
            PetDTO petDTO = new PetDTO();
            BeanUtils.copyProperties(pet, petDTO);
            petDTO.setAvatarUrl(pictureUrl);
            return petDTO;

        } catch (IOException e) {
            System.out.println("文件上传失败: " + e.getMessage());
            throw new RuntimeException("文件上传失败", e);
        }
    }
} 
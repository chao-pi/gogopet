package com.backend.service.impl;

import com.backend.mapper.PetMapper;
import com.backend.mapper.PictureMapper;
import com.backend.model.dto.PetDTO;
import com.backend.model.entity.Pet;
import com.backend.model.entity.Picture;
import com.backend.service.PetService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 宠物服务实现类
 */
@Service
public class PetServiceImpl implements PetService {

    @Autowired
    private PetMapper petMapper;

    @Autowired
    private PictureMapper pictureMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.url-prefix}")
    private String urlPrefix;

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
        pet.setCreateTime(new Date());
        pet.setUpdateTime(new Date());

        // 保存宠物信息
        petMapper.insert(pet);
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
        Pet pet = petMapper.selectById(petDTO.getPetId());
        if (pet == null) {
            System.out.println("宠物不存在，ID: " + petDTO.getPetId());
            throw new RuntimeException("宠物不存在");
        }

        // 更新宠物信息
        BeanUtils.copyProperties(petDTO, pet);
        pet.setUpdateTime(new Date());
        petMapper.update(pet);
        System.out.println("宠物信息更新成功");

        return petDTO;
    }

    @Override
    @Transactional
    public void deletePet(String petId) {
        System.out.println("开始删除宠物，ID: " + petId);

        // 检查宠物是否存在
        Pet pet = petMapper.selectById(petId);
        if (pet == null) {
            System.out.println("宠物不存在，ID: " + petId);
            throw new RuntimeException("宠物不存在");
        }

        // 删除宠物信息
        petMapper.deleteById(petId);
        System.out.println("宠物删除成功");
    }

    @Override
    public PetDTO getPetById(String petId) {
        System.out.println("查询宠物信息，ID: " + petId);

        // 查询宠物信息
        Pet pet = petMapper.selectById(petId);
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
        System.out.println("查询用户的宠物列表，用户ID: " + userId);

        // 查询用户的所有宠物
        List<Pet> pets = petMapper.selectByUserId(userId);
        List<PetDTO> petDTOs = new ArrayList<>();

        // 转换为DTO列表
        for (Pet pet : pets) {
            PetDTO petDTO = new PetDTO();
            BeanUtils.copyProperties(pet, petDTO);
            petDTOs.add(petDTO);
        }

        System.out.println("查询到 " + petDTOs.size() + " 个宠物");
        return petDTOs;
    }

    @Override
    @Transactional
    public PetDTO uploadPetPhoto(MultipartFile file, String petId) {
        try {
            System.out.println("开始上传宠物照片，宠物ID: " + petId);

            // 检查宠物是否存在
            Pet pet = petMapper.selectById(petId);
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

            // 保存文件
            File destFile = new File(uploadDir, fileName);
            file.transferTo(destFile);
            System.out.println("文件保存成功: " + destFile.getAbsolutePath());

            // 创建图片记录
            Picture picture = new Picture();
            picture.setPictureId(UUID.randomUUID().toString().replace("-", "").substring(0, 18));
            picture.setPictureUsage("P"); // P-宠物照片
            picture.setPictureUrl(urlPrefix + "/pets/" + fileName);
            picture.setUserId(pet.getUserId());
            picture.setUploadTime(new Date());

            // 保存图片记录
            pictureMapper.insert(picture);
            System.out.println("图片记录保存成功");

            // 返回宠物信息
            PetDTO petDTO = new PetDTO();
            BeanUtils.copyProperties(pet, petDTO);
            return petDTO;

        } catch (IOException e) {
            System.out.println("文件上传失败: " + e.getMessage());
            throw new RuntimeException("文件上传失败", e);
        }
    }
} 
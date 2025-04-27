package com.backend.service;

import com.backend.model.dto.PetDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 宠物服务接口
 */
public interface PetService {
    /**
     * 添加宠物
     * @param petDTO 宠物信息
     * @return 添加后的宠物信息
     */
    PetDTO addPet(PetDTO petDTO);

    /**
     * 更新宠物信息
     * @param petDTO 宠物信息
     * @return 更新后的宠物信息
     */
    PetDTO updatePet(PetDTO petDTO);

    /**
     * 删除宠物
     * @param petId 宠物ID
     */
    void deletePet(String petId);

    /**
     * 获取宠物信息
     * @param petId 宠物ID
     * @return 宠物信息
     */
    PetDTO getPetById(String petId);

    /**
     * 获取用户的所有宠物
     * @param userId 用户ID
     * @return 宠物列表
     */
    List<PetDTO> getPetsByUserId(String userId);

    /**
     * 上传宠物照片
     * @param file 图片文件
     * @param petId 宠物ID
     * @return 图片信息
     */
    PetDTO uploadPetPhoto(MultipartFile file, String petId);
} 
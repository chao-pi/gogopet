package com.backend.controller;

import com.backend.model.dto.PetDTO;
import com.backend.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 宠物控制器
 */
@RestController
@RequestMapping("/api/pet")
public class PetController {

    @Autowired
    private PetService petService;

    /**
     * 添加宠物
     * @param petDTO 宠物信息
     * @return 添加后的宠物信息
     */
    @PostMapping("/add")
    public ResponseEntity<PetDTO> addPet(@RequestBody PetDTO petDTO) {
        PetDTO result = petService.addPet(petDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * 更新宠物信息
     * @param petDTO 宠物信息
     * @return 更新后的宠物信息
     */
    @PutMapping("/update")
    public ResponseEntity<PetDTO> updatePet(@RequestBody PetDTO petDTO) {
        PetDTO result = petService.updatePet(petDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * 删除宠物
     * @param petId 宠物ID
     * @return 操作结果
     */
    @DeleteMapping("/delete/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable String petId) {
        petService.deletePet(petId);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取宠物信息
     * @param petId 宠物ID
     * @return 宠物信息
     */
    @GetMapping("/{petId}")
    public ResponseEntity<PetDTO> getPetById(@PathVariable String petId) {
        PetDTO result = petService.getPetById(petId);
        return ResponseEntity.ok(result);
    }

    /**
     * 获取用户的所有宠物
     * @return 宠物列表
     */
    @GetMapping("/list")
    public ResponseEntity<List<PetDTO>> getPets() {
        // TODO: 从token中获取用户ID
        String userId = "123456789012345678"; // 临时使用固定用户ID
        List<PetDTO> result = petService.getPetsByUserId(userId);
        return ResponseEntity.ok(result);
    }

    /**
     * 上传宠物照片
     * @param file 图片文件
     * @param petId 宠物ID
     * @return 宠物信息
     */
    @PostMapping("/upload")
    public ResponseEntity<PetDTO> uploadPetPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("petId") String petId) {
        PetDTO result = petService.uploadPetPhoto(file, petId);
        return ResponseEntity.ok(result);
    }
} 
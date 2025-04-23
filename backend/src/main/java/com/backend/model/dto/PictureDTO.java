package com.backend.model.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 图片数据传输对象
 */
@Data
public class PictureDTO {
    /**
     * 图片ID，唯一标识符
     */
    @Size(min = 18, max = 18, message = "图片ID长度必须为18个字符")
    private String pictureId;

    /**
     * 图片用途：A-头像，P-宠物照片，O-其他照片
     */
    @Size(max = 63, message = "图片用途长度不能超过63个字符")
    private String pictureUsage;

    /**
     * 图片URL，存储图片的服务路径
     */
    @Size(max = 255, message = "图片URL长度不能超过255个字符")
    private String pictureUrl;

    /**
     * 用户ID，关联上传用户
     */
    @Size(min = 18, max = 18, message = "用户ID长度必须为18个字符")
    private String userId;
} 
package com.backend.model.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 通知数据传输对象
 */
@Data
public class NotificationDTO {
    /**
     * 通知ID，唯一标识符
     */
    @Size(min = 18, max = 18, message = "通知ID长度必须为18个字符")
    private String notificationId;

    /**
     * 用户ID，关联接收通知的用户
     */
    @Size(min = 18, max = 18, message = "用户ID长度必须为18个字符")
    private String userId;

    /**
     * 通知类型：O-订单通知，S-系统通知
     */
    @Size(max = 63, message = "通知类型长度不能超过63个字符")
    private String notificationType;

    /**
     * 通知内容
     */
    @Size(max = 1000, message = "通知内容长度不能超过1000个字符")
    private String notificationContent;

    /**
     * 是否已读：false-未读，true-已读
     */
    private Boolean isRead;
} 
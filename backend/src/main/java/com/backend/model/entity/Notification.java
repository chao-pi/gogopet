package com.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.util.Date;

/**
 * 通知实体类
 * 对应数据库表 t_notification
 */
@Data
@TableName("t_notification")
public class Notification {
    /**
     * 通知ID，唯一标识符
     */
    @TableId("notification_id")
    private String notificationId;

    /**
     * 用户ID，关联接收通知的用户
     */
    @TableField("user_id")
    private String userId;

    /**
     * 通知类型：O-订单通知，S-系统通知
     */
    @TableField("notification_type")
    private String notificationType;

    /**
     * 通知内容
     */
    @TableField("notification_content")
    private String notificationContent;

    /**
     * 是否已读：false-未读，true-已读
     */
    @TableField("is_read")
    private Boolean isRead;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
} 
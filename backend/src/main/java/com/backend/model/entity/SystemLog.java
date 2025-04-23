package com.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.util.Date;

/**
 * 系统日志实体类
 * 对应数据库表 t_system_log
 */
@Data
@TableName("t_system_log")
public class SystemLog {
    /**
     * 日志ID，唯一标识符
     */
    @TableId("log_id")
    private String logId;

    /**
     * 用户ID，关联操作用户
     */
    @TableField("user_id")
    private String userId;

    /**
     * 操作类型，如：登录、修改信息等
     */
    @TableField("operation_type")
    private String operationType;

    /**
     * 操作内容，详细记录操作信息
     */
    @TableField("operation_content")
    private String operationContent;

    /**
     * 操作时间
     */
    @TableField("operation_time")
    private Date operationTime;
} 
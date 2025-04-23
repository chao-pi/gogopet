package com.backend.model.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 系统日志数据传输对象
 */
@Data
public class SystemLogDTO {
    /**
     * 日志ID，唯一标识符
     */
    @Size(min = 18, max = 18, message = "日志ID长度必须为18个字符")
    private String logId;

    /**
     * 用户ID，关联操作用户
     */
    @Size(min = 18, max = 18, message = "用户ID长度必须为18个字符")
    private String userId;

    /**
     * 操作类型，如：登录、修改信息等
     */
    @Size(max = 63, message = "操作类型长度不能超过63个字符")
    private String operationType;

    /**
     * 操作内容，详细记录操作信息
     */
    @Size(max = 1000, message = "操作内容长度不能超过1000个字符")
    private String operationContent;
} 
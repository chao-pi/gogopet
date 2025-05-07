package com.backend.common.exception;

/**
 * 未授权异常
 * 当用户尝试访问未授权的资源时抛出
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
    
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
} 
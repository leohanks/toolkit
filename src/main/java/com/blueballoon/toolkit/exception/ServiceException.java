package com.blueballoon.toolkit.exception;

import org.springframework.http.HttpStatus;

/**
 * @author hanzhen
 * @description：
 * @date 2019/11/14
 */
public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}

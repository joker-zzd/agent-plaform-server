package com.agent.platform.api.advice;

import com.agent.platform.api.dto.ApiErrorResponse;
import com.agent.platform.core.exception.AgentResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/**
 * API 全局异常处理器。
 *
 * <p>负责把 Core 异常和参数校验异常转换为稳定的 HTTP 错误响应。</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 将平台资源不存在异常转换为 HTTP 404。
     */
    @ExceptionHandler(AgentResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(
            AgentResourceNotFoundException exception
    ) {
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code("RESOURCE_NOT_FOUND")
                .message(exception.getMessage())
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * 将请求字段校验异常转换为 HTTP 400。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException exception
    ) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fieldError -> fieldError.getDefaultMessage())
                .orElse("请求参数不合法");

        ApiErrorResponse response = ApiErrorResponse.builder()
                .code("INVALID_REQUEST")
                .message(message)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.badRequest().body(response);
    }
}

package com.agent.platform.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * API 统一错误响应。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {

    /** 稳定的业务错误码。 */
    private String code;

    /** 面向调用方的错误说明。 */
    private String message;

    /** 错误发生时间。 */
    private Instant timestamp;
}

package com.agent.platform.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * API 统一错误响应。
 */
@Schema(description = "统一 API 错误响应")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {

    /** 稳定的业务错误码。 */
    @Schema(description = "稳定的业务错误码", example = "RESOURCE_NOT_FOUND")
    private String code;

    /** 面向调用方的错误说明。 */
    @Schema(description = "面向调用方的错误说明", example = "Agent 不存在，ID：unknown-agent")
    private String message;

    /** 错误发生时间。 */
    @Schema(description = "错误发生时间", example = "2026-09-15T10:30:00Z")
    private Instant timestamp;
}

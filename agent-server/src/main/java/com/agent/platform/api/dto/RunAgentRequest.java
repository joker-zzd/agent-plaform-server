package com.agent.platform.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * HTTP 层运行 Agent 的请求 DTO。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunAgentRequest {

    /** 会话唯一标识。 */
    @NotBlank(message = "sessionId 不能为空")
    private String sessionId;

    /** 当前用户唯一标识。 */
    @NotBlank(message = "userId 不能为空")
    private String userId;

    /** 用户输入的消息正文。 */
    @NotBlank(message = "message 不能为空")
    private String message;

    /** 可选的动态上下文变量。 */
    private Map<String, Object> variables;
}

package com.agent.platform.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * HTTP 层运行 Agent 的请求 DTO。
 */
@Schema(description = "同步运行 Agent 的请求参数")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunAgentRequest {

    @Schema(description = "会话唯一标识；连续对话应使用相同 ID", example = "session-001")
    @NotBlank(message = "sessionId 不能为空")
    private String sessionId;

    @Schema(description = "当前调用用户的业务唯一标识", example = "user-001")
    @NotBlank(message = "userId 不能为空")
    private String userId;

    @Schema(description = "发送给 Agent 的用户消息", example = "介绍一下 Agent Runtime")
    @NotBlank(message = "message 不能为空")
    private String message;

    @Schema(
            description = "可选的动态上下文变量；当前版本接收但尚未参与 Prompt 模板渲染",
            example = "{\"language\": \"zh-CN\", \"department\": \"研发部\"}"
    )
    private Map<String, Object> variables;
}

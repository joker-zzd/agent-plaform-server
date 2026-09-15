package com.agent.platform.api.dto;

import com.agent.platform.core.agent.runtime.AgentResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * HTTP 层运行 Agent 的响应 DTO。
 */
@Schema(description = "Agent 同步执行结果")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunAgentResponse {

    /** 本次执行唯一标识。 */
    @Schema(description = "本次执行唯一标识", example = "9bb60474-12a0-4cdb-aeb5-f017f0356173")
    private String executionId;

    /** 实际执行的 Agent ID。 */
    @Schema(description = "实际执行的 Agent ID", example = "general-agent")
    private String agentId;

    /** 当前会话 ID。 */
    @Schema(description = "当前会话 ID", example = "session-001")
    private String sessionId;

    /** 本次执行选择的平台模型 ID。 */
    @Schema(description = "平台模型路由标识，不一定等于模型厂商名称", example = "default-model")
    private String modelId;

    /** Agent 最终输出内容。 */
    @Schema(
            description = "Agent 最终生成的文本内容",
            example = "Agent Runtime 是负责组织模型、Prompt 和工具调用的运行时组件。"
    )
    private String content;

    /**
     * 将 Core 结果转换为对外响应，避免直接暴露领域对象。
     */
    public static RunAgentResponse from(AgentResult result) {
        return RunAgentResponse.builder()
                .executionId(result.getExecutionId())
                .agentId(result.getAgentId())
                .sessionId(result.getSessionId())
                .modelId(result.getModelId())
                .content(result.getContent())
                .build();
    }
}

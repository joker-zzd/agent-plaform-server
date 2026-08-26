package com.agent.platform.api.dto;

import com.agent.platform.core.agent.runtime.AgentResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * HTTP 层运行 Agent 的响应 DTO。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunAgentResponse {

    /** 本次执行唯一标识。 */
    private String executionId;

    /** 实际执行的 Agent ID。 */
    private String agentId;

    /** 当前会话 ID。 */
    private String sessionId;

    /** 本次执行选择的平台模型 ID。 */
    private String modelId;

    /** Agent 最终输出内容。 */
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

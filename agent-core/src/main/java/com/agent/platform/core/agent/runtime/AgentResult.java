package com.agent.platform.core.agent.runtime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Agent Runtime 的统一执行结果。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentResult {

    /** 本次执行唯一标识，用于后续日志、审计和执行记录查询。 */
    private String executionId;

    /** 实际执行的 Agent ID。 */
    private String agentId;

    /** 当前会话 ID。 */
    private String sessionId;

    /** 本次执行选择的平台模型 ID。 */
    private String modelId;

    /** Agent 最终返回的文本内容。 */
    private String content;
}

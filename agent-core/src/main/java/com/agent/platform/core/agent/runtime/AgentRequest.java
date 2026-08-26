package com.agent.platform.core.agent.runtime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Agent Runtime 的统一执行请求。
 *
 * <p>该对象属于 Core，不直接使用 HTTP 请求对象，避免 API 层侵入运行时。</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentRequest {

    /** 本次需要执行的 Agent ID。 */
    private String agentId;

    /** 当前会话 ID。 */
    private String sessionId;

    /** 当前用户 ID，后续用于权限和审计。 */
    private String userId;

    /** 用户输入的消息正文。 */
    private String message;

    /** 本次请求携带的动态上下文变量。 */
    private Map<String, Object> variables;
}

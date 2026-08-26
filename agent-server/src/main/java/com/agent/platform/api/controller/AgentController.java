package com.agent.platform.api.controller;

import com.agent.platform.api.dto.RunAgentRequest;
import com.agent.platform.api.dto.RunAgentResponse;
import com.agent.platform.core.agent.runtime.AgentRequest;
import com.agent.platform.core.agent.runtime.AgentResult;
import com.agent.platform.core.agent.runtime.AgentRuntime;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Agent 对外 HTTP 接口。
 *
 * <p>Controller 只负责协议对象转换，不拼接 Prompt，也不直接使用 ChatClient。</p>
 */
@RestController
@RequestMapping("/api/v1/agents")
@RequiredArgsConstructor
public class AgentController {

    private final AgentRuntime agentRuntime;

    /**
     * 执行指定 Agent。
     *
     * @param agentId Agent 唯一标识
     * @param request HTTP 请求
     * @return Agent 执行结果
     */
    @PostMapping("/{agentId}/runs")
    public RunAgentResponse run(
            @PathVariable String agentId,
            @Valid @RequestBody RunAgentRequest request
    ) {
        AgentRequest agentRequest = AgentRequest.builder()
                .agentId(agentId)
                .sessionId(request.getSessionId())
                .userId(request.getUserId())
                .message(request.getMessage())
                .variables(request.getVariables() == null ? Map.of() : request.getVariables())
                .build();

        AgentResult result = agentRuntime.execute(agentRequest);
        return RunAgentResponse.from(result);
    }
}

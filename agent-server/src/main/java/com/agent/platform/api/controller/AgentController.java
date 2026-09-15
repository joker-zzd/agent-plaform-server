package com.agent.platform.api.controller;

import com.agent.platform.api.dto.ApiErrorResponse;
import com.agent.platform.api.dto.RunAgentRequest;
import com.agent.platform.api.dto.RunAgentResponse;
import com.agent.platform.core.agent.runtime.AgentRequest;
import com.agent.platform.core.agent.runtime.AgentResult;
import com.agent.platform.core.agent.runtime.AgentRuntime;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Agent Runtime", description = "Agent 同步执行与运行结果接口")
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
    @Operation(
            operationId = "runAgent",
            summary = "执行 Agent",
            description = """
                    根据 agentId 加载 Agent 定义和 System Prompt，同步调用模型并返回文本结果。
                    当前版本尚未使用多轮记忆、动态变量、Tool Calling 和流式输出。
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Agent 执行成功",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RunAgentResponse.class),
                            examples = @ExampleObject(
                                    name = "执行成功",
                                    value = """
                                            {
                                              "executionId": "9bb60474-12a0-4cdb-aeb5-f017f0356173",
                                              "agentId": "general-agent",
                                              "sessionId": "session-001",
                                              "modelId": "default-model",
                                              "content": "Agent Runtime 是负责组织 Agent 执行流程的运行时组件。"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "请求参数不合法",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agent 或关联的 Prompt 不存在",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @PostMapping("/{agentId}/runs")
    public RunAgentResponse run(
            @Parameter(
                    description = "需要执行的 Agent 唯一标识",
                    example = "general-agent",
                    required = true
            )
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

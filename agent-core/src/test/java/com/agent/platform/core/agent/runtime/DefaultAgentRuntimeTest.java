package com.agent.platform.core.agent.runtime;

import com.agent.platform.core.agent.definition.AgentDefinition;
import com.agent.platform.core.agent.definition.AgentDefinitionProvider;
import com.agent.platform.core.model.ModelGateway;
import com.agent.platform.core.model.ModelResult;
import com.agent.platform.core.prompt.PromptDefinition;
import com.agent.platform.core.prompt.PromptProvider;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 默认 Agent Runtime 单元测试。
 *
 * <p>不启动 Spring 容器，验证 Core 可以独立完成执行编排。</p>
 */
class DefaultAgentRuntimeTest {

    /**
     * 验证 Runtime 能根据 Agent 定义加载 Prompt，并把模型结果转换为 Agent 结果。
     */
    @Test
    void shouldExecuteAgentThroughCorePorts() {
        AgentDefinitionProvider agentProvider = agentId -> AgentDefinition.builder()
                .id(agentId)
                .name("测试 Agent")
                .systemPromptId("test-prompt")
                .modelId("test-model")
                .build();

        PromptProvider promptProvider = promptId -> PromptDefinition.builder()
                .id(promptId)
                .systemText("你是一个测试助手。")
                .build();

        ModelGateway modelGateway = request -> ModelResult.builder()
                .content("模型回答：" + request.getUserPrompt())
                .build();

        AgentRuntime runtime = new DefaultAgentRuntime(agentProvider, promptProvider, modelGateway);
        AgentRequest request = AgentRequest.builder()
                .agentId("test-agent")
                .sessionId("session-001")
                .userId("user-001")
                .message("你好")
                .variables(Map.of())
                .build();

        AgentResult result = runtime.execute(request);

        assertThat(result.getExecutionId()).isNotBlank();
        assertThat(result.getAgentId()).isEqualTo("test-agent");
        assertThat(result.getModelId()).isEqualTo("test-model");
        assertThat(result.getContent()).isEqualTo("模型回答：你好");
    }
}

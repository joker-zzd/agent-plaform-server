package com.agent.platform.core.agent.runtime;

import com.agent.platform.core.agent.definition.AgentDefinition;
import com.agent.platform.core.agent.definition.AgentDefinitionProvider;
import com.agent.platform.core.model.ModelGateway;
import com.agent.platform.core.model.ModelRequest;
import com.agent.platform.core.model.ModelResult;
import com.agent.platform.core.prompt.PromptDefinition;
import com.agent.platform.core.prompt.PromptProvider;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * 默认 Agent Runtime 实现。
 *
 * <p>负责组织最基础的 Agent 执行流程，但不感知 Spring AI、HTTP 或存储实现。</p>
 */
@RequiredArgsConstructor
public class DefaultAgentRuntime implements AgentRuntime {

    private final AgentDefinitionProvider agentDefinitionProvider;
    private final PromptProvider promptProvider;
    private final ModelGateway modelGateway;

    /**
     * 按照“加载 Agent、加载 Prompt、调用模型、封装结果”的顺序完成一次执行。
     */
    @Override
    public AgentResult execute(AgentRequest request) {
        AgentDefinition agentDefinition = agentDefinitionProvider.getRequired(request.getAgentId());
        PromptDefinition promptDefinition = loadPrompt(agentDefinition);

        ModelRequest modelRequest = ModelRequest.builder()
                .modelId(agentDefinition.getModelId())
                .systemPrompt(promptDefinition.getSystemText())
                .userPrompt(request.getMessage())
                .build();

        ModelResult modelResult = modelGateway.call(modelRequest);

        return AgentResult.builder()
                .executionId(UUID.randomUUID().toString())
                .agentId(agentDefinition.getId())
                .sessionId(request.getSessionId())
                .modelId(agentDefinition.getModelId())
                .content(modelResult.getContent())
                .build();
    }

    /**
     * 根据 Agent 配置加载固定版本或当前发布版本的 Prompt。
     */
    private PromptDefinition loadPrompt(AgentDefinition agentDefinition) {
        Integer promptVersion = agentDefinition.getPromptVersion();
        if (promptVersion == null) {
            return promptProvider.getPublished(agentDefinition.getSystemPromptId());
        }
        return promptProvider.getRequired(agentDefinition.getSystemPromptId(), promptVersion);
    }
}

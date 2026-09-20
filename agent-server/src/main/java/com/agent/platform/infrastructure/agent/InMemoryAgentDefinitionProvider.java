package com.agent.platform.infrastructure.agent;

import com.agent.platform.core.agent.definition.AgentDefinition;
import com.agent.platform.core.agent.definition.AgentDefinitionProvider;
import com.agent.platform.core.exception.AgentResourceNotFoundException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 基于内存的 Agent 定义加载器。
 *
 * <p>当前用于验证通用 Runtime，后续可以替换为配置文件或数据库实现。</p>
 */
@Component
@ConditionalOnProperty(
        prefix = "agent.platform.persistence",
        name = "type",
        havingValue = "memory"
)
public class InMemoryAgentDefinitionProvider implements AgentDefinitionProvider {

    /** 预置两个不同定位的 Agent，用于验证配置与 Runtime 已经解耦。 */
    private final Map<String, AgentDefinition> definitions = Map.of(
            "general-agent",
            AgentDefinition.builder()
                    .id("general-agent")
                    .name("通用助手")
                    .systemPromptId("general-system-prompt")
                    .modelId("default-model")
                    .build(),
            "code-agent",
            AgentDefinition.builder()
                    .id("code-agent")
                    .name("编程助手")
                    .systemPromptId("code-system-prompt")
                    .modelId("default-model")
                    .build()
    );

    /**
     * 从当前内存定义中查询 Agent。
     */
    @Override
    public AgentDefinition getRequired(String agentId) {
        AgentDefinition definition = definitions.get(agentId);
        if (definition == null) {
            throw new AgentResourceNotFoundException("Agent", agentId);
        }
        return definition;
    }
}

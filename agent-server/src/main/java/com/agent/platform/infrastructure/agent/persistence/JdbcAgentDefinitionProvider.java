package com.agent.platform.infrastructure.agent.persistence;

import com.agent.platform.core.agent.definition.AgentDefinition;
import com.agent.platform.core.agent.definition.AgentDefinitionProvider;
import com.agent.platform.core.exception.AgentResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 基于 Spring Data JDBC 的 Agent 定义加载器。
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "agent.platform.persistence",
        name = "type",
        havingValue = "jdbc",
        matchIfMissing = true
)
public class JdbcAgentDefinitionProvider implements AgentDefinitionProvider {

    private static final String ENABLED_STATUS = "ENABLED";

    private final AgentDefinitionRepository repository;
    private final AgentDefinitionConverter converter;

    /**
     * 从数据库加载处于启用状态的 Agent 定义。
     *
     * @param agentId Agent 唯一标识
     * @return Core Agent 定义
     */
    @Override
    public AgentDefinition getRequired(String agentId) {
        AgentDefinitionDO dataObject = repository.findById(agentId)
                .filter(item -> ENABLED_STATUS.equals(item.getStatus()))
                .orElseThrow(() -> new AgentResourceNotFoundException("Agent", agentId));
        return converter.toDomain(dataObject);
    }
}

package com.agent.platform.infrastructure.model.persistence;

import com.agent.platform.core.exception.AgentResourceNotFoundException;
import com.agent.platform.core.model.ModelDefinition;
import com.agent.platform.core.model.ModelDefinitionProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 基于 Spring Data JDBC 的模型定义加载器。
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "agent.platform.persistence",
        name = "type",
        havingValue = "jdbc",
        matchIfMissing = true
)
public class JdbcModelDefinitionProvider implements ModelDefinitionProvider {

    private final ModelConfigRepository repository;
    private final ModelDefinitionConverter converter;

    /**
     * 从数据库加载处于启用状态的模型定义。
     *
     * @param modelId 平台内部模型唯一标识
     * @return Core 模型定义
     */
    @Override
    public ModelDefinition getRequired(String modelId) {
        ModelConfigDO dataObject = repository.findById(modelId)
                .filter(item -> Boolean.TRUE.equals(item.getEnabled()))
                .orElseThrow(() -> new AgentResourceNotFoundException("模型配置", modelId));
        return converter.toDomain(dataObject);
    }
}

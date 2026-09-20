package com.agent.platform.infrastructure.model;

import com.agent.platform.core.exception.AgentResourceNotFoundException;
import com.agent.platform.core.model.ModelDefinition;
import com.agent.platform.core.model.ModelDefinitionProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 内存模型定义加载器，用于不连接数据库的本地演示场景。
 */
@Component
@ConditionalOnProperty(
        prefix = "agent.platform.persistence",
        name = "type",
        havingValue = "memory"
)
public class InMemoryModelDefinitionProvider implements ModelDefinitionProvider {

    private final Map<String, ModelDefinition> definitions = Map.of(
            "default-model",
            ModelDefinition.builder()
                    .id("default-model")
                    .provider("OPENAI")
                    .modelName("gpt-4o-mini")
                    .credentialKey("OPENAI_API_KEY")
                    .temperature(new BigDecimal("0.700"))
                    .maxTokens(4096)
                    .build()
    );

    /**
     * 从内存定义中查询模型配置。
     *
     * @param modelId 平台内部模型唯一标识
     * @return Core 模型定义
     */
    @Override
    public ModelDefinition getRequired(String modelId) {
        ModelDefinition definition = definitions.get(modelId);
        if (definition == null) {
            throw new AgentResourceNotFoundException("模型配置", modelId);
        }
        return definition;
    }
}

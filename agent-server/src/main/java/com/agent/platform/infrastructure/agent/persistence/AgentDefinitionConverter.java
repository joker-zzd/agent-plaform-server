package com.agent.platform.infrastructure.agent.persistence;

import com.agent.platform.core.agent.definition.AgentDefinition;
import org.springframework.stereotype.Component;

/**
 * Agent 定义数据对象与 Core 领域对象之间的转换器。
 */
@Component
public class AgentDefinitionConverter {

    /**
     * 将数据库 Agent 定义转换为 Core 运行时定义。
     *
     * @param dataObject Agent 定义数据对象
     * @return Core Agent 定义
     */
    public AgentDefinition toDomain(AgentDefinitionDO dataObject) {
        return AgentDefinition.builder()
                .id(dataObject.getId())
                .name(dataObject.getName())
                .systemPromptId(dataObject.getSystemPromptId())
                .promptVersion(dataObject.getPromptVersion())
                .modelId(dataObject.getModelId())
                .build();
    }
}

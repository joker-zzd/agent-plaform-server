package com.agent.platform.infrastructure.model.persistence;

import com.agent.platform.core.model.ModelDefinition;
import org.springframework.stereotype.Component;

/**
 * 模型配置数据对象与 Core 模型定义之间的转换器。
 */
@Component
public class ModelDefinitionConverter {

    /**
     * 将数据库模型配置转换为 Core 模型定义。
     *
     * @param dataObject 模型配置数据对象
     * @return Core 模型定义
     */
    public ModelDefinition toDomain(ModelConfigDO dataObject) {
        return ModelDefinition.builder()
                .id(dataObject.getId())
                .provider(dataObject.getProvider())
                .modelName(dataObject.getModelName())
                .baseUrl(dataObject.getBaseUrl())
                .credentialKey(dataObject.getCredentialKey())
                .temperature(dataObject.getTemperature())
                .maxTokens(dataObject.getMaxTokens())
                .build();
    }
}

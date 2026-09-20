package com.agent.platform.infrastructure.prompt.persistence;

import com.agent.platform.core.prompt.PromptDefinition;
import org.springframework.stereotype.Component;

/**
 * Prompt 数据对象与 Core 定义之间的转换器。
 */
@Component
public class PromptDefinitionConverter {

    /**
     * 将 Prompt 定义及其具体版本转换为 Core Prompt 定义。
     *
     * @param definitionDataObject Prompt 定义数据对象
     * @param versionDataObject Prompt 版本数据对象
     * @return Core Prompt 定义
     */
    public PromptDefinition toDomain(
            PromptDefinitionDO definitionDataObject,
            PromptVersionDO versionDataObject
    ) {
        return PromptDefinition.builder()
                .id(definitionDataObject.getId())
                .version(versionDataObject.getVersion())
                .systemText(versionDataObject.getSystemText())
                .build();
    }
}

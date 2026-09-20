package com.agent.platform.infrastructure.prompt.persistence;

import com.agent.platform.core.exception.AgentResourceNotFoundException;
import com.agent.platform.core.prompt.PromptDefinition;
import com.agent.platform.core.prompt.PromptProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 基于 Spring Data JDBC 的 Prompt 加载器。
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "agent.platform.persistence",
        name = "type",
        havingValue = "jdbc",
        matchIfMissing = true
)
public class JdbcPromptProvider implements PromptProvider {

    private static final String DRAFT_STATUS = "DRAFT";

    private final PromptDefinitionRepository definitionRepository;
    private final PromptVersionRepository versionRepository;
    private final PromptDefinitionConverter converter;

    /**
     * 加载 Prompt 当前正式发布的版本。
     *
     * @param promptId Prompt 唯一标识
     * @return 当前发布的 Prompt 定义
     */
    @Override
    public PromptDefinition getRequired(String promptId) {
        return getPublished(promptId);
    }

    /**
     * 加载 Prompt 当前正式发布的版本。
     *
     * @param promptId Prompt 唯一标识
     * @return 当前发布的 Prompt 定义
     */
    @Override
    public PromptDefinition getPublished(String promptId) {
        PromptDefinitionDO definitionDataObject = getEnabledDefinition(promptId);
        Integer publishedVersion = definitionDataObject.getPublishedVersion();
        if (publishedVersion == null) {
            throw new AgentResourceNotFoundException("Prompt 发布版本", promptId);
        }
        return loadVersion(definitionDataObject, publishedVersion);
    }

    /**
     * 加载 Prompt 指定的非草稿版本；版本为空时加载当前发布版本。
     *
     * @param promptId Prompt 唯一标识
     * @param version Prompt 版本号
     * @return 指定版本的 Prompt 定义
     */
    @Override
    public PromptDefinition getRequired(String promptId, Integer version) {
        if (version == null) {
            return getPublished(promptId);
        }
        PromptDefinitionDO definitionDataObject = getEnabledDefinition(promptId);
        return loadVersion(definitionDataObject, version);
    }

    /**
     * 查询处于启用状态的 Prompt 定义。
     */
    private PromptDefinitionDO getEnabledDefinition(String promptId) {
        return definitionRepository.findById(promptId)
                .filter(item -> Boolean.TRUE.equals(item.getEnabled()))
                .orElseThrow(() -> new AgentResourceNotFoundException("Prompt", promptId));
    }

    /**
     * 查询可以被 Agent 使用的 Prompt 版本。
     */
    private PromptDefinition loadVersion(PromptDefinitionDO definitionDataObject, Integer version) {
        String promptId = definitionDataObject.getId();
        PromptVersionDO versionDataObject = versionRepository.findByPromptIdAndVersion(promptId, version)
                .filter(item -> !DRAFT_STATUS.equals(item.getStatus()))
                .orElseThrow(() -> new AgentResourceNotFoundException(
                        "Prompt 版本",
                        promptId + ":" + version
                ));
        return converter.toDomain(definitionDataObject, versionDataObject);
    }
}

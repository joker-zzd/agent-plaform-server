package com.agent.platform.infrastructure.prompt;

import com.agent.platform.core.exception.AgentResourceNotFoundException;
import com.agent.platform.core.prompt.PromptDefinition;
import com.agent.platform.core.prompt.PromptProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * 基于内存的 Prompt 加载器。
 *
 * <p>当前只承载演示 Prompt，后续替换存储方式时无需修改 Agent Runtime。</p>
 */
@Component
@ConditionalOnProperty(
        prefix = "agent.platform.persistence",
        name = "type",
        havingValue = "memory"
)
public class InMemoryPromptProvider implements PromptProvider {

    /** 不同 Agent 使用各自独立的 System Prompt。 */
    private final Map<String, PromptDefinition> prompts = Map.of(
            "general-system-prompt",
            PromptDefinition.builder()
                    .id("general-system-prompt")
                    .version(1)
                    .systemText("你是一个通用 AI 助手，请使用清晰、准确的语言回答用户问题。")
                    .build(),
            "code-system-prompt",
            PromptDefinition.builder()
                    .id("code-system-prompt")
                    .version(1)
                    .systemText("你是一个专业的软件开发助手，请优先给出可靠、可维护的技术方案。")
                    .build()
    );

    /**
     * 从当前内存定义中查询 Prompt。
     */
    @Override
    public PromptDefinition getRequired(String promptId) {
        PromptDefinition promptDefinition = prompts.get(promptId);
        if (promptDefinition == null) {
            throw new AgentResourceNotFoundException("Prompt", promptId);
        }
        return promptDefinition;
    }

    /**
     * 从当前内存定义中查询指定版本的 Prompt。
     */
    @Override
    public PromptDefinition getRequired(String promptId, Integer version) {
        PromptDefinition promptDefinition = getRequired(promptId);
        if (!Objects.equals(promptDefinition.getVersion(), version)) {
            throw new AgentResourceNotFoundException("Prompt 版本", promptId + ":" + version);
        }
        return promptDefinition;
    }
}

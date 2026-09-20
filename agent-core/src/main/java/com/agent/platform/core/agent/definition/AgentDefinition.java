package com.agent.platform.core.agent.definition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Agent 静态定义。
 *
 * <p>描述一个 Agent 运行时需要使用的 Prompt 和模型配置。
 * 后续可以继续增加工具、知识库、记忆策略和权限策略等定义。</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentDefinition {

    /** Agent 唯一标识。 */
    private String id;

    /** Agent 显示名称。 */
    private String name;

    /** System Prompt 唯一标识。 */
    private String systemPromptId;

    /** 固定使用的 Prompt 版本号，为空时使用当前发布版本。 */
    private Integer promptVersion;

    /** 平台内部模型唯一标识。 */
    private String modelId;
}

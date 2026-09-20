package com.agent.platform.core.prompt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Prompt 静态定义。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromptDefinition {

    /** Prompt 唯一标识。 */
    private String id;

    /** 实际加载的 Prompt 版本号。 */
    private Integer version;

    /** System Prompt 正文。 */
    private String systemText;
}

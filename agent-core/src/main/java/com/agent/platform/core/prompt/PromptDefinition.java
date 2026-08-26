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

    /** System Prompt 正文。 */
    private String systemText;
}

package com.agent.platform.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Core 发给模型适配器的统一请求。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelRequest {

    /** 平台内部模型 ID，用于后续多模型路由。 */
    private String modelId;

    /** 当前 Agent 使用的 System Prompt。 */
    private String systemPrompt;

    /** 当前用户输入。 */
    private String userPrompt;
}

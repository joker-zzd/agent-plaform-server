package com.agent.platform.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 模型调用的统一结果。
 *
 * <p>后续可以继续加入 Token 使用量、结束原因和模型元数据。</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelResult {

    /** 模型生成的文本内容。 */
    private String content;
}

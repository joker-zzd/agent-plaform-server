package com.agent.platform.core.prompt;

/**
 * Prompt 加载端口。
 *
 * <p>实现可以从内存、YAML、数据库或远程配置中心读取 Prompt。</p>
 */
public interface PromptProvider {

    /**
     * 根据 Prompt ID 获取必须存在的 Prompt 定义。
     *
     * @param promptId Prompt 唯一标识
     * @return Prompt 定义
     */
    PromptDefinition getRequired(String promptId);
}

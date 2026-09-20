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

    /**
     * 获取 Prompt 当前正式发布的版本。
     *
     * @param promptId Prompt 唯一标识
     * @return 当前发布的 Prompt 定义
     */
    default PromptDefinition getPublished(String promptId) {
        return getRequired(promptId);
    }

    /**
     * 获取 Prompt 指定版本。
     *
     * <p>默认实现仅用于兼容不支持版本管理的 Provider。支持版本管理的实现必须覆盖此方法。</p>
     *
     * @param promptId Prompt 唯一标识
     * @param version Prompt 版本号；为空时加载当前发布版本
     * @return 指定版本的 Prompt 定义
     */
    default PromptDefinition getRequired(String promptId, Integer version) {
        if (version == null) {
            return getPublished(promptId);
        }
        throw new UnsupportedOperationException("当前 PromptProvider 不支持按版本加载 Prompt");
    }
}

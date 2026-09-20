package com.agent.platform.core.model;

/**
 * 模型定义加载契约。
 *
 * <p>Core 只依赖该契约，不感知模型定义来自数据库、配置文件或内存。</p>
 */
public interface ModelDefinitionProvider {

    /**
     * 根据平台模型标识加载可用的模型定义。
     *
     * @param modelId 平台内部模型唯一标识
     * @return 模型定义
     */
    ModelDefinition getRequired(String modelId);
}

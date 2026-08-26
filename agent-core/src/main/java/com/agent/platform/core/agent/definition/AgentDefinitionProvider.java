package com.agent.platform.core.agent.definition;

/**
 * Agent 定义加载端口。
 *
 * <p>Core 只依赖该接口，不关心定义来自内存、配置文件还是数据库。</p>
 */
public interface AgentDefinitionProvider {

    /**
     * 根据 Agent ID 获取必须存在的 Agent 定义。
     *
     * @param agentId Agent 唯一标识
     * @return Agent 定义
     */
    AgentDefinition getRequired(String agentId);
}

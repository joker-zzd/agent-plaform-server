package com.agent.platform.core.agent.runtime;

/**
 * 通用 Agent Runtime 入口。
 *
 * <p>HTTP、定时任务和其他接入方式都通过该接口执行 Agent。</p>
 */
public interface AgentRuntime {

    /**
     * 执行一次 Agent 请求。
     *
     * @param request Agent 请求
     * @return Agent 执行结果
     */
    AgentResult execute(AgentRequest request);
}

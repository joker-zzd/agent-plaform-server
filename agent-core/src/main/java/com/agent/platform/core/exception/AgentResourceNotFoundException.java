package com.agent.platform.core.exception;

/**
 * Agent 平台资源不存在异常。
 *
 * <p>用于 Agent、Prompt 等平台资源查询失败的场景。</p>
 */
public class AgentResourceNotFoundException extends RuntimeException {

    /**
     * 创建带有资源类型和资源 ID 的异常。
     *
     * @param resourceType 资源类型
     * @param resourceId   资源 ID
     */
    public AgentResourceNotFoundException(String resourceType, String resourceId) {
        super(resourceType + " 不存在，ID：" + resourceId);
    }
}

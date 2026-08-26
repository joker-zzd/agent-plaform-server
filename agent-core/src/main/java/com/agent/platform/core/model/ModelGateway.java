package com.agent.platform.core.model;

/**
 * 大模型调用端口。
 *
 * <p>具体实现可以使用 Spring AI、其他 AI 框架或直接调用模型厂商 API。</p>
 */
public interface ModelGateway {

    /**
     * 向选定模型发送一次请求。
     *
     * @param request 平台统一模型请求
     * @return 平台统一模型结果
     */
    ModelResult call(ModelRequest request);
}

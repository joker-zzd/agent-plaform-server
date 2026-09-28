package com.agent.platform.infrastructure.model;

import com.agent.platform.core.model.ModelDefinition;
import com.agent.platform.core.model.ModelRequest;
import com.agent.platform.core.model.ModelResult;

/**
 * 单个模型供应商的调用契约，负责把平台模型对象转换为供应商请求。
 */
public interface ModelProviderClient {

    /** 返回模型配置使用的供应商标识。 */
    String provider();

    /** 使用指定模型配置执行一次调用。 */
    ModelResult call(ModelDefinition definition, ModelRequest request);
}

package com.agent.platform.infrastructure.model;

import com.agent.platform.core.exception.ModelConfigurationException;
import com.agent.platform.core.model.ModelDefinition;
import com.agent.platform.core.model.ModelDefinitionProvider;
import com.agent.platform.core.model.ModelGateway;
import com.agent.platform.core.model.ModelRequest;
import com.agent.platform.core.model.ModelResult;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据模型定义选择供应商适配器，保持 Core 的模型调用契约稳定。
 */
@Component
public class RoutingModelGateway implements ModelGateway {

    private final ModelDefinitionProvider modelDefinitionProvider;
    private final Map<String, ModelProviderClient> clients;

    /**
     * 装配供应商适配器，并在启动时检查供应商标识是否重复。
     */
    public RoutingModelGateway(ModelDefinitionProvider modelDefinitionProvider, List<ModelProviderClient> providerClients) {
        this.modelDefinitionProvider = modelDefinitionProvider;
        Map<String, ModelProviderClient> registeredClients = new HashMap<>();
        for (ModelProviderClient client : providerClients) {
            String provider = client.provider();
            if (!StringUtils.hasText(provider)) {
                throw new ModelConfigurationException("模型供应商适配器标识不能为空");
            }
            if (registeredClients.putIfAbsent(provider, client) != null) {
                throw new ModelConfigurationException("模型供应商适配器重复：" + provider);
            }
        }
        this.clients = Map.copyOf(registeredClients);
    }

    /**
     * 加载平台模型配置后，将请求交给对应供应商适配器。
     */
    @Override
    public ModelResult call(ModelRequest request) {
        ModelDefinition definition = modelDefinitionProvider.getRequired(request.getModelId());
        String provider = definition.getProvider();
        if (!StringUtils.hasText(provider)) {
            throw new ModelConfigurationException("模型供应商未配置，模型 ID：" + definition.getId());
        }
        ModelProviderClient client = clients.get(provider);
        if (client == null) {
            throw new ModelConfigurationException("暂不支持模型供应商：" + provider);
        }
        return client.call(definition, request);
    }
}

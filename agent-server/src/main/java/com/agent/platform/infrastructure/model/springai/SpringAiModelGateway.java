package com.agent.platform.infrastructure.model.springai;

import com.agent.platform.core.exception.ModelConfigurationException;
import com.agent.platform.core.model.ModelDefinition;
import com.agent.platform.core.model.ModelDefinitionProvider;
import com.agent.platform.core.model.ModelGateway;
import com.agent.platform.core.model.ModelRequest;
import com.agent.platform.core.model.ModelResult;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 使用 Spring AI ChatClient 调用大模型的适配器。
 *
 * <p>Spring AI 类型被限制在基础设施层，Agent Core 只感知 ModelGateway。</p>
 */
@Component
@RequiredArgsConstructor
public class SpringAiModelGateway implements ModelGateway {

    private static final String OPENAI_PROVIDER = "OPENAI";

    private final ChatClient chatClient;
    private final ModelDefinitionProvider modelDefinitionProvider;
    private final EnvironmentModelCredentialResolver credentialResolver;

    /**
     * 将平台模型请求转换为 ChatClient 调用，并返回平台统一结果。
     */
    @Override
    public ModelResult call(ModelRequest request) {
        ModelDefinition modelDefinition = modelDefinitionProvider.getRequired(request.getModelId());
        OpenAiChatOptions.Builder options = createOptions(modelDefinition);

        String content = chatClient.prompt()
                .options(options)
                .system(request.getSystemPrompt())
                .user(request.getUserPrompt())
                .call()
                .content();

        return ModelResult.builder()
                .content(content)
                .build();
    }

    /**
     * 将平台模型定义转换为当前 OpenAI 客户端的单次请求参数。
     */
    private OpenAiChatOptions.Builder createOptions(ModelDefinition definition) {
        if (!OPENAI_PROVIDER.equals(definition.getProvider())) {
            throw new ModelConfigurationException(
                    "暂不支持模型供应商：" + definition.getProvider()
            );
        }
        if (!StringUtils.hasText(definition.getModelName())) {
            throw new ModelConfigurationException("模型名称不能为空，模型 ID：" + definition.getId());
        }

        OpenAiChatOptions.Builder builder = OpenAiChatOptions.builder()
                .model(definition.getModelName());

        if (definition.getTemperature() != null) {
            builder.temperature(definition.getTemperature().doubleValue());
        }
        if (definition.getMaxTokens() != null) {
            builder.maxTokens(definition.getMaxTokens());
        }
        if (StringUtils.hasText(definition.getBaseUrl())) {
            builder.baseUrl(definition.getBaseUrl());
        }
        if (StringUtils.hasText(definition.getCredentialKey())) {
            builder.apiKey(credentialResolver.getRequired(definition.getCredentialKey()));
        }
        return builder;
    }
}

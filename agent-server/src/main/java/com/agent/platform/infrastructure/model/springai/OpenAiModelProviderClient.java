package com.agent.platform.infrastructure.model.springai;

import com.agent.platform.core.exception.ModelConfigurationException;
import com.agent.platform.core.model.ModelDefinition;
import com.agent.platform.core.model.ModelRequest;
import com.agent.platform.core.model.ModelResult;
import com.agent.platform.infrastructure.model.ModelProviderClient;
import com.agent.platform.infrastructure.model.constant.ModelProviderConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * OpenAI 模型适配器，封装 Spring AI 请求选项和凭据解析。
 */
@Component
@RequiredArgsConstructor
public class OpenAiModelProviderClient implements ModelProviderClient {

    private final ChatClient chatClient;
    private final EnvironmentModelCredentialResolver credentialResolver;

    /** 返回 OpenAI 供应商标识。 */
    @Override
    public String provider() {
        return ModelProviderConstants.OPENAI;
    }

    /**
     * 将平台模型请求转换为 OpenAI ChatClient 调用。
     */
    @Override
    public ModelResult call(ModelDefinition definition, ModelRequest request) {
        OpenAiChatOptions.Builder options = createOptions(definition);
        String content = chatClient.prompt()
                .options(options)
                .system(request.getSystemPrompt())
                .user(request.getUserPrompt())
                .call()
                .content();
        return ModelResult.builder().content(content).build();
    }

    /**
     * 将模型配置转换为当前 OpenAI 客户端的单次请求参数。
     */
    private OpenAiChatOptions.Builder createOptions(ModelDefinition definition) {
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

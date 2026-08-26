package com.agent.platform.infrastructure.model.springai;

import com.agent.platform.core.model.ModelGateway;
import com.agent.platform.core.model.ModelRequest;
import com.agent.platform.core.model.ModelResult;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

/**
 * 使用 Spring AI ChatClient 调用大模型的适配器。
 *
 * <p>Spring AI 类型被限制在基础设施层，Agent Core 只感知 ModelGateway。</p>
 */
@Component
@RequiredArgsConstructor
public class SpringAiModelGateway implements ModelGateway {

    private final ChatClient chatClient;

    /**
     * 将平台模型请求转换为 ChatClient 调用，并返回平台统一结果。
     */
    @Override
    public ModelResult call(ModelRequest request) {
        /*
         * 当前只有一个默认 ChatClient，modelId 暂时作为平台路由标识保留。
         * 接入多模型后由独立的 ModelRouter 根据 modelId 选择具体客户端。
         */
        String content = chatClient.prompt()
                .system(request.getSystemPrompt())
                .user(request.getUserPrompt())
                .call()
                .content();

        return ModelResult.builder()
                .content(content)
                .build();
    }
}

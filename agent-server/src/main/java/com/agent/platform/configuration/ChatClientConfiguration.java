package com.agent.platform.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ChatClient 基础配置。
 *
 * <p>统一创建客户端，后续可在此加入通用 Advisor、默认选项和可观测配置。</p>
 */
@Configuration
public class ChatClientConfiguration {

    /**
     * 使用 Spring AI 自动配置的 Builder 创建默认 ChatClient。
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }
}

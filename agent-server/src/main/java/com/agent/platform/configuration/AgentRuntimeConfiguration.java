package com.agent.platform.configuration;

import com.agent.platform.core.agent.definition.AgentDefinitionProvider;
import com.agent.platform.core.agent.runtime.AgentRuntime;
import com.agent.platform.core.agent.runtime.DefaultAgentRuntime;
import com.agent.platform.core.model.ModelGateway;
import com.agent.platform.core.prompt.PromptProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Agent Runtime 装配配置。
 *
 * <p>在 Server 模块中组合 Core 端口及其基础设施实现。</p>
 */
@Configuration
public class AgentRuntimeConfiguration {

    /**
     * 创建平台默认 Runtime，保持 Core 本身不依赖 Spring 容器。
     */
    @Bean
    public AgentRuntime agentRuntime(
            AgentDefinitionProvider agentDefinitionProvider,
            PromptProvider promptProvider,
            ModelGateway modelGateway
    ) {
        return new DefaultAgentRuntime(agentDefinitionProvider, promptProvider, modelGateway);
    }
}

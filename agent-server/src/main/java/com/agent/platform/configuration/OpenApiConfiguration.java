package com.agent.platform.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Agent Platform OpenAPI 文档配置。
 *
 * <p>集中维护平台级文档信息，Controller 只描述各自负责的 HTTP 接口。</p>
 */
@Configuration
public class OpenApiConfiguration {

    /**
     * 创建平台 OpenAPI 定义。
     *
     * @return 平台 OpenAPI 元数据
     */
    @Bean
    public OpenAPI agentPlatformOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Agent Platform API")
                        .description("通用 Agent 平台 HTTP API，当前提供同步 Agent 执行能力。")
                        .version("v1")
                        .contact(new Contact().name("Agent Platform Team")))
                .addTagsItem(new Tag()
                        .name("Agent Runtime")
                        .description("Agent 同步执行与运行结果接口"));
    }
}

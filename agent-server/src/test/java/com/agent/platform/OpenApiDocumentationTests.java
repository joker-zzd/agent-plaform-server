package com.agent.platform;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * OpenAPI 文档端点集成测试。
 *
 * <p>通过随机端口访问真实 HTTP 端点，验证 OpenAPI 描述和 Swagger UI 均可用。</p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OpenApiDocumentationTests {

    @Value("${local.server.port}")
    private int serverPort;

    /**
     * 验证文档包含 Agent 执行接口和主要响应模型。
     */
    @Test
    void shouldExposeAgentRunApiInOpenApiDocument() throws IOException, InterruptedException {
        HttpResponse<String> response = get("/v3/api-docs");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.headers().firstValue("content-type").orElse(""))
                .contains("application/json");
        assertThat(response.body())
                .contains("Agent Platform API")
                .contains("/api/v1/agents/{agentId}/runs")
                .contains("runAgent")
                .contains("RunAgentRequest")
                .contains("RunAgentResponse")
                .contains("ApiErrorResponse");
    }

    /**
     * 验证 Swagger UI 静态页面可以正常访问。
     */
    @Test
    void shouldExposeSwaggerUi() throws IOException, InterruptedException {
        HttpResponse<String> response = get("/swagger-ui.html");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).containsIgnoringCase("swagger ui");
    }

    private HttpResponse<String> get(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + serverPort + path))
                .GET()
                .build();

        return HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
    }
}

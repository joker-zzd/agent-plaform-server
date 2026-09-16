# Agent Platform 后续开发规划

当前项目已经完成基础 Agent Runtime、Spring AI 模型调用和 REST API。下一阶段应优先完善持久化，再逐步增加工具、记忆、知识库与生产能力。

## 开发路线

### 1. Agent 与 Prompt 持久化

- 设计 Agent、Prompt 和 Prompt 版本表。
- 使用 Flyway 管理数据库结构。
- 使用 Spring Data JDBC 实现 `AgentDefinitionProvider` 和 `PromptProvider`。
- 增加 Agent、Prompt 的管理、发布和回滚接口。

验收目标：不修改 Java 代码，只通过数据库配置即可创建并运行新的 Agent。

### 2. 会话与执行记录

- 持久化 Session、Message、Execution 和执行步骤。
- 记录模型、Prompt 版本、Token、耗时、状态和错误信息。
- 明确事务边界和执行状态流转。

### 3. 流式响应与 Tool Calling

- 增加 SSE 流式对话接口。
- 建立 `ToolDefinition`、`ToolRegistry` 和 `ToolExecutor` 等核心契约。
- 实现工具发现、参数转换、权限检查、执行和结果回传闭环。

### 4. Chat Memory 与权限

- PostgreSQL 保存完整聊天历史。
- Redis 保存短期上下文、摘要和临时状态。
- 接入 Spring Security、JWT，并控制 Agent、Tool 和业务数据访问权限。

### 5. RAG 与知识库

- 接入 MinIO/S3 保存原始文档。
- 完成文档解析、切片、Embedding 和 pgvector 检索。
- 支持权限过滤、来源引用和检索效果评估。

### 6. MCP 与生产化

- 接入 MCP Tool、Resource 和 Prompt。
- 增加 Docker、健康检查、限流、重试和配置管理。
- 使用 Actuator、Micrometer、OpenTelemetry、Prometheus 和 Grafana 建立可观测体系。

## 涉及的主要知识点

- 架构：六边形架构、依赖倒置、Adapter、领域对象与持久化对象隔离。
- 数据：PostgreSQL、Spring Data JDBC、Flyway、事务、索引和版本控制。
- AI：Spring AI、ChatClient、Tool Calling、Chat Memory、Embedding 和 RAG。
- 中间件：Redis、pgvector、MinIO/S3、MCP。
- 接口：REST、SSE、参数校验、异常处理和 OpenAPI。
- 安全：Spring Security、JWT、RBAC/ABAC、数据权限和 Prompt Injection 防护。
- 运维：Docker、CI/CD、日志、指标、链路追踪、告警和故障恢复。

## 近期实施顺序

1. 创建 Agent、Prompt 和 Prompt Version 数据模型及 Flyway 脚本。
2. 实现数据库 Provider，替换内存 Provider。
3. 增加 Agent 和 Prompt 管理 API。
4. 持久化 Session、Message 和 Execution。
5. 增加 SSE 流式接口。
6. 开始 Tool Calling 和权限控制。


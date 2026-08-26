# Agent Platform 任务记录

## 2026-08-26｜任务 001：建立多模块项目骨架

### 任务目标

- 将单体初始工程调整为 Maven 多模块工程。
- 首期只建立 `agent-core` 和 `agent-server` 两个子模块。
- 只搭建目录、依赖边界和项目规范，不新增业务代码。

### 本次变更

- 根 POM 改为聚合父工程，统一管理 Java 和 Spring AI 版本。
- 新增 `agent-core`，预留 Agent、Model、Prompt、Tool、Memory、Policy、Execution 和 Exception 包。
- 新增 `agent-server`，预留 API、Spring AI Adapter、Prompt、Agent、Execution 和 Configuration 包。
- `agent-server` 单向依赖 `agent-core`。
- 原有启动类、应用配置和测试迁移到 `agent-server`，代码内容不变。
- 项目规范文件调整为根目录 `AGENTS.md`。
- 任务记录文件调整为根目录 `Record.md`。

### 关键决策

- 当前使用模块化单体，不提前拆分 RAG、MCP、Memory、Security 等空模块。
- Agent Core 保持技术无关，Spring AI 相关依赖和实现仅放在 Server/Adapter 一侧。
- 不提交空包占位文件；新增 Java 类型时再由实际源码建立并跟踪对应目录。

### 验证结果

- `mvn -q validate` 通过，父工程可以正确识别并聚合两个子模块。
- `mvn -q test` 已执行，但当前环境 Maven 使用 Java 17，而项目配置为 Java 21，编译器报告“不支持发行版本 21”。这是本机 JDK 环境问题，不是模块结构错误；本次未擅自降低项目 Java 版本。

### 后续事项

- 定义第一批 Core 领域对象和端口接口。
- 建立基于 ChatClient 的 `ModelGateway` Adapter。

### 后续调整

- 根据项目偏好移除全部源码目录中的 `.gitkeep` 占位文件。Git 不跟踪空目录，因此尚未包含代码的预留包不会出现在仓库提交中，后续随实际 Java 文件自然建立。

## 2026-08-26｜任务 002：固化日常编码偏好

### 任务目标

- 将用户确认的 Java 类、Lombok、包结构和注释要求写入项目规范。

### 本次变更

- 规定项目默认使用传统 Java 类，不使用 `record`。
- 规定数据承载类使用 Lombok `@Data`，不手写 Getter、Setter 和仅用于字段赋值的样板构造方法。
- 规定 Spring 行为类使用 `@RequiredArgsConstructor` 完成构造器注入，不滥用 `@Data`。
- 将 `core.agent` 进一步划分为 `definition` 和 `runtime`，并明确其他通用能力保持为 Core 下的同级功能包。
- 规定包按功能职责组织，不按 `entity`、`interface`、`service`、`impl` 机械分包。
- 规定新增代码需要包含有实际说明意义的中文注释。

### 验证结果

- 已检查 `AGENTS.md`，新增规则与当前两模块依赖边界一致。
- 本次只修改 Markdown 文档，无需执行 Maven 构建。

## 2026-08-26｜任务 003：实现 Agent 最小运行链路

### 任务目标

- 按照两模块边界写入第一批可运行基础代码。
- 使用普通 Java 类和 Lombok，建立从 HTTP API 到 ChatClient Adapter 的完整调用链。

### 本次变更

- 为 `agent-core` 和 `agent-server` 添加 Lombok，并为 Server 添加请求参数校验依赖。
- 在 `core.agent.definition` 中实现 Agent 定义及加载端口。
- 在 `core.agent.runtime` 中实现请求、结果、Runtime 接口和默认编排实现。
- 在 `core.model` 中实现模型调用端口、请求和结果对象。
- 在 `core.prompt` 中实现 Prompt 定义及加载端口。
- 新增平台资源不存在异常。
- 在 Server 中提供两个内存 Agent、两个内存 Prompt 和对应 Provider。
- 新增 Spring AI `ModelGateway` Adapter，并将 `ChatClient` 限制在基础设施层。
- 新增 Runtime 和 ChatClient 的 Spring Bean 装配。
- 新增 `/api/v1/agents/{agentId}/runs` 接口、API DTO、参数校验和统一异常响应。
- OpenAI 密钥改为通过 `OPENAI_API_KEY` 环境变量注入。
- 新增 Core Runtime 单元测试和测试环境的模型占位配置。

### 关键决策

- 当前只实现单模型 ChatClient，保留 `modelId` 作为后续多模型路由入口。
- 当前使用内存 Provider 验证扩展边界，后续替换为 YAML 或数据库时不修改 Runtime。
- 本阶段暂不加入 Tool、Memory、RAG 和执行持久化，先保持最小闭环清晰。

### 验证结果

- 使用当前 JDK 17，通过临时参数执行 `mvn -q "-Dmaven.compiler.release=17" test` 成功。
- Core Runtime 单元测试：1 个通过，0 失败。
- Server Spring Boot 上下文测试：1 个通过，0 失败；ChatClient、Adapter、Runtime 和 Web Bean 均成功装配。
- 项目正式版本仍为 Java 21；本机切换到 JDK 21 后应再次执行不带临时参数的 `mvn -q test`。

### 后续事项

- 配置真实模型服务后，对 `general-agent` 和 `code-agent` 执行接口联调。
- 下一阶段可增加执行记录端口，保存 executionId、Token、耗时和状态。

## 2026-08-26｜任务 004：整理项目 README 技术栈

### 任务目标

- 在 README 中记录实现通用 Agent 平台需要使用的技术栈和各组件职责。

### 本次变更

- 补充项目目标、总体架构和模块依赖方向。
- 区分当前已引入技术与后续计划接入技术。
- 明确 PostgreSQL、Redis、pgvector 和 MinIO 的存储职责。
- 补充 Agent Runtime、Prompt、Tool、Memory、RAG、权限和审计能力规划。
- 增加分阶段实施路线、当前进度、本地运行方式和接口示例。

### 关键决策

- PostgreSQL 作为永久数据的主要事实来源，Redis 只承担短期 Memory、缓存和运行时状态。
- 初期使用 PostgreSQL + pgvector 完成关系数据和向量数据存储，避免过早引入独立向量数据库。
- 技术栈表明确标记“当前已引入”和“计划接入”，避免文档状态与代码实现不一致。

### 验证结果

- 已核对 README 中的当前版本与父 POM、Server POM 保持一致。
- 本次只修改 Markdown 文档，无需执行 Maven 构建。

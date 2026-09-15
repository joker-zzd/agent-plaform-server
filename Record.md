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

## 2026-09-15｜任务 005：接入 OpenAPI 与 Swagger UI

### 任务目标

- 为现有 Agent REST API 生成清晰、可调试的 OpenAPI 文档。
- 保持 Swagger 相关类型仅存在于 Server/API 层，不污染 Agent Core。
- 通过真实 HTTP 端点测试验证 OpenAPI JSON 和 Swagger UI 可以访问。

### 本次变更

- 在父 POM 中统一管理 Springdoc 和 Swagger Annotations 版本。
- 在 `agent-server` 中引入 Springdoc Web MVC UI，并处理 Spring AI 引入的 Swagger 注解版本冲突。
- 新增 `OpenApiConfiguration`，集中定义平台标题、版本、说明和 Agent Runtime 标签。
- 为 Agent  执行接口补充操作说明、路径参数、成功响应、错误响应和示例。
- 为 API 请求、响应和错误 DTO 补充字段说明与示例。
- 配置 Swagger UI 路径、排序、请求耗时显示和在线调试。
- 在 README 中增加 Swagger UI、OpenAPI JSON 和 OpenAPI YAML 访问地址。
- 新增 OpenAPI 端点集成测试，通过随机端口访问真实 HTTP 服务。

### 关键决策

- 当前接口数量较少，不额外抽取 API Contract 接口，保持路由、文档和 Controller 实现集中可见。
- OpenAPI 注解只用于 `agent-server` 的 HTTP DTO 和 Controller，Core 继续保持技术无关。
- 文档只描述当前真实能力，明确动态变量、多轮记忆、Tool Calling 和流式输出尚未实现。
- Spring AI 2.0.1 传递引入旧版 Swagger 注解，因此在其依赖边界排除旧版本，统一使用与 Springdoc 3.0.3 匹配的 2.2.47。

### 验证结果

- 使用 Microsoft OpenJDK 21.0.12.1 执行 `mvn -q clean test` 成功。
- 共执行 4 个测试，0 失败、0 错误、0 跳过。
- `/v3/api-docs` 返回 HTTP 200，包含 Agent 执行路径、operationId 和三个 API Schema。
- `/swagger-ui.html` 跟随重定向后返回 HTTP 200，Swagger UI 页面可正常加载。

### 后续事项

- 引入 Spring Security 后，需要明确 Swagger 文档端点的认证和生产环境开放策略。
- 新增 API 时同步补充操作说明、响应码、DTO Schema 和文档端点测试断言。

## 2026-09-15｜任务 006：调整服务默认端口

### 任务目标

- 避免本机 8080 端口占用，将 Agent Platform 默认端口调整为 8090。

### 本次变更

- 在 Server 应用配置中将 `server.port` 设置为 `8090`。
- 同步更新 README 中 Swagger UI、OpenAPI JSON 和 OpenAPI YAML 的访问地址。

### 验证结果

- OpenAPI 集成测试使用随机端口运行，不依赖或占用固定的 8090 端口。

## 2026-09-15｜任务 007：接入 PostgreSQL

### 任务目标

- 为 `agent-server` 建立 PostgreSQL 数据源和可版本化的数据库迁移基础。
- 保持数据库密码与源码、默认配置分离。

### 本次变更

- 引入 Spring Data JDBC、PostgreSQL JDBC Driver 和 Flyway PostgreSQL 支持。
- 默认连接本机 `agent_platform` 数据库，并支持通过环境变量覆盖连接参数。
- 增加 HikariCP 连接池基础配置和 Flyway V1 基线迁移。
- 测试环境使用 PostgreSQL 兼容模式的 H2，避免普通单元测试依赖本机数据库。

### 关键决策

- 密码只从 `AGENT_PLATFORM_DB_PASSWORD` 读取，不写入仓库。
- 当前只建立迁移基线，不在数据源接入任务中提前设计 Agent、Prompt 等业务表。

### 验证结果

- 已创建本机 `agent_platform` 数据库，并验证 `postgres` 用户可正常连接。
- 使用 Microsoft OpenJDK 21.0.12.1 执行 `mvn -q clean test package`，构建和全部测试通过。
- 应用成功连接 PostgreSQL 17.11，HikariCP 连接池启动成功。
- Flyway 成功执行 V1 迁移，`flyway_schema_history` 记录版本 1 状态为成功。

## 2026-09-15｜任务 008：补充测试代码编写规则

### 任务目标

- 明确测试代码的新增和修改需要用户显式要求。

### 本次变更

- 在根目录 `AGENTS.md` 中增加规则：用户未明确要求时，不编写测试相关代码。
- 保留运行已有测试或构建检查的验证要求。

### 验证结果

- 本次仅修改项目规范和任务记录，未修改代码，无需执行 Maven 构建。

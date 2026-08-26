# Agent Platform 项目开发规范

## 1. 适用范围

本规范适用于整个 `agent-platform` 仓库。新增子模块可以补充更具体的 `AGENTS.md`，但不得违反根目录规范。

## 2. 架构与依赖

- 项目采用 Maven 多模块结构，当前包含 `agent-core` 和 `agent-server`。
- 依赖方向固定为 `agent-server -> agent-core`，禁止反向依赖和循环依赖。
- `agent-core` 保存通用领域模型、运行时流程及扩展契约，不依赖 Spring AI、Web、数据库和具体业务系统。
- `agent-server` 保存 Spring Boot 启动、API、配置和基础设施适配实现。
- 电商、运维、HR 等业务能力通过 Adapter、扩展包或 MCP Server 接入，禁止写死在 Agent Core 中。
- 跨层调用依赖接口，Core 不感知接口的具体技术实现。

## 3. 包职责

- `core.agent`：Agent 定义、请求、结果、上下文和运行时编排。
- `core.model`：模型调用契约及平台统一模型对象。
- `core.prompt`：Prompt 定义和加载契约。
- `core.tool`：工具描述、发现和注册契约。
- `core.memory`：记忆访问契约。
- `core.policy`：权限和策略判断契约。
- `core.execution`：执行记录及执行状态。
- `core.exception`：平台核心异常。
- `api`：HTTP 接口和传输对象，不编写 Agent 编排逻辑。
- `infrastructure`：Spring AI、存储和外部系统等技术实现。
- `configuration`：Spring Bean 与配置属性装配。

## 4. Java 编码规范

- 使用 Java 21，类名使用 UpperCamelCase，方法和变量使用 lowerCamelCase，常量使用 UPPER_SNAKE_CASE。
- 优先使用构造器注入，禁止字段注入。
- API DTO、领域对象和持久化对象相互独立，禁止用一个对象贯穿所有层。
- 对稳定且只承载数据的不可变对象，优先考虑 `record`。
- 禁止在 Controller 中直接调用 `ChatClient`、拼接 Prompt 或处理 Tool Calling。
- 禁止在 Core 中直接引用 `ChatClient`、`ChatModel`、Advisor 或数据库框架类型。
- 异常应表达明确语义，禁止无说明地捕获并忽略异常。
- 敏感数据不得写入源码、默认配置、日志或任务记录。

## 5. 配置与接口规范

- 平台自定义配置统一使用 `agent.platform` 前缀。
- 密钥通过环境变量或外部配置注入，不得提交到 Git。
- HTTP API 统一使用 `/api/v1` 前缀。
- 对外响应使用明确 DTO，不直接暴露 Spring AI 或数据库对象。

## 6. 测试与变更规范

- Core 规则优先编写不依赖 Spring 容器的单元测试。
- Adapter 使用集成测试验证框架配置和边界转换。
- 每次修改后至少执行与变更范围匹配的 Maven 测试或构建检查。
- 不修改与当前任务无关的用户代码和配置。
- 每次任务完成后更新根目录 `Record.md`，记录目标、变更、关键决策、验证结果和后续事项。

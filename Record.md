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

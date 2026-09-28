-- Agent Platform PostgreSQL 初始化脚本。
-- 表结构依据本地 agent_platform 数据库整理，包含约束、索引和字段说明。

CREATE TABLE model_config
(
    id             VARCHAR(64)  NOT NULL,
    name           VARCHAR(128) NOT NULL,
    provider       VARCHAR(64)  NOT NULL,
    model_name     VARCHAR(128) NOT NULL,
    base_url       VARCHAR(512),
    credential_key VARCHAR(128),
    temperature    NUMERIC(4, 3),
    max_tokens     INTEGER,
    enabled        BOOLEAN      NOT NULL DEFAULT TRUE,
    extra_config   JSONB        NOT NULL DEFAULT '{}'::JSONB,
    created_time   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT model_config_pkey PRIMARY KEY (id),
    CONSTRAINT ck_model_config_temperature
        CHECK (temperature IS NULL OR (temperature >= 0 AND temperature <= 2)),
    CONSTRAINT ck_model_config_max_tokens
        CHECK (max_tokens IS NULL OR max_tokens > 0)
);

COMMENT ON TABLE model_config IS '平台模型配置表，管理 Agent 可以使用的大模型及调用参数';
COMMENT ON COLUMN model_config.id IS '平台内部模型唯一标识';
COMMENT ON COLUMN model_config.name IS '模型配置显示名称';
COMMENT ON COLUMN model_config.provider IS '模型供应商，例如 OPENAI、DEEPSEEK、DASHSCOPE';
COMMENT ON COLUMN model_config.model_name IS '模型供应商定义的真实模型名称';
COMMENT ON COLUMN model_config.base_url IS '模型服务接口地址，为空时使用供应商默认地址';
COMMENT ON COLUMN model_config.credential_key IS '模型密钥对应的环境变量或外部密钥标识，不保存密钥明文';
COMMENT ON COLUMN model_config.temperature IS '模型生成随机性参数，取值范围为 0 至 2';
COMMENT ON COLUMN model_config.max_tokens IS '模型单次调用允许生成的最大 Token 数量';
COMMENT ON COLUMN model_config.enabled IS '模型配置是否启用：true-启用，false-禁用';
COMMENT ON COLUMN model_config.extra_config IS '模型供应商专属扩展配置，使用 JSON 格式保存';
COMMENT ON COLUMN model_config.created_time IS '模型配置创建时间';
COMMENT ON COLUMN model_config.updated_time IS '模型配置最后更新时间';

CREATE TABLE prompt_definition
(
    id                VARCHAR(64)  NOT NULL,
    name              VARCHAR(128) NOT NULL,
    description       VARCHAR(500),
    published_version INTEGER,
    enabled           BOOLEAN      NOT NULL DEFAULT TRUE,
    created_by        VARCHAR(64),
    created_time      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT prompt_definition_pkey PRIMARY KEY (id)
);

COMMENT ON TABLE prompt_definition IS 'Prompt 定义表，保存 Prompt 的稳定业务标识和当前发布版本';
COMMENT ON COLUMN prompt_definition.id IS 'Prompt 唯一业务标识';
COMMENT ON COLUMN prompt_definition.name IS 'Prompt 显示名称';
COMMENT ON COLUMN prompt_definition.description IS 'Prompt 的用途和功能说明';
COMMENT ON COLUMN prompt_definition.published_version IS '当前正式发布并默认使用的 Prompt 版本号';
COMMENT ON COLUMN prompt_definition.enabled IS 'Prompt 是否启用：true-启用，false-禁用';
COMMENT ON COLUMN prompt_definition.created_by IS 'Prompt 创建人标识';
COMMENT ON COLUMN prompt_definition.created_time IS 'Prompt 创建时间';
COMMENT ON COLUMN prompt_definition.updated_time IS 'Prompt 最后更新时间';

CREATE TABLE prompt_version
(
    id                 UUID         NOT NULL DEFAULT gen_random_uuid(),
    prompt_id          VARCHAR(64)  NOT NULL,
    version            INTEGER      NOT NULL,
    system_text        TEXT         NOT NULL,
    variable_schema    JSONB        NOT NULL DEFAULT '{}'::JSONB,
    status             VARCHAR(20)  NOT NULL DEFAULT 'DRAFT',
    change_description VARCHAR(500),
    created_by         VARCHAR(64),
    created_time       TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    published_time     TIMESTAMPTZ,
    CONSTRAINT prompt_version_pkey PRIMARY KEY (id),
    CONSTRAINT uk_prompt_version UNIQUE (prompt_id, version),
    CONSTRAINT fk_prompt_version_prompt
        FOREIGN KEY (prompt_id) REFERENCES prompt_definition (id) ON DELETE CASCADE,
    CONSTRAINT ck_prompt_version_positive CHECK (version > 0),
    CONSTRAINT ck_prompt_version_status
        CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED'))
);

COMMENT ON TABLE prompt_version IS 'Prompt 历史版本表，每次修改 Prompt 时新增版本，不覆盖历史数据';
COMMENT ON COLUMN prompt_version.id IS 'Prompt 版本记录唯一标识';
COMMENT ON COLUMN prompt_version.prompt_id IS 'Prompt 定义标识，关联 prompt_definition.id';
COMMENT ON COLUMN prompt_version.version IS 'Prompt 版本号，同一个 Prompt 内从 1 开始递增';
COMMENT ON COLUMN prompt_version.system_text IS '该版本的 System Prompt 正文';
COMMENT ON COLUMN prompt_version.variable_schema IS 'Prompt 支持的动态变量及变量约束，使用 JSON 格式保存';
COMMENT ON COLUMN prompt_version.status IS '版本状态：DRAFT-草稿，PUBLISHED-已发布，ARCHIVED-已归档';
COMMENT ON COLUMN prompt_version.change_description IS '该版本相对于上一版本的变更说明';
COMMENT ON COLUMN prompt_version.created_by IS 'Prompt 版本创建人标识';
COMMENT ON COLUMN prompt_version.created_time IS 'Prompt 版本创建时间';
COMMENT ON COLUMN prompt_version.published_time IS 'Prompt 版本正式发布时间';

ALTER TABLE prompt_definition
    ADD CONSTRAINT fk_prompt_published_version
        FOREIGN KEY (id, published_version)
            REFERENCES prompt_version (prompt_id, version)
            DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX idx_prompt_version_status
    ON prompt_version (prompt_id, status);
CREATE INDEX idx_prompt_version_created_time
    ON prompt_version (prompt_id, created_time DESC);

CREATE TABLE agent_definition
(
    id               VARCHAR(64)  NOT NULL,
    name             VARCHAR(128) NOT NULL,
    description      VARCHAR(500),
    model_id         VARCHAR(64)  NOT NULL,
    system_prompt_id VARCHAR(64)  NOT NULL,
    prompt_version   INTEGER,
    status           VARCHAR(20)  NOT NULL DEFAULT 'ENABLED',
    runtime_config   JSONB        NOT NULL DEFAULT '{}'::JSONB,
    created_by       VARCHAR(64),
    created_time     TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time     TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT agent_definition_pkey PRIMARY KEY (id),
    CONSTRAINT fk_agent_model
        FOREIGN KEY (model_id) REFERENCES model_config (id),
    CONSTRAINT fk_agent_prompt
        FOREIGN KEY (system_prompt_id) REFERENCES prompt_definition (id),
    CONSTRAINT fk_agent_prompt_version
        FOREIGN KEY (system_prompt_id, prompt_version)
            REFERENCES prompt_version (prompt_id, version),
    CONSTRAINT ck_agent_prompt_version
        CHECK (prompt_version IS NULL OR prompt_version > 0),
    CONSTRAINT ck_agent_status
        CHECK (status IN ('ENABLED', 'DISABLED', 'ARCHIVED'))
);

COMMENT ON TABLE agent_definition IS 'Agent 静态定义表，配置 Agent 使用的模型、Prompt 和运行参数';
COMMENT ON COLUMN agent_definition.id IS 'Agent 唯一业务标识';
COMMENT ON COLUMN agent_definition.name IS 'Agent 显示名称';
COMMENT ON COLUMN agent_definition.description IS 'Agent 的用途和能力说明';
COMMENT ON COLUMN agent_definition.model_id IS 'Agent 默认使用的模型配置标识，关联 model_config.id';
COMMENT ON COLUMN agent_definition.system_prompt_id IS 'Agent 使用的 System Prompt 标识，关联 prompt_definition.id';
COMMENT ON COLUMN agent_definition.prompt_version IS '固定使用的 Prompt 版本号；为空时使用 Prompt 当前发布版本';
COMMENT ON COLUMN agent_definition.status IS 'Agent 状态：ENABLED-启用，DISABLED-禁用，ARCHIVED-已归档';
COMMENT ON COLUMN agent_definition.runtime_config IS 'Agent 运行扩展配置，包括 Tool、Memory、RAG、策略和迭代次数等';
COMMENT ON COLUMN agent_definition.created_by IS 'Agent 创建人标识';
COMMENT ON COLUMN agent_definition.created_time IS 'Agent 创建时间';
COMMENT ON COLUMN agent_definition.updated_time IS 'Agent 最后更新时间';

CREATE INDEX idx_agent_definition_status
    ON agent_definition (status);

CREATE TABLE chat_session
(
    id                VARCHAR(128) NOT NULL,
    agent_id          VARCHAR(64)  NOT NULL,
    user_id           VARCHAR(64)  NOT NULL,
    title             VARCHAR(255),
    status            VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    metadata          JSONB        NOT NULL DEFAULT '{}'::JSONB,
    last_message_time TIMESTAMPTZ,
    created_time      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chat_session_pkey PRIMARY KEY (id),
    CONSTRAINT fk_chat_session_agent
        FOREIGN KEY (agent_id) REFERENCES agent_definition (id),
    CONSTRAINT ck_chat_session_status
        CHECK (status IN ('ACTIVE', 'CLOSED', 'ARCHIVED'))
);

COMMENT ON TABLE chat_session IS 'Agent 会话表，表示用户与某个 Agent 之间的一次连续对话';
COMMENT ON COLUMN chat_session.id IS '会话唯一标识，可使用调用方生成的业务会话编号';
COMMENT ON COLUMN chat_session.agent_id IS '当前会话使用的 Agent 标识，关联 agent_definition.id';
COMMENT ON COLUMN chat_session.user_id IS '会话所属用户的外部身份标识';
COMMENT ON COLUMN chat_session.title IS '会话标题，可以由用户设置或由模型自动生成';
COMMENT ON COLUMN chat_session.status IS '会话状态：ACTIVE-进行中，CLOSED-已关闭，ARCHIVED-已归档';
COMMENT ON COLUMN chat_session.metadata IS '会话扩展信息，例如来源、客户端、租户和业务上下文';
COMMENT ON COLUMN chat_session.last_message_time IS '该会话最后一条消息的产生时间';
COMMENT ON COLUMN chat_session.created_time IS '会话创建时间';
COMMENT ON COLUMN chat_session.updated_time IS '会话最后更新时间';

CREATE INDEX idx_chat_session_agent_updated_time
    ON chat_session (agent_id, updated_time DESC);
CREATE INDEX idx_chat_session_user_updated_time
    ON chat_session (user_id, updated_time DESC);

CREATE TABLE agent_execution
(
    id                     UUID         NOT NULL DEFAULT gen_random_uuid(),
    request_id             VARCHAR(128),
    agent_id               VARCHAR(64)  NOT NULL,
    session_id             VARCHAR(128),
    user_id                VARCHAR(64)  NOT NULL,
    model_id               VARCHAR(64)  NOT NULL,
    prompt_id              VARCHAR(64)  NOT NULL,
    prompt_version         INTEGER      NOT NULL,
    status                 VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    input_content          TEXT         NOT NULL,
    input_variables        JSONB        NOT NULL DEFAULT '{}'::JSONB,
    output_content         TEXT,
    model_name_snapshot    VARCHAR(128),
    system_prompt_snapshot TEXT,
    prompt_tokens          INTEGER,
    completion_tokens      INTEGER,
    total_tokens           INTEGER,
    duration_ms            BIGINT,
    error_code             VARCHAR(64),
    error_message          TEXT,
    started_time           TIMESTAMPTZ,
    completed_time         TIMESTAMPTZ,
    created_time           TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT agent_execution_pkey PRIMARY KEY (id),
    CONSTRAINT uk_execution_request UNIQUE (request_id),
    CONSTRAINT fk_execution_agent
        FOREIGN KEY (agent_id) REFERENCES agent_definition (id),
    CONSTRAINT fk_execution_session
        FOREIGN KEY (session_id) REFERENCES chat_session (id),
    CONSTRAINT fk_execution_model
        FOREIGN KEY (model_id) REFERENCES model_config (id),
    CONSTRAINT fk_execution_prompt_version
        FOREIGN KEY (prompt_id, prompt_version)
            REFERENCES prompt_version (prompt_id, version),
    CONSTRAINT ck_execution_status
        CHECK (status IN ('PENDING', 'RUNNING', 'SUCCEEDED', 'FAILED', 'CANCELLED')),
    CONSTRAINT ck_execution_prompt_tokens
        CHECK (prompt_tokens IS NULL OR prompt_tokens >= 0),
    CONSTRAINT ck_execution_completion_tokens
        CHECK (completion_tokens IS NULL OR completion_tokens >= 0),
    CONSTRAINT ck_execution_total_tokens
        CHECK (total_tokens IS NULL OR total_tokens >= 0),
    CONSTRAINT ck_execution_duration
        CHECK (duration_ms IS NULL OR duration_ms >= 0)
);

COMMENT ON TABLE agent_execution IS 'Agent 单次执行记录表，保存一次 Runtime 调用的输入、输出、状态和审计信息';
COMMENT ON COLUMN agent_execution.id IS 'Agent 执行记录唯一标识，对应平台 executionId';
COMMENT ON COLUMN agent_execution.request_id IS '调用方提供的幂等请求标识，用于避免同一请求重复执行';
COMMENT ON COLUMN agent_execution.agent_id IS '本次执行使用的 Agent 标识，关联 agent_definition.id';
COMMENT ON COLUMN agent_execution.session_id IS '本次执行所属的会话标识，关联 chat_session.id';
COMMENT ON COLUMN agent_execution.user_id IS '本次执行发起用户的外部身份标识';
COMMENT ON COLUMN agent_execution.model_id IS '本次执行实际使用的模型配置标识，关联 model_config.id';
COMMENT ON COLUMN agent_execution.prompt_id IS '本次执行实际使用的 Prompt 标识';
COMMENT ON COLUMN agent_execution.prompt_version IS '本次执行实际使用的 Prompt 版本号';
COMMENT ON COLUMN agent_execution.status IS '执行状态：PENDING-等待，RUNNING-执行中，SUCCEEDED-成功，FAILED-失败，CANCELLED-已取消';
COMMENT ON COLUMN agent_execution.input_content IS '本次执行接收到的用户输入内容';
COMMENT ON COLUMN agent_execution.input_variables IS '本次请求携带的动态上下文变量，使用 JSON 格式保存';
COMMENT ON COLUMN agent_execution.output_content IS 'Agent 本次执行生成的最终输出内容';
COMMENT ON COLUMN agent_execution.model_name_snapshot IS '执行时实际模型名称的快照，避免模型配置修改后历史记录失真';
COMMENT ON COLUMN agent_execution.system_prompt_snapshot IS '执行时实际发送给模型的 System Prompt 快照';
COMMENT ON COLUMN agent_execution.prompt_tokens IS '本次模型调用消耗的输入 Token 数量';
COMMENT ON COLUMN agent_execution.completion_tokens IS '本次模型调用产生的输出 Token 数量';
COMMENT ON COLUMN agent_execution.total_tokens IS '本次模型调用消耗的 Token 总数';
COMMENT ON COLUMN agent_execution.duration_ms IS '本次 Agent 执行总耗时，单位为毫秒';
COMMENT ON COLUMN agent_execution.error_code IS '执行失败时的平台错误编码';
COMMENT ON COLUMN agent_execution.error_message IS '执行失败时的错误说明，不应保存密码或密钥等敏感信息';
COMMENT ON COLUMN agent_execution.started_time IS 'Agent 开始执行时间';
COMMENT ON COLUMN agent_execution.completed_time IS 'Agent 执行完成、失败或取消的时间';
COMMENT ON COLUMN agent_execution.created_time IS '执行记录创建时间';

CREATE INDEX idx_agent_execution_agent_status
    ON agent_execution (agent_id, status, created_time DESC);
CREATE INDEX idx_agent_execution_session_created_time
    ON agent_execution (session_id, created_time DESC);
CREATE INDEX idx_agent_execution_user_created_time
    ON agent_execution (user_id, created_time DESC);

CREATE TABLE chat_message
(
    id           UUID         NOT NULL DEFAULT gen_random_uuid(),
    session_id   VARCHAR(128) NOT NULL,
    execution_id UUID,
    sequence_no  BIGINT       NOT NULL,
    role         VARCHAR(20)  NOT NULL,
    content      TEXT         NOT NULL,
    content_type VARCHAR(32)  NOT NULL DEFAULT 'TEXT',
    token_count  INTEGER,
    metadata     JSONB        NOT NULL DEFAULT '{}'::JSONB,
    created_time TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chat_message_pkey PRIMARY KEY (id),
    CONSTRAINT uk_chat_message_sequence UNIQUE (session_id, sequence_no),
    CONSTRAINT fk_chat_message_session
        FOREIGN KEY (session_id) REFERENCES chat_session (id) ON DELETE CASCADE,
    CONSTRAINT fk_chat_message_execution
        FOREIGN KEY (execution_id) REFERENCES agent_execution (id),
    CONSTRAINT ck_chat_message_role
        CHECK (role IN ('SYSTEM', 'USER', 'ASSISTANT', 'TOOL')),
    CONSTRAINT ck_chat_message_content_type
        CHECK (content_type IN ('TEXT', 'JSON', 'IMAGE', 'FILE', 'TOOL_RESULT')),
    CONSTRAINT ck_chat_message_token_count
        CHECK (token_count IS NULL OR token_count >= 0)
);

COMMENT ON TABLE chat_message IS '会话消息表，长期保存用户、助手、系统和工具产生的完整消息';
COMMENT ON COLUMN chat_message.id IS '消息唯一标识';
COMMENT ON COLUMN chat_message.session_id IS '消息所属会话标识，关联 chat_session.id';
COMMENT ON COLUMN chat_message.execution_id IS '产生或消费该消息的 Agent 执行记录标识，关联 agent_execution.id';
COMMENT ON COLUMN chat_message.sequence_no IS '消息在当前会话中的严格递增序号，用于保证消息顺序';
COMMENT ON COLUMN chat_message.role IS '消息角色：SYSTEM-系统，USER-用户，ASSISTANT-助手，TOOL-工具';
COMMENT ON COLUMN chat_message.content IS '消息正文内容';
COMMENT ON COLUMN chat_message.content_type IS '消息内容类型：TEXT、JSON、IMAGE、FILE、TOOL_RESULT';
COMMENT ON COLUMN chat_message.token_count IS '该消息对应的 Token 数量';
COMMENT ON COLUMN chat_message.metadata IS '消息扩展信息，例如附件、图片地址、引用来源和模型信息';
COMMENT ON COLUMN chat_message.created_time IS '消息创建时间';

CREATE INDEX idx_chat_message_execution
    ON chat_message (execution_id) WHERE execution_id IS NOT NULL;
CREATE INDEX idx_chat_message_session_created_time
    ON chat_message (session_id, created_time);
CREATE INDEX idx_chat_message_session_sequence
    ON chat_message (session_id, sequence_no);

CREATE TABLE execution_step
(
    id             UUID         NOT NULL DEFAULT gen_random_uuid(),
    execution_id   UUID         NOT NULL,
    step_no        INTEGER      NOT NULL,
    step_type      VARCHAR(32)  NOT NULL,
    name           VARCHAR(128) NOT NULL,
    status         VARCHAR(20)  NOT NULL DEFAULT 'RUNNING',
    request_data   JSONB        NOT NULL DEFAULT '{}'::JSONB,
    response_data  JSONB        NOT NULL DEFAULT '{}'::JSONB,
    error_message  TEXT,
    duration_ms    BIGINT,
    started_time   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_time TIMESTAMPTZ,
    CONSTRAINT execution_step_pkey PRIMARY KEY (id),
    CONSTRAINT uk_execution_step_no UNIQUE (execution_id, step_no),
    CONSTRAINT fk_execution_step_execution
        FOREIGN KEY (execution_id) REFERENCES agent_execution (id) ON DELETE CASCADE,
    CONSTRAINT ck_execution_step_no CHECK (step_no > 0),
    CONSTRAINT ck_execution_step_type
        CHECK (step_type IN ('POLICY', 'PROMPT', 'MODEL', 'TOOL', 'MEMORY', 'RAG')),
    CONSTRAINT ck_execution_step_status
        CHECK (status IN ('RUNNING', 'SUCCEEDED', 'FAILED', 'SKIPPED')),
    CONSTRAINT ck_execution_step_duration
        CHECK (duration_ms IS NULL OR duration_ms >= 0)
);

COMMENT ON TABLE execution_step IS 'Agent 执行步骤表，记录策略判断、Prompt 组装、模型、工具、Memory 和 RAG 调用过程';
COMMENT ON COLUMN execution_step.id IS '执行步骤唯一标识';
COMMENT ON COLUMN execution_step.execution_id IS '步骤所属的 Agent 执行记录标识，关联 agent_execution.id';
COMMENT ON COLUMN execution_step.step_no IS '步骤在当前 Agent 执行中的顺序编号，从 1 开始递增';
COMMENT ON COLUMN execution_step.step_type IS '步骤类型：POLICY、PROMPT、MODEL、TOOL、MEMORY、RAG';
COMMENT ON COLUMN execution_step.name IS '执行步骤名称，例如权限检查、加载记忆、调用模型或查询订单';
COMMENT ON COLUMN execution_step.status IS '步骤状态：RUNNING-执行中，SUCCEEDED-成功，FAILED-失败，SKIPPED-跳过';
COMMENT ON COLUMN execution_step.request_data IS '步骤请求数据，使用 JSON 格式保存，写入前必须过滤敏感信息';
COMMENT ON COLUMN execution_step.response_data IS '步骤响应数据，使用 JSON 格式保存，写入前必须过滤敏感信息';
COMMENT ON COLUMN execution_step.error_message IS '步骤执行失败时的错误说明';
COMMENT ON COLUMN execution_step.duration_ms IS '步骤执行耗时，单位为毫秒';
COMMENT ON COLUMN execution_step.started_time IS '步骤开始执行时间';
COMMENT ON COLUMN execution_step.completed_time IS '步骤执行完成、失败或跳过的时间';

CREATE INDEX idx_execution_step_execution
    ON execution_step (execution_id, step_no);

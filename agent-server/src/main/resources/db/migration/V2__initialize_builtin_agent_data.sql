-- Agent Platform 内置基础数据。
-- 仅提供最小可运行的模型、Prompt 与 Agent 配置，不包含业务数据。

INSERT INTO model_config (
    id,
    name,
    provider,
    model_name,
    credential_key,
    temperature,
    max_tokens,
    enabled,
    extra_config
) VALUES (
    'default-model',
    '默认 OpenAI 模型',
    'OPENAI',
    'gpt-4o-mini',
    'OPENAI_API_KEY',
    0.700,
    4096,
    TRUE,
    '{}'::JSONB
)
ON CONFLICT (id) DO UPDATE
SET name = EXCLUDED.name,
    provider = EXCLUDED.provider,
    model_name = EXCLUDED.model_name,
    credential_key = EXCLUDED.credential_key,
    temperature = EXCLUDED.temperature,
    max_tokens = EXCLUDED.max_tokens,
    enabled = EXCLUDED.enabled,
    extra_config = EXCLUDED.extra_config,
    updated_time = CURRENT_TIMESTAMP;

INSERT INTO prompt_definition (
    id,
    name,
    description,
    published_version,
    enabled,
    created_by
) VALUES
    (
        'general-system-prompt',
        '通用助手系统提示词',
        '用于通用问答场景的系统提示词',
        NULL,
        TRUE,
        'system'
    ),
    (
        'code-system-prompt',
        '编程助手系统提示词',
        '用于软件开发场景的系统提示词',
        NULL,
        TRUE,
        'system'
    )
ON CONFLICT (id) DO UPDATE
SET name = EXCLUDED.name,
    description = EXCLUDED.description,
    enabled = EXCLUDED.enabled,
    created_by = EXCLUDED.created_by,
    updated_time = CURRENT_TIMESTAMP;

INSERT INTO prompt_version (
    prompt_id,
    version,
    system_text,
    variable_schema,
    status,
    change_description,
    created_by,
    published_time
) VALUES
    (
        'general-system-prompt',
        1,
        '你是一个通用 AI 助手，请使用清晰、准确的语言回答用户问题。',
        '{}'::JSONB,
        'PUBLISHED',
        '初始化内置通用助手提示词',
        'system',
        CURRENT_TIMESTAMP
    ),
    (
        'code-system-prompt',
        1,
        '你是一个专业的软件开发助手，请优先给出可靠、可维护的技术方案。',
        '{}'::JSONB,
        'PUBLISHED',
        '初始化内置编程助手提示词',
        'system',
        CURRENT_TIMESTAMP
    )
ON CONFLICT (prompt_id, version) DO UPDATE
SET system_text = EXCLUDED.system_text,
    variable_schema = EXCLUDED.variable_schema,
    status = EXCLUDED.status,
    change_description = EXCLUDED.change_description,
    created_by = EXCLUDED.created_by,
    published_time = EXCLUDED.published_time;

UPDATE prompt_definition
SET published_version = 1,
    updated_time = CURRENT_TIMESTAMP
WHERE id IN ('general-system-prompt', 'code-system-prompt');

INSERT INTO agent_definition (
    id,
    name,
    description,
    model_id,
    system_prompt_id,
    prompt_version,
    status,
    runtime_config,
    created_by
) VALUES
    (
        'general-agent',
        '通用助手',
        '用于通用问答场景的 Agent',
        'default-model',
        'general-system-prompt',
        NULL,
        'ENABLED',
        '{}'::JSONB,
        'system'
    ),
    (
        'code-agent',
        '编程助手',
        '用于软件开发场景的 Agent',
        'default-model',
        'code-system-prompt',
        NULL,
        'ENABLED',
        '{}'::JSONB,
        'system'
    )
ON CONFLICT (id) DO UPDATE
SET name = EXCLUDED.name,
    description = EXCLUDED.description,
    model_id = EXCLUDED.model_id,
    system_prompt_id = EXCLUDED.system_prompt_id,
    prompt_version = EXCLUDED.prompt_version,
    status = EXCLUDED.status,
    runtime_config = EXCLUDED.runtime_config,
    created_by = EXCLUDED.created_by,
    updated_time = CURRENT_TIMESTAMP;

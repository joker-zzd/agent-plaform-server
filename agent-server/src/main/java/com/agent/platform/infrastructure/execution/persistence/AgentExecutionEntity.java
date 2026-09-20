package com.agent.platform.infrastructure.execution.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Agent 单次执行持久化实体，对应 {@code agent_execution} 表。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("agent_execution")
public class AgentExecutionEntity {

    /** Agent 执行记录唯一标识。 */
    @Id
    private UUID id;

    /** 调用方提供的幂等请求标识。 */
    @Column("request_id")
    private String requestId;

    /** 本次执行使用的 Agent 标识。 */
    @Column("agent_id")
    private String agentId;

    /** 本次执行所属的会话标识。 */
    @Column("session_id")
    private String sessionId;

    /** 本次执行的发起用户标识。 */
    @Column("user_id")
    private String userId;

    /** 本次执行实际使用的模型配置标识。 */
    @Column("model_id")
    private String modelId;

    /** 本次执行实际使用的 Prompt 标识。 */
    @Column("prompt_id")
    private String promptId;

    /** 本次执行实际使用的 Prompt 版本号。 */
    @Column("prompt_version")
    private Integer promptVersion;

    /** 执行状态。 */
    private String status;

    /** 用户输入内容。 */
    @Column("input_content")
    private String inputContent;

    /** 请求携带的动态变量，内容为 JSON。 */
    @Column("input_variables")
    private String inputVariables;

    /** Agent 生成的最终输出内容。 */
    @Column("output_content")
    private String outputContent;

    /** 执行时实际模型名称的快照。 */
    @Column("model_name_snapshot")
    private String modelNameSnapshot;

    /** 执行时实际发送的 System Prompt 快照。 */
    @Column("system_prompt_snapshot")
    private String systemPromptSnapshot;

    /** 模型调用消耗的输入 Token 数量。 */
    @Column("prompt_tokens")
    private Integer promptTokens;

    /** 模型调用产生的输出 Token 数量。 */
    @Column("completion_tokens")
    private Integer completionTokens;

    /** 模型调用消耗的 Token 总数。 */
    @Column("total_tokens")
    private Integer totalTokens;

    /** Agent 执行总耗时，单位为毫秒。 */
    @Column("duration_ms")
    private Long durationMs;

    /** 执行失败时的平台错误编码。 */
    @Column("error_code")
    private String errorCode;

    /** 执行失败时的错误说明。 */
    @Column("error_message")
    private String errorMessage;

    /** 执行开始时间。 */
    @Column("started_time")
    private OffsetDateTime startedTime;

    /** 执行完成、失败或取消的时间。 */
    @Column("completed_time")
    private OffsetDateTime completedTime;

    /** 执行记录创建时间。 */
    @Column("created_time")
    private OffsetDateTime createdTime;
}

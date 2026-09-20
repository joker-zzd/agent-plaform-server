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
 * Agent 执行步骤持久化实体，对应 {@code execution_step} 表。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("execution_step")
public class ExecutionStepEntity {

    /** 执行步骤唯一标识。 */
    @Id
    private UUID id;

    /** 步骤所属的 Agent 执行记录标识。 */
    @Column("execution_id")
    private UUID executionId;

    /** 步骤在当前执行中的顺序编号。 */
    @Column("step_no")
    private Integer stepNo;

    /** 执行步骤类型。 */
    @Column("step_type")
    private String stepType;

    /** 执行步骤名称。 */
    private String name;

    /** 执行步骤状态。 */
    private String status;

    /** 步骤请求数据，内容为 JSON。 */
    @Column("request_data")
    private String requestData;

    /** 步骤响应数据，内容为 JSON。 */
    @Column("response_data")
    private String responseData;

    /** 步骤执行失败时的错误说明。 */
    @Column("error_message")
    private String errorMessage;

    /** 步骤执行耗时，单位为毫秒。 */
    @Column("duration_ms")
    private Long durationMs;

    /** 步骤开始时间。 */
    @Column("started_time")
    private OffsetDateTime startedTime;

    /** 步骤完成、失败或跳过的时间。 */
    @Column("completed_time")
    private OffsetDateTime completedTime;
}

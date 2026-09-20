package com.agent.platform.infrastructure.agent.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

/**
 * Agent 定义持久化实体，对应 {@code agent_definition} 表。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("agent_definition")
public class AgentDefinitionEntity {

    /** Agent 唯一业务标识。 */
    @Id
    @Column("id")
    private String id;

    /** Agent 显示名称。 */
    @Column("name")
    private String name;

    /** Agent 用途和能力说明。 */
    @Column("description")
    private String description;

    /** Agent 默认使用的模型配置标识。 */
    @Column("model_id")
    private String modelId;

    /** Agent 使用的 System Prompt 标识。 */
    @Column("system_prompt_id")
    private String systemPromptId;

    /** 固定使用的 Prompt 版本号，为空时使用当前发布版本。 */
    @Column("prompt_version")
    private Integer promptVersion;

    /** Agent 状态。 */
    @Column("status")
    private String status;

    /** Agent 运行扩展配置，内容为 JSON。 */
    @Column("runtime_config")
    private String runtimeConfig;

    /** 创建人标识。 */
    @Column("created_by")
    private String createdBy;

    /** 创建时间。 */
    @Column("created_time")
    private OffsetDateTime createdTime;

    /** 最后更新时间。 */
    @Column("updated_time")
    private OffsetDateTime updatedTime;
}

package com.agent.platform.infrastructure.prompt.persistence;

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
 * Prompt 版本数据对象，对应 {@code prompt_version} 表。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("prompt_version")
public class PromptVersionDO {

    /** Prompt 版本记录唯一标识。 */
    @Id
    @Column("id")
    private UUID id;

    /** 所属 Prompt 的唯一标识。 */
    @Column("prompt_id")
    private String promptId;

    /** Prompt 版本号。 */
    @Column("version")
    private Integer version;

    /** 当前版本的 System Prompt 正文。 */
    @Column("system_text")
    private String systemText;

    /** 动态变量及其约束，内容为 JSON。 */
    @Column("variable_schema")
    private String variableSchema;

    /** 版本状态。 */
    @Column("status")
    private String status;

    /** 当前版本的变更说明。 */
    @Column("change_description")
    private String changeDescription;

    /** 创建人标识。 */
    @Column("created_by")
    private String createdBy;

    /** 创建时间。 */
    @Column("created_time")
    private OffsetDateTime createdTime;

    /** 正式发布时间。 */
    @Column("published_time")
    private OffsetDateTime publishedTime;
}

package com.agent.platform.infrastructure.prompt.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

/**
 * Prompt 定义数据对象，对应 {@code prompt_definition} 表。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("prompt_definition")
public class PromptDefinitionDO {

    /** Prompt 唯一业务标识。 */
    @Id
    @Column("id")
    private String id;

    /** Prompt 显示名称。 */
    @Column("name")
    private String name;

    /** Prompt 用途和功能说明。 */
    @Column("description")
    private String description;

    /** 当前正式发布的版本号。 */
    @Column("published_version")
    private Integer publishedVersion;

    /** Prompt 是否启用。 */
    @Column("enabled")
    private Boolean enabled;

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

package com.agent.platform.infrastructure.model.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * 模型配置数据对象，对应 {@code model_config} 表。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("model_config")
public class ModelConfigDO {

    /** 平台内部模型唯一标识。 */
    @Id
    @Column("id")
    private String id;

    /** 模型配置显示名称。 */
    @Column("name")
    private String name;

    /** 模型供应商标识。 */
    @Column("provider")
    private String provider;

    /** 模型供应商定义的真实模型名称。 */
    @Column("model_name")
    private String modelName;

    /** 模型服务接口地址。 */
    @Column("base_url")
    private String baseUrl;

    /** 外部密钥标识，不保存密钥明文。 */
    @Column("credential_key")
    private String credentialKey;

    /** 模型生成随机性参数。 */
    @Column("temperature")
    private BigDecimal temperature;

    /** 模型单次调用允许生成的最大 Token 数量。 */
    @Column("max_tokens")
    private Integer maxTokens;

    /** 模型配置是否启用。 */
    @Column("enabled")
    private Boolean enabled;

    /** 模型供应商专属扩展配置，内容为 JSON。 */
    @Column("extra_config")
    private String extraConfig;

    /** 创建时间。 */
    @Column("created_time")
    private OffsetDateTime createdTime;

    /** 最后更新时间。 */
    @Column("updated_time")
    private OffsetDateTime updatedTime;
}

package com.agent.platform.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 平台模型定义。
 *
 * <p>描述一次模型调用所需的供应商、真实模型名称和生成参数，
 * 不包含数据库框架或 Spring AI 类型。</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelDefinition {

    /** 平台内部模型唯一标识。 */
    private String id;

    /** 模型供应商标识。 */
    private String provider;

    /** 模型供应商定义的真实模型名称。 */
    private String modelName;

    /** 模型服务接口地址，为空时使用客户端默认地址。 */
    private String baseUrl;

    /** 模型密钥对应的环境变量或外部配置标识。 */
    private String credentialKey;

    /** 模型生成随机性参数。 */
    private BigDecimal temperature;

    /** 模型单次调用允许生成的最大 Token 数量。 */
    private Integer maxTokens;
}

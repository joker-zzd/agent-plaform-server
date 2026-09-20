package com.agent.platform.infrastructure.model.springai;

import com.agent.platform.core.exception.ModelConfigurationException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 从 Spring Environment 解析模型凭据引用。
 *
 * <p>数据库只保存配置键，不保存或返回密钥明文。</p>
 */
@Component
@RequiredArgsConstructor
public class EnvironmentModelCredentialResolver {

    private final Environment environment;

    /**
     * 根据配置键读取模型凭据。
     *
     * @param credentialKey 环境变量或外部配置键
     * @return 模型凭据
     */
    public String getRequired(String credentialKey) {
        if (!StringUtils.hasText(credentialKey)) {
            throw new ModelConfigurationException("模型凭据引用不能为空");
        }
        String credential = environment.getProperty(credentialKey);
        if (!StringUtils.hasText(credential)) {
            throw new ModelConfigurationException("模型凭据未配置，配置键：" + credentialKey);
        }
        return credential;
    }
}

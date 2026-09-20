package com.agent.platform.core.exception;

/**
 * 模型配置无效异常。
 *
 * <p>用于模型供应商不受支持、关键配置缺失等无法继续调用模型的场景。</p>
 */
public class ModelConfigurationException extends RuntimeException {

    /**
     * 创建模型配置异常。
     *
     * @param message 不包含密钥等敏感信息的异常说明
     */
    public ModelConfigurationException(String message) {
        super(message);
    }
}

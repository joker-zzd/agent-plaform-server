package com.agent.platform.infrastructure.persistence.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PostgreSQL JSONB 字段的数据载体。
 *
 * <p>使用独立类型隔离普通字符串与 JSONB 字段，避免全局字符串转换影响其它列。</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonbValue {

    /** JSON 文本。 */
    private String value;
}

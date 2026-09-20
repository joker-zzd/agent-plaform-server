package com.agent.platform.infrastructure.persistence.json;

import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.sql.SQLException;

/**
 * 将平台 JSONB 数据载体转换为 PostgreSQL JSONB 值。
 */
@WritingConverter
public class JsonbWritingConverter implements Converter<JsonbValue, PGobject> {

    private static final String JSONB_TYPE = "jsonb";

    /**
     * 创建 PostgreSQL 驱动可识别的 JSONB 对象。
     *
     * @param source JSONB 数据载体
     * @return PostgreSQL 数据对象
     */
    @Override
    public PGobject convert(JsonbValue source) {
        PGobject target = new PGobject();
        target.setType(JSONB_TYPE);
        try {
            target.setValue(source.getValue());
            return target;
        } catch (SQLException exception) {
            throw new IllegalArgumentException("JSONB 数据转换失败", exception);
        }
    }
}

package com.agent.platform.infrastructure.persistence.json;

import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * 将 PostgreSQL JSONB 值转换为平台 JSONB 数据载体。
 */
@ReadingConverter
public class JsonbReadingConverter implements Converter<PGobject, JsonbValue> {

    /**
     * 转换数据库驱动返回的 JSONB 对象。
     *
     * @param source PostgreSQL 数据对象
     * @return JSONB 数据载体
     */
    @Override
    public JsonbValue convert(PGobject source) {
        return new JsonbValue(source.getValue());
    }
}

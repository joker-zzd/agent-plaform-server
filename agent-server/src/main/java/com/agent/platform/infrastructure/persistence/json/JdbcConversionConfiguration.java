package com.agent.platform.infrastructure.persistence.json;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.dialect.PostgresDialect;

import java.util.List;

/**
 * Spring Data JDBC 自定义类型转换配置。
 */
@Configuration
public class JdbcConversionConfiguration {

    /**
     * 注册 PostgreSQL JSONB 的读写转换器。
     *
     * @param dialect 当前数据库方言
     * @return JDBC 自定义转换集合
     */
    @Bean
    public JdbcCustomConversions jdbcCustomConversions(Dialect dialect) {
        if (dialect instanceof PostgresDialect) {
            return JdbcCustomConversions.of(dialect, List.of(
                    new JsonbReadingConverter(),
                    new JsonbWritingConverter()
            ));
        }
        return JdbcCustomConversions.of(dialect, List.of());
    }
}

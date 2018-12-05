package com.kaihei.esportingplus.payment.config.datasource;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author: tangtao
 **/
@ConditionalOnProperty(name = "migrate.enable", havingValue = "true")
@Configuration
public class JdbcTemplatConfig {

    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("shardingDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

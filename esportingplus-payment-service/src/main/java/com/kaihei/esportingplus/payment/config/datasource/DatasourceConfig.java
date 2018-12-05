package com.kaihei.esportingplus.payment.config.datasource;

import io.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author tangtao
 */
@Configuration
public class DatasourceConfig extends SpringBootConfiguration {


    /**
     * 覆盖Sharding的dataSource
     */
    @Primary
    @Bean("shardingDataSource")
    @Override
    public DataSource dataSource() throws SQLException {
        return super.dataSource();
    }

    @ConditionalOnProperty(name = "migrate.enable", havingValue = "true")
    @Bean("externalDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.external")
    public DataSource externalDataSource() {
        return DataSourceBuilder.create().build();
    }
}

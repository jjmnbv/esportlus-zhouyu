package com.kaihei.esportingplus.payment.config.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

/**
 * @author tangtao
 */

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.kaihei.esportingplus.payment.data.jpa.repository")
public class ShardingJpaConfig {

    @Autowired
    @Qualifier("shardingDataSource")
    private DataSource shardingDataSource;

    @Resource
    private JpaProperties jpaProperties;

    @Primary
    @Bean
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactory(builder).getObject().createEntityManager();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(shardingDataSource)
                .properties(jpaProperties.getHibernateProperties(shardingDataSource))
                .packages("com.kaihei.esportingplus.payment.domain.entity")
                .persistenceUnit("shardingPersistenceUnit")
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactory(builder).getObject());
    }
}

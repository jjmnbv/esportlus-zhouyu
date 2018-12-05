package com.kaihei.esportingplus.payment.config.datasource;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author tangtao
 */

@ConditionalOnProperty(name = "migrate.enable", havingValue = "true")
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryExternal",
        transactionManagerRef = "transactionManagerExternal",
        basePackages = "com.kaihei.esportingplus.payment.migrate.repository")
public class ExternalJpaConfig {

    @Autowired
    @Qualifier("externalDataSource")
    private DataSource externalDataSource;

    @Resource
    private JpaProperties jpaProperties;

    @Bean
    public EntityManager entityManagerExternal(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryExternal(builder).getObject().createEntityManager();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryExternal(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(externalDataSource)
                .properties(jpaProperties.getHibernateProperties(externalDataSource))
                .packages("com.kaihei.esportingplus.payment.migrate.entity")
                .persistenceUnit("migratePersistenceUnit")
                .build();
    }

    @Bean
    PlatformTransactionManager transactionManagerExternal(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryExternal(builder).getObject());
    }
}

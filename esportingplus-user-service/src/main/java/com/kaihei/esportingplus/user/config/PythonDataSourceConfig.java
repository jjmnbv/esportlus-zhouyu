package com.kaihei.esportingplus.user.config;

import com.alibaba.druid.pool.DruidDataSource;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * python数据源配置
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/12/3 18:04
 */
@Configuration
@MapperScan(basePackages = PythonDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "pythonSqlSessionFactory")
public class PythonDataSourceConfig {

    /**
     * python repository 目录
     */
    static final String PACKAGE = "com.kaihei.esportingplus.user.data.pyrepository";
    static final String MAPPER_LOCATION = "classpath:META-INF/pymappers/*.xml";

    @Value("${spring.python.datasource.url}")
    private String url;

    @Value("${spring.python.datasource.username}")
    private String user;

    @Value("${spring.python.datasource.password}")
    private String password;

    @Value("${spring.python.datasource.driver-class-name}")
    private String driverClass;

    @Bean(name = "pythonDataSource")
    @Primary
    public DataSource pythonDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "pythonTransactionManager")
    @Primary
    public DataSourceTransactionManager pythonTransactionManager() {
        return new DataSourceTransactionManager(pythonDataSource());
    }

    @Bean(name = "pythonSqlSessionFactory")
    @Primary
    public SqlSessionFactory pythonSqlSessionFactory()
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(pythonDataSource());
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(PythonDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }

}

//package com.kaihei.esportingplus.auth.server.config;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.alibaba.druid.support.http.StatViewServlet;
//import com.alibaba.druid.support.http.WebStatFilter;
//import com.alibaba.druid.wall.WallConfig;
//import com.alibaba.druid.wall.WallFilter;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import javax.sql.DataSource;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
///**
// * 阿里数据源配置
// *
// * @author Orochi-Yzh
// * @dateTime 2018/3/15 17:40
// * @updatetor
// */
//@Configuration
//@EnableTransactionManagement
//public class DruidConfig {
//
//    private Logger logger = LoggerFactory.getLogger(DruidConfig.class);
//
//    @Value("${spring.datasource.url}")
//    private String dbUrl;
//
//    @Value("${spring.datasource.username}")
//    private String username;
//
//    @Value("${spring.datasource.password}")
//    private String password;
//
//    @Value("${spring.datasource.driver-class-name}")
//    private String driverClassName;
//
//    @Value("${spring.datasource.initialSize}")
//    private int initialSize;
//
//    @Value("${spring.datasource.minIdle}")
//    private int minIdle;
//
//    @Value("${spring.datasource.maxActive}")
//    private int maxActive;
//
//    @Value("${spring.datasource.maxWait}")
//    private int maxWait;
//
//    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
//    private int timeBetweenEvictionRunsMillis;
//
//    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
//    private int minEvictableIdleTimeMillis;
//
//    @Value("${spring.datasource.validationQuery}")
//    private String validationQuery;
//
//    @Value("${spring.datasource.testWhileIdle}")
//    private boolean testWhileIdle;
//
//    @Value("${spring.datasource.testOnBorrow}")
//    private boolean testOnBorrow;
//
//    @Value("${spring.datasource.testOnReturn}")
//    private boolean testOnReturn;
//
//    @Value("${spring.datasource.filters}")
//    private String filters;
//
//    @Value("${spring.datasource.logSlowSql}")
//    private String logSlowSql;
//
//
//    @Bean
//    public ServletRegistrationBean druidServlet() {
//        ServletRegistrationBean reg = new ServletRegistrationBean();
//        reg.setServlet(new StatViewServlet());
//        reg.addUrlMappings("/druid/*");
//        reg.addInitParameter("loginUsername", "admin");
//        reg.addInitParameter("loginPassword", "admin");
//        reg.addInitParameter("logSlowSql", logSlowSql);
//        return reg;
//    }
//
//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//        filterRegistrationBean.setFilter(new WebStatFilter());
//        filterRegistrationBean.addUrlPatterns("/*");
//        filterRegistrationBean
//                .addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
//        filterRegistrationBean.addInitParameter("profileEnable", "true");
//        return filterRegistrationBean;
//    }
//
//    @Bean(name = "druidDataSource")
//    public DataSource druidDataSource() {
//        DruidDataSource datasource = new DruidDataSource();
//        datasource.setUrl(dbUrl);
//        datasource.setUsername(username);
//        datasource.setPassword(password);
//        datasource.setDriverClassName(driverClassName);
//        datasource.setInitialSize(initialSize);
//        datasource.setMinIdle(minIdle);
//        datasource.setMaxActive(maxActive);
//        datasource.setMaxWait(maxWait);
//        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
//        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
//        datasource.setValidationQuery(validationQuery);
//        datasource.setTestWhileIdle(testWhileIdle);
//        datasource.setTestOnBorrow(testOnBorrow);
//        datasource.setTestOnReturn(testOnReturn);
//        try {
//            datasource.setFilters(filters);
//
//            List filterList = new ArrayList<>();
//            filterList.add(wallFilter());
//            datasource.setProxyFilters(filterList);
//        } catch (SQLException e) {
//            logger.error("druid configuration init filter", e);
//        }
//        return datasource;
//    }
//
//    @Bean
//    public WallFilter wallFilter() {
//        WallFilter wallFilter = new WallFilter();
//        wallFilter.setConfig(wallConfig());
//        return wallFilter;
//
//    }
//
//    @Bean
//    public WallConfig wallConfig() {
//        WallConfig config = new WallConfig();
//        //允许一次执行多条语句
//        config.setMultiStatementAllow(true);
//        //允许非基本语句的其他语句
//        config.setNoneBaseStatementAllow(true);
//        return config;
//
//    }
//
//}
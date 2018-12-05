package com.kaihei.esportingplus.marketing.config;

import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import java.util.Properties;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zhouyu
 * @Date: 2018/9/18 18:13
 * @Description: redis缓存配置
 */
@Configuration
public class RedisConfig {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Value("${redis.nodes:[\"127.0.0.1:6379\"]}")
    private String redisNodes;

    @Value("${redis.cluster-model:single}")
    private String clusterModel;

    @Value("${redis.password:}")
    private String password;

    @Value("${redis.database:0}")
    private int database;

    @Value("${redis.config.maxTotal:500}")
    private int maxTotal;

    @Value("${redis.config.maxIdle:10}")
    private int maxIdle;

    @Value("${redis.config.minIdle:1}")
    private int minIdle;

    @Value("${redis.config.maxWaitMillis:5000}")
    private int maxWaitMillis;

    @Value("${redis.config.testOnBorrow:false}")
    private String testOnBorrow;


    @PostConstruct
    public void initCacheManager() {
        Properties prop = new Properties();
        prop.setProperty("redis.cluster-model", clusterModel);
        prop.setProperty("redis.nodes", redisNodes);
        prop.setProperty("redis.password", password);
        prop.setProperty("redis.maxTotal", String.valueOf(maxTotal));
        prop.setProperty("redis.minIdle", String.valueOf(minIdle));
        prop.setProperty("redis.maxIdle", String.valueOf(maxIdle));
        prop.setProperty("redis.maxWaitMillis", String.valueOf(maxWaitMillis));
        prop.setProperty("redis.evictorShutdownTimeoutMillis", "60000");
        prop.setProperty("redis.testOnBorrow", testOnBorrow);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("redis properties: {}", prop);
        }
        CacheManagerFactory.create().init(prop);
    }

}

package com.kaihei.esportingplus.user.config;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiekeqing
 * @Title: PythonRedisConfig
 * @Description: 连接python缓存配置，目前token、验证码需与python公用redis
 * @date 2018/9/229:39
 */
@Configuration
public class PythonRedisConfig {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${python.redis.nodes:[\"redis:6379\"]}")
    private String redisNodes;

    @Value("${python.redis.cluster-model:single}")
    private String clusterModel;

    @Value("${python.redis.password:}")
    private String password;

    @Value("${python.redis.database:0}")
    private int database;

    @Value("${python.redis.config.maxTotal:500}")
    private int maxTotal;

    @Value("${python.redis.config.maxIdle:10}")
    private int maxIdle;

    @Value("${python.redis.config.minIdle:1}")
    private int minIdle;

    @Value("${python.redis.config.maxWaitMillis:5000}")
    private int maxWaitMillis;

    @Value("${python.redis.config.testOnBorrow:false}")
    private String testOnBorrow;


    @Bean(name = "pythonCacheManager")
    public CacheManager cacheManager() {
        Properties prop = new Properties();
        prop.setProperty("redis.cluster-model", clusterModel);
        prop.setProperty("redis.nodes", redisNodes);
        prop.setProperty("redis.password", password);
        prop.setProperty("redis.database", String.valueOf(database));
        prop.setProperty("redis.maxTotal", String.valueOf(maxTotal));
        prop.setProperty("redis.minIdle", String.valueOf(minIdle));
        prop.setProperty("redis.maxIdle", String.valueOf(maxIdle));
        prop.setProperty("redis.maxWaitMillis", String.valueOf(maxWaitMillis));
        prop.setProperty("redis.evictorShutdownTimeoutMillis", "60000");
        prop.setProperty("redis.testOnBorrow", testOnBorrow);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("python redis properties: {}", prop);
        }

        CacheManager pythonCacheManager = new com.kaihei.commons.cache.redis.manager.RedisManager();
        pythonCacheManager.init(prop);

        return pythonCacheManager;
    }

}

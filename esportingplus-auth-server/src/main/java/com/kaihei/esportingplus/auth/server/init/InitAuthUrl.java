package com.kaihei.esportingplus.auth.server.init;

import static com.kaihei.esportingplus.common.constant.RedisKey.NEED_AUTH_URLS;
import com.kaihei.esportingplus.auth.server.config.OauthUrlsConfig;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class InitAuthUrl {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

//    @Autowired
//    private IAuthRecourcesService authRecourcesService;

    @Autowired
    private OauthUrlsConfig oauthUrlsConfig;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * @Description: 系统启动的时候预热接口url到redis
     * @author Orochi-Yzh
     * @dateTime 2018/8/4 11:33
     */
    public void init() {
        LOGGER.info("初始化接口url权限到缓存....");
        RedisConnection connection = stringRedisTemplate.getConnectionFactory().getConnection();
        try {
            List<String> authUrls = oauthUrlsConfig.getUrls();
            if (CollectionUtils.isNotEmpty(authUrls)) {
                connection.openPipeline();
                String[] urls = authUrls.toArray(new String[]{});
                stringRedisTemplate.opsForSet().add(NEED_AUTH_URLS, urls);
            } else {
                throw new BusinessException(BizExceptionEnum.INIT_AUTHURL_ERROR);
            }
            connection.openPipeline();
        } catch (Exception e) {
            throw new BusinessException(BizExceptionEnum.INIT_AUTHURL_ERROR, e);
        }finally {
            connection.close();
        }
        LOGGER.info("权限缓存完毕");

    }
}

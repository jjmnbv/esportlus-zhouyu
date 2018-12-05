package com.kaihei.esportingplus.core.data.manager;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.core.constant.CoreRediskey;
import org.springframework.stereotype.Component;

/**
 * @Author liuyang
 * @Description
 * @Date 2018/11/14 12:11
 **/
@Component
public class RonyunUserTokenCacheManagerImpl implements RonyunUserTokenCacheManager {

    CacheManager cacheManager = CacheManagerFactory.create();

    @Override
    public boolean saveToken(String user, String token) {
        String key = String.format(CoreRediskey.RonyunKey.USER_TOKEN, user);
        cacheManager.set(key, token);
        return true;
    }

    @Override
    public String getToken(String user) {
        String key = String.format(CoreRediskey.RonyunKey.USER_TOKEN, user);
        return cacheManager.get(key, String.class);
    }
}

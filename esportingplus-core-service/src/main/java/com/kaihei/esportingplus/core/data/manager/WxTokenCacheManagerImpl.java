package com.kaihei.esportingplus.core.data.manager;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.core.constant.CoreRediskey;
import org.springframework.stereotype.Component;

/**
 * @Author liuyang
 * @Description 微信tokencache
 * @Date 2018/11/8 17:03
 **/
@Component
public class WxTokenCacheManagerImpl implements WxTokenCacheManager {

    CacheManager cacheManager = CacheManagerFactory.create();

    @Override
    public void setToken(String token, int expire) {
        cacheManager.set(CoreRediskey.WxKey.ACCESS_TOKEN, token, expire);
    }

    @Override
    public String getToken() {
        return cacheManager.get(CoreRediskey.WxKey.ACCESS_TOKEN, String.class);
    }
}

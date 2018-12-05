package com.kaihei.esportingplus.core.data.manager;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.core.constant.CoreRediskey;
import org.springframework.stereotype.Component;

/**
 * @Author liuyang
 * @Description
 * @Date 2018/10/25 16:39
 **/
@Component
public class VerificationCodeCacheManagerImpl implements VerificationCodeCacheManager {

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    @Override
    public void setPhoneCode(String phone, String code) {
        cacheManager.set(String.format(CoreRediskey.SmsKey.VERIFICATION_CODE, phone), code, 3 * 60);
    }

    @Override
    public String getCodeByPhone(String phone) {
        return cacheManager.get(String.format(CoreRediskey.SmsKey.VERIFICATION_CODE, phone), String.class);
    }
}

package com.kaihei.esportingplus.user.data.manager;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import org.springframework.stereotype.Service;

@Service
public class Demo {

    protected static final CacheManager cacheManager = CacheManagerFactory.create();
}

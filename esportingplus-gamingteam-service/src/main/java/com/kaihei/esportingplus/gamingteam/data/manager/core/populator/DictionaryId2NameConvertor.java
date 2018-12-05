package com.kaihei.esportingplus.gamingteam.data.manager.core.populator;

import com.kaihei.esportingplus.api.feign.DictionaryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

/**
 * 字典id -> 字典名 转换器
 */
@Component
public class DictionaryId2NameConvertor {

    private final String cacheName = "DictId2NameCache";
    @Autowired
    private DictionaryClient dictionaryClient;
    private ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager(
            cacheName);

    public String converte(Integer id) {
        Cache cache = cacheManager.getCache(cacheName);
        String name = cache.get(id, String.class);
        if (name == null) {
            name = dictionaryClient.findById(id).getData().getName();
            cache.put(id, name);
        }
        return name;
    }
}

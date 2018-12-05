package com.kaihei.esportingplus.core.data.manager;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.core.constant.CoreRediskey;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author liuyang
 * @Description
 * @Date 2018/11/15 14:32
 **/
@Component
public class WhiteListManagerImpl implements WhiteListManager {

    private CacheManager cacheManager = CacheManagerFactory.create();

    @Override
    public Collection<String> queryWhiteList() {
       return cacheManager.sMembers(CoreRediskey.WhiteListKey.USER_WHITELIST);
    }

    @Override
    public List<String> inWhiteList(Collection<String> uids) {
        Collection<String> whiteList = this.queryWhiteList();
        return uids.parallelStream().filter(s -> whiteList.contains(s)).collect(Collectors.toList());
    }
}

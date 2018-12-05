package com.kaihei.esportingplus.gamingteam.data.manager.cache;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PVPCompositeCacheManager {

    @Autowired
    private List<PVPCacheManager> cacheManagerList;

    /**
     * 通过teamSequence移除缓存
     */
    public void removeCache(String teamSequence) {
        cacheManagerList.forEach(cm -> cm.removeCache(teamSequence));
    }
}

package com.kaihei.esportingplus.gamingteam.data.manager.core.populator;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RedisKey;
import java.util.Iterator;
import org.springframework.stereotype.Component;

/**
 * @author 谢思勇
 *
 * 房间号生成器
 */
@Component
public class RoomNumberGenerator implements Iterator<Integer> {

    private final Integer THRESHOLD = 8999;
    private CacheManager cacheManager = CacheManagerFactory.create();
    private String redisKey = RedisKey.TEAM_ROOM_NUM_SET + ":incr";

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Integer next() {
        long incr = cacheManager.incr(redisKey);
        if (incr >= THRESHOLD) {
            cacheManager.decrBy(redisKey, 8999);
        }
        while (incr < 0) {
            incr += 8999;
        }
        return (int) (incr % 8999) + 1000;
    }
}

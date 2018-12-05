package com.kaihei.esportingplus.trade.event;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.event.EventConsumer;
import com.kaihei.esportingplus.common.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SendUpdatedEventConsumer extends EventConsumer {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    /**
     * 发送暴鸡收益到工作室
     * @param event
     */
    @Subscribe
    @AllowConcurrentEvents //开启线程安全
    public void sendUpdated(SendUpdatedEvent event) {
        LOGGER.info("异步设置车队[{}]所有队员订单更新完成。",event.getTeamSequence());
        cacheManager.set(RedisKey.UPDATED_ORDER + event.getTeamSequence(), StringUtils.EMPTY,120);
    }

}

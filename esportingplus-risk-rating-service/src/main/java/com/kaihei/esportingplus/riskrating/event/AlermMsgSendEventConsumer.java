package com.kaihei.esportingplus.riskrating.event;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.kaihei.esportingplus.common.event.EventConsumer;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.riskrating.service.impl.RiskRatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 更新车队信息时事件消费
 *
 * @author zhangfang
 */
@Component
public class AlermMsgSendEventConsumer extends EventConsumer{
    private static final Logger LOGGER = LoggerFactory.getLogger(AlermMsgSendEventConsumer.class);

    @Autowired
    private RiskRatingService riskRatingService;
    /**
     * 将车队信息缓存到 redis 中
     */
    @Subscribe
    @AllowConcurrentEvents
    @Transactional(rollbackFor = Exception.class)
    public void updateTeamPostition(AlermMsgSendEvent event) {
        String msg = JacksonUtils.toJson(event);
        LOGGER.warn(">>发送风险控制顶顶告警，告警内容为:{}",msg);
        riskRatingService.send(msg);
    }
}

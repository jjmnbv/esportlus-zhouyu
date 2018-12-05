package com.kaihei.esportingplus.marketing.mq.consumer;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.gamingteam.api.mq.PVPFreeTeamEndOrderMessage;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamMemberVO;
import com.kaihei.esportingplus.marketing.api.event.TeamFinishOrderEvent;
import com.kaihei.esportingplus.marketing.api.event.UserEventHandler;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import java.util.Arrays;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: chen.junyong
 * @Date: 2018-12-03 10:53
 * @Description:
 */
@MQConsumer(topic = RocketMQConstant.TOPIC_PVP_FREE, tag = RocketMQConstant.UPDATE_ORDER_STATUS_TAG,
        consumerGroup = RocketMQConstant.FRIEND_FINISH_ORDER_CONSUMER_PVP_FREE_GROUP)
public class FriendFinishOrderConsumer extends AbstractMQPushConsumer<PVPFreeTeamEndOrderMessage> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private CacheManager cacheManager = CacheManagerFactory.create();

    @Resource(name = "teamFreeFinishEventHandler")
    UserEventHandler teamFreeFinishEventHandler;

    @Override
    public boolean process(PVPFreeTeamEndOrderMessage message,
            Map<String, Object> map) {
        Object msg_id = map.get("MSG_ID") + "_" + message.hashCode();
        String teamSequence = message.getTeamSequence();
        Boolean repeatConsume = cacheManager
                .exists(RedisKey.FRIEND_FINISH_GAMETEAM_ORDER + msg_id);
        if (repeatConsume != null && repeatConsume) {
            logger.info(
                    "cmd=FriendFinishOrderConsumer.process | msg=重复收到好友完成车队mq消费请求, 忽略。车队={}, MQ消息id={}",
                    teamSequence,
                    msg_id);
            return true;
        }
        try {

            teamFreeFinishEventHandler.handle(BeanMapper.map(message, TeamFinishOrderEvent.class));
        } catch (Exception e) {
            logger.error(
                    "cmd=FriendFinishOrderConsumer.process | msg=好友完成车队事件异常 | message={} | exception={}",
                    message, e);
            return false;
        }
        cacheManager.set(RedisKey.FRIEND_FINISH_GAMETEAM_ORDER + msg_id, StringUtils.EMPTY, 1800);
        return true;
    }

}

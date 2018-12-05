package com.kaihei.esportingplus.user.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.sensors.service.SensorsAnalyticsService;
import com.kaihei.esportingplus.user.api.vo.RegistTrackSignUpMessageVo;
import com.kaihei.esportingplus.user.constant.MembersAuthConstants;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import com.maihaoche.starter.mq.base.MessageExtConst;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther: chen.junyong
 * @Date: 2018-09-18 15:36
 * @Description: 注册事件消费类
 */
@MQConsumer(consumerGroup = MembersAuthConstants.USER_MQ_REGIST_LOGIN_TRACKSIGNUP_CONSUMER_GROUP_KEY,
        topic = MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
        tag = MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_TAG_SASIGNUP_KEY)
public class RegistTrackSignUpConsumer extends AbstractMQPushConsumer<RegistTrackSignUpMessageVo> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SensorsAnalyticsService sensorsAnalyticsService;

    private CacheManager cacheManager = CacheManagerFactory.create();

    @Override
    public boolean process(RegistTrackSignUpMessageVo message, Map<String, Object> extMap) {
        logger.info(
                "cmd=RegistTrackSignUpConsumer.process | msg=收到mq消费请求, req={}, Topic={}, tags={}, keys={}",
                JSON.toJSON(message), extMap.get(MessageExtConst.PROPERTY_TOPIC),
                extMap.get(MessageExtConst.PROPERTY_TAGS),
                extMap.get(MessageExtConst.PROPERTY_KEYS));

        if (cacheManager.exists(extMap.get(MessageExtConst.PROPERTY_KEYS).toString())) {
            logger.info(
                    "cmd=UpdateRtokenConsumer.process | msg=重复收到注册神策消费请求,忽略 req={}, Topic={}, tags={}, keys={}",
                    JSON.toJSON(message), extMap.get(MessageExtConst.PROPERTY_TOPIC),
                    extMap.get(MessageExtConst.PROPERTY_TAGS),
                    extMap.get(MessageExtConst.PROPERTY_KEYS));
            return true;
        }

        sensorsAnalyticsService
                .trackSignUp(message.getUid(), message.getSaDistinctId());
        cacheManager.set(extMap.get(MessageExtConst.PROPERTY_KEYS).toString(), "", 3600);
        return true;
    }
}

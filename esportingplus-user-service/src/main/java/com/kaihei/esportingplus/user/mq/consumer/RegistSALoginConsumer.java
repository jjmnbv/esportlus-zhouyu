package com.kaihei.esportingplus.user.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.sensors.service.SensorsAnalyticsService;
import com.kaihei.esportingplus.user.api.vo.RegistSALoginMessageVo;
import com.kaihei.esportingplus.user.constant.MembersAuthConstants;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import com.maihaoche.starter.mq.base.MessageExtConst;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther: chen.junyong
 * @Date: 2018-09-18 15:36
 * @Description: 设置神策用户profile, 神策上报消费类
 */
@MQConsumer(consumerGroup = MembersAuthConstants.USER_MQ_REGIST_LOGIN_SALOGIN_CONSUMER_GROUP_KEY,
        topic = MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
        tag = MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_TAG_SALOGIN_KEY)
public class RegistSALoginConsumer extends AbstractMQPushConsumer<RegistSALoginMessageVo> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SensorsAnalyticsService sensorsAnalyticsService;

    private CacheManager cacheManager = CacheManagerFactory.create();

    @Override
    public boolean process(RegistSALoginMessageVo message, Map<String, Object> extMap) {
        logger.info(
                "cmd=RegistSALoginConsumer.process | msg=收到mq消费请求, req={}, Topic={}, tags={}, keys={}",
                JSON.toJSON(message), extMap.get(MessageExtConst.PROPERTY_TOPIC),
                extMap.get(MessageExtConst.PROPERTY_TAGS),
                extMap.get(MessageExtConst.PROPERTY_KEYS));

        if (cacheManager.exists(extMap.get(MessageExtConst.PROPERTY_KEYS).toString())) {
            logger.info(
                    "cmd=RegistSALoginConsumer.process | msg=重复收到神策登录消费请求,忽略 req={}, Topic={}, tags={}, keys={}",
                    JSON.toJSON(message), extMap.get(MessageExtConst.PROPERTY_TOPIC),
                    extMap.get(MessageExtConst.PROPERTY_TAGS),
                    extMap.get(MessageExtConst.PROPERTY_KEYS));
            return true;
        }

        HashMap<String, Object> profileSetMap = new HashMap<>();
        profileSetMap.put("is_login_id", true);
        profileSetMap.put("last_login_version", message.getVersion());
        profileSetMap.put("last_login_time", message.getLastLogin());
        profileSetMap.put("login_channel", message.getChannel());
        sensorsAnalyticsService.profileSet(message.getUid(), profileSetMap);

        HashMap<String, Object> trackMap = new HashMap<>();
        trackMap.put("login_type", RegistConsumer.TYPE_MAP.get(message.getPlatform()));
        trackMap.put("login_channel", message.getChannel());
        sensorsAnalyticsService.track(message.getUid(),
                MembersAuthConstants.USER_EVENT_NAME_LOGIN, trackMap);
        cacheManager.set(extMap.get(MessageExtConst.PROPERTY_KEYS).toString(), "", 3600);
        return true;
    }
}

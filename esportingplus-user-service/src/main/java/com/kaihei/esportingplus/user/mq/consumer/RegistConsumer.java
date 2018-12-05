package com.kaihei.esportingplus.user.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.sensors.service.SensorsAnalyticsService;
import com.kaihei.esportingplus.user.api.vo.RegistingMessageVo;
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
 * @Description: 注册事件消费类
 */
@MQConsumer(consumerGroup = MembersAuthConstants.USER_MQ_REGIST_LOGIN_REGIST_CONSUMER_GROUP_KEY,
        topic = MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
        tag = MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_TAG_PRFILESET_KEY)
public class RegistConsumer extends AbstractMQPushConsumer<RegistingMessageVo> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SensorsAnalyticsService sensorsAnalyticsService;

    private CacheManager cacheManager = CacheManagerFactory.create();

    final static HashMap<String, Integer> TYPE_MAP = new HashMap<>();

    static {
        TYPE_MAP.put(MembersAuthConstants.REGISTER_WAY_WX, 1);
        TYPE_MAP.put(MembersAuthConstants.REGISTER_WAY_QQ, 2);
        TYPE_MAP.put(MembersAuthConstants.REGISTER_WAY_PHONE, 3);
        TYPE_MAP.put(MembersAuthConstants.REGISTER_WAY_H5WX, 4);
        TYPE_MAP.put(MembersAuthConstants.REGISTER_WAY_H5QQ, 5);
    }

    @Override
    public boolean process(RegistingMessageVo message, Map<String, Object> extMap) {
        logger.info(
                "cmd=RegistConsumer.process | msg=收到mq消费请求, req={}, Topic={}, tags={}, keys={}",
                JSON.toJSON(message), extMap.get(MessageExtConst.PROPERTY_TOPIC),
                extMap.get(MessageExtConst.PROPERTY_TAGS),
                extMap.get(MessageExtConst.PROPERTY_KEYS));

        if (cacheManager.exists(extMap.get(MessageExtConst.PROPERTY_KEYS).toString())) {
            logger.info(
                    "cmd=RegistConsumer.process | msg=重复收到注册事件消费请求,忽略 req={}, Topic={}, tags={}, keys={}",
                    JSON.toJSON(message), extMap.get(MessageExtConst.PROPERTY_TOPIC),
                    extMap.get(MessageExtConst.PROPERTY_TAGS),
                    extMap.get(MessageExtConst.PROPERTY_KEYS));
            return true;
        }

        HashMap<String, Object> map = new HashMap<>();
        Integer registType = TYPE_MAP.get(message.getRegistWay());
        if (registType == null) {
            registType = 0;
        }
        map.put("nickname", message.getNickname());
        map.put("sex", message.getSex());
        map.put("register_time", message.getRegisterTime());
        map.put("register_type", registType);
        map.put("jipai", message.getChickenId());
        map.put("phone", message.getPhone() != null ? message.getPhone() : "");
        map.put("register_channel", message.getChannel());
        map.put("register_version", message.getVersion());
        sensorsAnalyticsService.profileSetOnce(message.getUid(), map);
        HashMap<String, Object> trackMap = new HashMap<>();
        trackMap.put("register_type", registType);
        if (registType == 3) {
            trackMap.put("phone", message.getPhone() != null ? message.getPhone() : "");
        }
        sensorsAnalyticsService.track(message.getUid(),
                MembersAuthConstants.USER_EVENT_NAME_REGISTER_SUCCESS, trackMap);
        cacheManager.set(extMap.get(MessageExtConst.PROPERTY_KEYS).toString(), "", 3600);
        return true;
    }
}

package com.kaihei.esportingplus.marketing.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.marketing.api.event.UserEventHandler;
import com.kaihei.esportingplus.marketing.api.event.UserRegistEvent;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import com.maihaoche.starter.mq.base.MessageExtConst;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: chen.junyong
 * @Date: 2018-12-04 16:53
 * @Description:
 */
@MQConsumer(topic = RocketMQConstant.TOPIC_SHARE_INVIT, tag = RocketMQConstant.INVIT_REGIST_TAG,
        consumerGroup = RocketMQConstant.INVIT_SHARE_CONSUMER_GROUP)
public class InvitRegistConsumer extends AbstractMQPushConsumer<UserRegistEvent> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private CacheManager cacheManager = CacheManagerFactory.create();

    @Resource(name = "marketUserRegistEventHandler")
    UserEventHandler marketUserRegistEventHandler;

    @Override
    public boolean process(UserRegistEvent message,
            Map<String, Object> extMap) {
        logger.info(
                "cmd=InvitRegistConsumer.process | msg=收到邀请注册mq消费请求, req={}, Topic={}, tags={}, keys={}",
                JSON.toJSON(message), extMap.get(MessageExtConst.PROPERTY_TOPIC),
                extMap.get(MessageExtConst.PROPERTY_TAGS),
                extMap.get(MessageExtConst.PROPERTY_KEYS));
        if (cacheManager.exists(extMap.get(MessageExtConst.PROPERTY_KEYS).toString())) {
            logger.info(
                    "cmd=RegistConsumer.process | msg=重复收到邀请注册消费请求,忽略 req={}, Topic={}, tags={}, keys={}",
                    JSON.toJSON(message), extMap.get(MessageExtConst.PROPERTY_TOPIC),
                    extMap.get(MessageExtConst.PROPERTY_TAGS),
                    extMap.get(MessageExtConst.PROPERTY_KEYS));
            return true;
        }
        try {

            marketUserRegistEventHandler.handle(message);
        } catch (Exception e) {
            logger.error(
                    "cmd=RegistConsumer.process | msg=邀请注册消费事件异常 | message={} | exception={}",
                    message, e);
            return false;
        }
        cacheManager.set(extMap.get(MessageExtConst.PROPERTY_KEYS).toString(), "", 3600);
        return true;
    }

}

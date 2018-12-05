package com.kaihei.esportingplus.user.mq.consumer;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.core.api.enums.MessageTypeEnum;
import com.kaihei.esportingplus.core.api.feign.MsgSendServiceClient;
import com.kaihei.esportingplus.core.api.params.MessageSendParam;
import com.kaihei.esportingplus.user.api.vo.GreetingMessageVo;
import com.kaihei.esportingplus.user.config.UserProperties;
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
 * @Description: 注册登录发送问候消息消费类
 */
@MQConsumer(consumerGroup = MembersAuthConstants.USER_MQ_REGIST_LOGIN_GREETING_CONSUMER_GROUP_KEY,
        topic = MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
        tag = MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_TAG_GREETING_KEY)
public class GreetingConsumer extends AbstractMQPushConsumer<GreetingMessageVo> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserProperties userProperties;

    @Autowired
    private MsgSendServiceClient sendServiceClient;

    private CacheManager cacheManager = CacheManagerFactory.create();

    @Override
    public boolean process(GreetingMessageVo message, Map<String, Object> extMap) {
        logger.info(
                "cmd=GreetingConsumer.process | msg=收到mq消费请求, req={}, Topic={}, tags={}, keys={}",
                message.getUidList(), extMap.get(MessageExtConst.PROPERTY_TOPIC),
                extMap.get(MessageExtConst.PROPERTY_TAGS),
                extMap.get(MessageExtConst.PROPERTY_KEYS));
        if (cacheManager.exists(extMap.get(MessageExtConst.PROPERTY_KEYS).toString())) {
            logger.info(
                    "cmd=GreetingConsumer.process | msg=重复收到发送信息消费请求,忽略 req={}, Topic={}, tags={}, keys={}",
                    message.getUidList(), extMap.get(MessageExtConst.PROPERTY_TOPIC),
                    extMap.get(MessageExtConst.PROPERTY_TAGS),
                    extMap.get(MessageExtConst.PROPERTY_KEYS));
            return true;
        }
        MessageSendParam msp = new MessageSendParam();
        msp.setToSelf(false);
        msp.setTemplateId(3);
        HashMap data = new HashMap();
        data.put("content", userProperties.getUserRegistLoginGreetingMessage());
        msp.setData(data);
        msp.setReciever(message.getUidList());
        msp.setMessageType(MessageTypeEnum.SYSTEM);
        msp.setSender("bjdj_system");
        sendServiceClient.send(msp);
        cacheManager.set(extMap.get(MessageExtConst.PROPERTY_KEYS).toString(), "", 3600);
        return true;
    }
}

package com.kaihei.esportingplus.user.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.kaihei.esportingplus.common.sensors.service.SensorsAnalyticsService;
import com.kaihei.esportingplus.user.api.vo.Auth3BindSATrackMessageVo;
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
 * 用户第三方绑定以及手机绑定神策消费类
 *
 * @author yangshidong
 * @date 2018/10/29
 */
@MQConsumer(consumerGroup = MembersAuthConstants.USER_MQ_AUTH3_BIND_SATRACK_CONSUMER_GROUP_KEY,
        topic = MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
        tag = MembersAuthConstants.USER_MQ_AUTH3_BIND_TOPIC_TAG_SATRACK_KEY)
public class Auth3BindSATrackConsumer extends AbstractMQPushConsumer<Auth3BindSATrackMessageVo> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SensorsAnalyticsService sensorsAnalyticsService;

    @Override
    public boolean process(Auth3BindSATrackMessageVo message, Map<String, Object> extMap) {
        logger.info(
                "cmd=RegistSALoginConsumer.process | msg=收到mq消费请求, req={}, Topic={}, tags={}, keys={}",
                JSON.toJSON(message), extMap.get(MessageExtConst.PROPERTY_TOPIC),
                extMap.get(MessageExtConst.PROPERTY_TAGS),
                extMap.get(MessageExtConst.PROPERTY_KEYS));

        HashMap<String, Object> trackMap = new HashMap<>();
        trackMap.put("binding_type", message.getBindType());
        trackMap.put("account_type", message.getAccountType());
        sensorsAnalyticsService.track(message.getUid(),
                MembersAuthConstants.USER_EVENT_NAME_ACCOUNT_BINDING, trackMap);
        return true;
    }
}

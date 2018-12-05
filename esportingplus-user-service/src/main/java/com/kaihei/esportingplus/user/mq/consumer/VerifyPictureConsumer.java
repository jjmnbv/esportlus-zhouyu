package com.kaihei.esportingplus.user.mq.consumer;

import com.kaihei.esportingplus.user.api.vo.UserUrlMessageVo;
import com.kaihei.esportingplus.user.constant.MembersAuthConstants;
import com.kaihei.esportingplus.user.domain.service.MembersUserService;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import com.maihaoche.starter.mq.base.MessageExtConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @Auther: linruihe
 * @Date: 2018-11-01 15:36
 * @Description: 审核照片消费类
 */
@MQConsumer(consumerGroup = MembersAuthConstants.USER_MQ_PICTURE_VERIFY_CONSUMER_GROUP_KEY,
        topic = MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
        tag = MembersAuthConstants.USER_MQ_PICTURE_VERIFY_TOPIC_TAG_KEY)
public class VerifyPictureConsumer extends AbstractMQPushConsumer<UserUrlMessageVo> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MembersUserService membersUserService;

    @Override
    public boolean process(UserUrlMessageVo message, Map<String, Object> extMap) {
        logger.info(
                "cmd=VerifyPictureConsumer.process | msg=收到mq消费请求, req={}, Topic={}, tags={}, keys={}",
                message.getChannel(), extMap.get(MessageExtConst.PROPERTY_TOPIC),
                extMap.get(MessageExtConst.PROPERTY_TAGS),
                extMap.get(MessageExtConst.PROPERTY_KEYS));
        try{
            membersUserService.verifyPicture(message);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}

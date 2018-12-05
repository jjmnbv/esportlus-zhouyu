package com.kaihei.esportingplus.user.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.user.api.vo.CreateBankMessageVo;
import com.kaihei.esportingplus.user.constant.MembersAuthConstants;
import com.kaihei.esportingplus.user.constant.UserRedisKey;
import com.kaihei.esportingplus.user.data.manager.MembersUserManager;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import com.maihaoche.starter.mq.base.MessageExtConst;
import java.util.Map;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther: chen.junyong
 * @Date: 2018-09-18 15:36
 * @Description: 注册登录创建bank消费类
 */
//@MQConsumer(consumerGroup = MembersAuthConstants.USER_MQ_REGIST_LOGIN_CREATEBANK_CONSUMER_GROUP_KEY,
//        topic = MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
//        tag = MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_TAG_CREATEBANK_KEY)
public class CreateBankConsumer extends AbstractMQPushConsumer<CreateBankMessageVo> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MembersUserManager membersUserManager;

    private CacheManager cacheManager = CacheManagerFactory.create();

    @Override
    public boolean process(CreateBankMessageVo message, Map<String, Object> extMap) {
        logger.info(
                "cmd=CreateBankConsumer.process | msg=收到mq消费请求, req={}, Topic={}, tags={}, keys={}",
                JSON.toJSON(message), extMap.get(MessageExtConst.PROPERTY_TOPIC),
                extMap.get(MessageExtConst.PROPERTY_TAGS),
                extMap.get(MessageExtConst.PROPERTY_KEYS));
        RLock redisLock = null;
        try {
            redisLock = cacheManager.redissonClient().getLock(
                    String.format(UserRedisKey.USER_CREATE_BANK_KEY, message.getUid()));
            redisLock.lock();
            membersUserManager.createBankIfNotExistForRegist(message.getUserId());
        } catch (Exception e) {
            logger.error("cmd=CreateBankConsumer.process | msg=银行账号新增失败", e);
            return false;
        } finally {
            if (redisLock != null && redisLock.isLocked()) {
                redisLock.unlock();
            }
        }
        return true;
    }
}

package com.kaihei.esportingplus.payment.mq.consumer;

import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.payment.mq.message.MQSimpleOrder;
import com.kaihei.esportingplus.payment.service.GCoinRechargeService;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import com.maihaoche.starter.mq.base.MessageExtConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 暴鸡币充值消费者
 *
 * @author xusisi
 **/
@MQConsumer(topic = RocketMQConstant.PAYMENT_TOPIC, tag = RocketMQConstant.GCOIN_RECHARGE_TAG, consumerGroup =
        RocketMQConstant.GCOIN_RECHARGE_CONSUMER_GROUP)
public class GCoinRechargeConsumer extends AbstractMQPushConsumer<MQSimpleOrder> {

    private static final Logger logger = LoggerFactory.getLogger(GCoinRechargeConsumer.class);

    @Autowired
    private GCoinRechargeService gcoinRechargeService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean process(MQSimpleOrder msg, Map map) {

        logger.info("入参 >> ");
        logger.info("MQSimpleOrder : {} ", msg);
        logger.info("Map : {} ", map);

        //重复消费次数：默认为0
        int consumerTime = (int) map.get(MessageExtConst.PROPERTY_EXT_RECONSUME_TIMES);

        logger.debug("重复消费次数 : {}", consumerTime);

        Boolean flag = gcoinRechargeService.consumeRechargeMQ(msg);

        //重复消费三次以后不在消费
        if (!flag && consumerTime > 3) {
            logger.warn("连续三次消费不成功，则不再消费该消息");
            flag = true;
        }
        return flag;

    }
}




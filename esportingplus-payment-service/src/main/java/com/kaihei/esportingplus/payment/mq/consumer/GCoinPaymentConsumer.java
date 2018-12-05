package com.kaihei.esportingplus.payment.mq.consumer;

import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.payment.mq.message.MQSimpleOrder;
import com.kaihei.esportingplus.payment.service.GCoinPaymentService;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import com.maihaoche.starter.mq.base.MessageExtConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 暴鸡币支付消费者
 *
 * @author xusisi
 **/
@MQConsumer(topic = RocketMQConstant.PAYMENT_TOPIC, tag = RocketMQConstant.GCOIN_PAYMENT_TAG, consumerGroup =
        RocketMQConstant.GCOIN_PAYMENT_CONSUMER_GROUP)
public class GCoinPaymentConsumer extends AbstractMQPushConsumer<MQSimpleOrder> {

    private static final Logger logger = LoggerFactory.getLogger(GCoinPaymentConsumer.class);

    @Autowired
    private GCoinPaymentService gcoinPaymentService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean process(MQSimpleOrder msg, Map map) {

        logger.info("入参 >> ");
        logger.info("MQSimpleOrder : {} ", msg);
        logger.info("Map : {} ", map);

        //MQ消费次数：第一次为0，
        int consumTime = (int) map.get(MessageExtConst.PROPERTY_EXT_RECONSUME_TIMES);
        logger.debug("重复消费次数 : {}", consumTime);

        Boolean flag = gcoinPaymentService.consumePaymentMQ(msg);
        if (!flag && consumTime > 3) {
            logger.warn("重复消费三次还不成功，则不再消费该消息");
            flag = true;
        }
        return flag;

    }

}

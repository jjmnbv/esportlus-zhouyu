package com.kaihei.esportingplus.payment.mq.consumer;

import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.payment.mq.message.MQSimpleOrder;
import com.kaihei.esportingplus.payment.service.GCoinRefundService;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import com.maihaoche.starter.mq.base.MessageExtConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 暴鸡币退款消费者
 *
 * @author xusisi
 **/
@MQConsumer(topic = RocketMQConstant.PAYMENT_TOPIC, tag = RocketMQConstant.GCOIN_REFUND_TAG, consumerGroup =
        RocketMQConstant.GCOIN_REFUND_CONSUMER_GROUP)
public class GCoinRefundConsumer extends AbstractMQPushConsumer<MQSimpleOrder> {

    private static final Logger logger = LoggerFactory.getLogger(GCoinRefundConsumer.class);

    @Autowired
    private GCoinRefundService gcoinRefundService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean process(MQSimpleOrder msg, Map map) {

        logger.debug("入参 >> ");
        logger.debug("MQSimpleOrder : {} ", msg);
        logger.debug("Map : {} ", map);

        //MQ消费次数：第一次为0，
        int consumeTime = (int) map.get(MessageExtConst.PROPERTY_EXT_RECONSUME_TIMES);
        logger.debug("MQ消费次数 :{} ", consumeTime);

        Boolean flag = gcoinRefundService.consumeRefundMQ(msg);
        if (!flag && consumeTime > 3) {
            logger.warn("连续消费三次都不成功，则不在消费该消息");
            flag = true;
        }

        return flag;

    }

}

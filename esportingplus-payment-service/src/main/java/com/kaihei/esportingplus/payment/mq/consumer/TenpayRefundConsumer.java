package com.kaihei.esportingplus.payment.mq.consumer;

import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.payment.mq.message.ExternalRefundOrderMQ;
import com.kaihei.esportingplus.payment.service.impl.*;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import com.maihaoche.starter.mq.base.MessageExtConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 用户微信、QQ支付，退款消费者
 *
 * @author xusisi
 **/
@MQConsumer(topic = RocketMQConstant.PAYMENT_TOPIC, tag = RocketMQConstant.EXTERNAL_TENPAY_REFUND_TAG, consumerGroup =
        RocketMQConstant.EXTERNAL_TENPAY_REFUND_CONSUMER_GROUP)
public class TenpayRefundConsumer extends AbstractMQPushConsumer<ExternalRefundOrderMQ> {

    private static final Logger logger = LoggerFactory.getLogger(TenpayRefundConsumer.class);

    @Autowired
    private TenpayServiceImpl tenpayService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean process(ExternalRefundOrderMQ refundOrderMQ, Map map) {

        logger.info("process >> start >> refundOrderMQ : {} ", refundOrderMQ);
        logger.info("process >> start >> map : {} ", map);

        //MQ消费次数：第一次为0
        int consumeTime = (int) map.get(MessageExtConst.PROPERTY_EXT_RECONSUME_TIMES);
        logger.debug("process >> consumerTimes : {} ", consumeTime);
        Boolean flag = tenpayService.consumeRefundInfo(refundOrderMQ);
        //重复消费三次后则不在消费
        if (!flag && consumeTime > 3) {
            flag = true;
        }
        return flag;

    }

}

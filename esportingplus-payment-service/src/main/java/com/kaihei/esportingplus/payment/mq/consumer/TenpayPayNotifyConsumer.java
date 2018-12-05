package com.kaihei.esportingplus.payment.mq.consumer;

import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.payment.mq.message.ExternalPayOrderMQ;
import com.kaihei.esportingplus.payment.service.impl.TenpayServiceImpl;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import com.maihaoche.starter.mq.base.MessageExtConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 微信、QQ支付,支付回调信息消费者
 *
 * @author xusisi
 **/
@MQConsumer(topic = RocketMQConstant.PAYMENT_TOPIC, tag = RocketMQConstant.EXTERNAL_TENPAY_PAY_NOTIFY_TAG, consumerGroup =
        RocketMQConstant.EXTERNAL_TENPAY_PAY_NOTIFY_CONSUMER_GROUP)
public class TenpayPayNotifyConsumer extends AbstractMQPushConsumer<ExternalPayOrderMQ> {

    private static final Logger logger = LoggerFactory.getLogger(TenpayPayNotifyConsumer.class);

    @Autowired
    private TenpayServiceImpl tenpayService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean process(ExternalPayOrderMQ externalPayOrderMQ, Map map) {

        logger.info("process >> start >> externalPayOrderMQ : {} ", externalPayOrderMQ);
        logger.info("process >> start >> map : {} ", map);
        //MQ消费次数：第一次为0，
        int consumerTimes = (int) map.get(MessageExtConst.PROPERTY_EXT_RECONSUME_TIMES);

        logger.debug("process >> consumerTimes : {} ", consumerTimes);

        Boolean flag = tenpayService.consumerPaymentNotify(externalPayOrderMQ);
        //重复消费三次后则不在消费
        if (!flag && consumerTimes > 3) {
            flag = true;
        }
        return flag;
    }

}

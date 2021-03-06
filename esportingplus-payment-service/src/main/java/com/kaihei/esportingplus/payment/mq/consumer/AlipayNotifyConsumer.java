package com.kaihei.esportingplus.payment.mq.consumer;

import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.payment.mq.message.ExternalPayOrderMQ;
import com.kaihei.esportingplus.payment.service.impl.AlipayServiceImpl;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import com.maihaoche.starter.mq.base.MessageExtConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 支付宝回调信息消费者
 *
 * @author xusisi
 **/
@MQConsumer(topic = RocketMQConstant.PAYMENT_TOPIC, tag = RocketMQConstant.EXTERNAL_ALIPAY_NOTIFY_TAG, consumerGroup =
        RocketMQConstant.EXTERNAL_ALIPAY_NOTIFY_CONSUMER_GROUP)
public class AlipayNotifyConsumer extends AbstractMQPushConsumer<ExternalPayOrderMQ> {

    private static final Logger logger = LoggerFactory.getLogger(AlipayNotifyConsumer.class);

    @Autowired
    private AlipayServiceImpl alipayService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean process(ExternalPayOrderMQ externalPayOrderMQ, Map map) {

        logger.info("process >> start >> externalPayOrderMQ : {} ", externalPayOrderMQ);
        logger.info("process >> start >> map : {} ", map);
        //MQ消费次数：第一次为0，
        int consumerTimes = (int) map.get(MessageExtConst.PROPERTY_EXT_RECONSUME_TIMES);

        logger.debug("process >> consumerTimes : {} ", consumerTimes);

        Boolean flag = alipayService.consumerAlipayCallBackInfo(externalPayOrderMQ);
        //重复消费三次后则不在消费
        if (!flag && consumerTimes > 3) {
            flag = true;
        }
        return flag;
    }

}

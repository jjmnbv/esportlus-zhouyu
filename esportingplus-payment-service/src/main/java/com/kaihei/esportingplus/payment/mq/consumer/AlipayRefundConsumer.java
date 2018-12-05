package com.kaihei.esportingplus.payment.mq.consumer;

import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.payment.mq.message.ExternalRefundOrderMQ;
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
 * 用户支付宝退款消费者
 *
 * @author xusisi
 **/
@MQConsumer(topic = RocketMQConstant.PAYMENT_TOPIC, tag = RocketMQConstant.EXTERNAL_ALIPAY_REFUND_TAG, consumerGroup =
        RocketMQConstant.EXTERNAL_ALIPAY_REFUND_CONSUMER_GROUP)
public class AlipayRefundConsumer extends AbstractMQPushConsumer<ExternalRefundOrderMQ> {

    private static final Logger logger = LoggerFactory.getLogger(AlipayRefundConsumer.class);

    @Autowired
    private AlipayServiceImpl alipayService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean process(ExternalRefundOrderMQ refundOrderMQ, Map map) {

        logger.info("process >> start >> refundOrderMQ : {} ", refundOrderMQ);
        logger.info("process >> start >> map : {} ", map);

        //MQ消费次数：第一次为0
        int consumerTimes = (int) map.get(MessageExtConst.PROPERTY_EXT_RECONSUME_TIMES);
        logger.debug("process >> consumerTimes : {} ", consumerTimes);

        Boolean flag = alipayService.updateRefundInfo(refundOrderMQ);
        //重复消费三次后则不在消费
        if (!flag && consumerTimes > 3) {
            flag = true;
        }
        return flag;

    }

}

package com.kaihei.esportingplus.payment.mq.producer;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.payment.mq.message.ExternalPayOrderMQ;
import com.maihaoche.starter.mq.annotation.MQTransactionProducer;
import com.maihaoche.starter.mq.base.AbstractMQTransactionProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: esportingplus
 * @description: 微信、QQ支付，支付回调生产者
 * @author: xusisi
 * @create: 2018-11-03 17:20
 **/
@MQTransactionProducer(producerGroup = RocketMQConstant.EXTERNAL_TENPAY_PAY_NOTIFY_PRODUCER_GROUP)
public class TenpayPayNotifyProductor extends AbstractMQTransactionProducer {

    private static Logger logger = LoggerFactory.getLogger(TenpayPayNotifyProductor.class);

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    /**
     * 本地事务方法
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        if (arg instanceof ExternalPayOrderMQ) {
            logger.info("微信、QQ支付,支付回调消息入列成功");
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        logger.info("微信、QQ支付,支付回调消息入列异常");
        return LocalTransactionState.ROLLBACK_MESSAGE;

    }

    /**
     * 本地事务确认
     * <p>
     * executeLocalTransaction返回UNKNOW 的情况下 MQ会在一段时间后调用producerGroup相同的应用
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }
}


package com.kaihei.esportingplus.payment.mq.producer;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.payment.mq.message.WalletPayNotifyMQ;
import com.maihaoche.starter.mq.annotation.MQTransactionProducer;
import com.maihaoche.starter.mq.base.AbstractMQTransactionProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: esportingplus
 * @description: 内部支付-暴鸡币支付回调信息生产者
 * @author: xusisi
 * @create: 2018-11-03 17:20
 **/
@MQTransactionProducer(producerGroup = RocketMQConstant.EXTERNAL_GCOIN_NOTIFY_PRODUCER_GROUP)
public class WalletPayNotifyProducer extends AbstractMQTransactionProducer {

    private static Logger logger = LoggerFactory.getLogger(WalletPayNotifyProducer.class);

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    /**
     * 本地事务方法
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        if (arg instanceof WalletPayNotifyMQ) {
            logger.info("内部支付-暴鸡币支付回调信息入列成功");
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        logger.info("内部支付-暴鸡币支付回调信息入列异常");
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


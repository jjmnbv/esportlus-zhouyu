package com.kaihei.esportingplus.payment.mq.producer;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.payment.api.params.GCoinPaymentUpdateParams;
import com.kaihei.esportingplus.payment.data.jpa.repository.GCoinPaymentRepository;
import com.kaihei.esportingplus.payment.domain.entity.GCoinPaymentOrder;
import com.maihaoche.starter.mq.annotation.MQTransactionProducer;
import com.maihaoche.starter.mq.base.AbstractMQTransactionProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 支付服务事务消息发送方
 *
 * @author xusisi
 */
@MQTransactionProducer(producerGroup = RocketMQConstant.GCOIN_PAYMENT_PRODUCER_GROUP)
public class GCoinPaymentTransactionProducer extends AbstractMQTransactionProducer {

    private static Logger logger = LoggerFactory.getLogger(GCoinPaymentTransactionProducer.class);

    @Autowired
    private GCoinPaymentRepository gcoinPaymentRepository;

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    /**
     * 本地事务方法
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {

        GCoinPaymentUpdateParams gcoinPaymentUpdateParams = null;
        if (arg != null && arg instanceof GCoinPaymentUpdateParams) {
            gcoinPaymentUpdateParams = (GCoinPaymentUpdateParams) arg;
        }

        logger.info("executeLocalTransaction >> start >> msg : {}", msg);
        // 提交本地事务
        try {
            String orderId = gcoinPaymentUpdateParams.getOrderId();
            String userId = gcoinPaymentUpdateParams.getUserId();
            GCoinPaymentOrder gcoinPayment = gcoinPaymentRepository.findOneByOrderIdAndUserId(orderId, userId);
            gcoinPayment.setOrderType(gcoinPaymentUpdateParams.getOrderType());
            gcoinPayment.setOutTradeNo(gcoinPaymentUpdateParams.getOutTradeNo());
            gcoinPaymentRepository.save(gcoinPayment);

            //更新redis中的充值订单数据
            String key = String.format(RedisKey.GCOIN_PAYMENT_KEY, userId, orderId);
            logger.debug("preCreatePayment >> saveToRedis >> key : {} ", key);

            //将订单信息存于redis中，过期时间为24小时
            cacheManager.set(key, gcoinPayment, 24 * 60 * 60);

        } catch (Exception e) {
            logger.error("executeLocalTransaction >> exception msg :{} ,gcoinPaymentUpdateParams :{} ,e :{} ", msg, gcoinPaymentUpdateParams, e);
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        logger.info("executeLocalTransaction >> end >> success ");
        return LocalTransactionState.COMMIT_MESSAGE;

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

package com.kaihei.esportingplus.payment.mq.producer;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.payment.api.enums.RedisKeyType;
import com.kaihei.esportingplus.payment.api.vo.ExchangeUpdateMessageVO;
import com.kaihei.esportingplus.payment.data.jpa.repository.WithdrawOrderRepository;
import com.kaihei.esportingplus.payment.domain.entity.WithdrawOrder;
import com.maihaoche.starter.mq.annotation.MQTransactionProducer;
import com.maihaoche.starter.mq.base.AbstractMQTransactionProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 兑换事务生产者
 * @author chenzhenjun
 */
@MQTransactionProducer(producerGroup = "${app.rocketmq.transaction-producer-group}")
public class ExchangeTransactionProducer extends AbstractMQTransactionProducer {

    private static Logger logger = LoggerFactory.getLogger(ExchangeTransactionProducer.class);

    @Autowired
    private WithdrawOrderRepository withdrawOrderRepository;

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object arg) {
        ExchangeUpdateMessageVO exchangeUpdateMessageVO = null;
        if (arg != null && arg instanceof ExchangeUpdateMessageVO) {
            exchangeUpdateMessageVO = (ExchangeUpdateMessageVO) arg;
        }

        logger.info("ExchangeTransactionProducer >> executeLocalTransaction >> start >> msg : {}", message);
        // 提交本地事务
        try {
            String orderId = exchangeUpdateMessageVO.getOrderId();
            String userId = exchangeUpdateMessageVO.getUserId();
            WithdrawOrder withdrawOrder = withdrawOrderRepository.findOneByOrderIdAndUserId(orderId, userId);
            withdrawOrderRepository.save(withdrawOrder);

            String key = RedisKeyType.EXCHANGE.getCode() + ":" + userId + ":" + orderId;
            logger.debug("convertStarlightToGCoin >> saveToRedis >> key : {} ", key);

            //将订单信息存于redis中，过期时间为24小时
            cacheManager.set(key, withdrawOrder, 24 * 60 * 60);

        } catch (Exception e) {
            logger.error("executeLocalTransaction >> exception msg :{} ,gcoinPaymentUpdateParams :{} ,e :{} ", message, exchangeUpdateMessageVO, e);
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        logger.info("executeLocalTransaction >> end >> success ");
        return LocalTransactionState.COMMIT_MESSAGE;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }
}

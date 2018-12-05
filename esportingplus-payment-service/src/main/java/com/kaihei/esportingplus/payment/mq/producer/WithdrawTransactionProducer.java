package com.kaihei.esportingplus.payment.mq.producer;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.payment.api.enums.RedisKeyType;
import com.kaihei.esportingplus.payment.api.vo.WithdrawUpdateMessageVO;
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
 * 提现事务生产者
 * @author chenzhenjun
 */
@MQTransactionProducer(producerGroup = "${app.rocketmq.transaction-producer-group}")
public class WithdrawTransactionProducer extends AbstractMQTransactionProducer {

    private static Logger logger = LoggerFactory.getLogger(WithdrawTransactionProducer.class);

    @Autowired
    private WithdrawOrderRepository withdrawOrderRepository;

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    /**
     * 本地事务方法
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        WithdrawUpdateMessageVO withdrawUpdateMessageVO = null;
        if (arg != null && arg instanceof WithdrawUpdateMessageVO) {
            withdrawUpdateMessageVO = (WithdrawUpdateMessageVO) arg;
        }

        logger.info("WithdrawTransactionProducer >> executeLocalTransaction >> start >> msg : {}", msg);
        // 提交本地事务
        try {
            String orderId = withdrawUpdateMessageVO.getOrderId();
            String userId = withdrawUpdateMessageVO.getUserId();
            WithdrawOrder withdrawOrder = withdrawOrderRepository.findOneByOrderIdAndUserId(orderId, userId);
            withdrawOrder.setOutTradeNo(withdrawUpdateMessageVO.getOutTradeNo());
            withdrawOrderRepository.save(withdrawOrder);

            String key = RedisKeyType.WITHDRAW.getCode() + ":" + userId + ":" + orderId;
            logger.debug("checkAndCreateWithdrawOrder >> saveToRedis >> key : {} ", key);

            //将订单信息存于redis中，过期时间为24小时
            cacheManager.set(key, withdrawOrder, 24 * 60 * 60);

        } catch (Exception e) {
            logger.error("executeLocalTransaction >> exception msg :{} ,gcoinPaymentUpdateParams :{} ,e :{} ", msg, withdrawUpdateMessageVO, e);
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

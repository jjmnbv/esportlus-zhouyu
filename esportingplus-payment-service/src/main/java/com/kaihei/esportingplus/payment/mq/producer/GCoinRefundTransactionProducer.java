package com.kaihei.esportingplus.payment.mq.producer;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.payment.api.enums.GCoinRefundStateType;
import com.kaihei.esportingplus.payment.api.params.GCoinRefundParams;
import com.kaihei.esportingplus.payment.data.jpa.repository.GCoinPaymentRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.GCoinRefundRepository;
import com.kaihei.esportingplus.payment.domain.entity.GCoinPaymentOrder;
import com.kaihei.esportingplus.payment.domain.entity.GCoinRefundOrder;
import com.maihaoche.starter.mq.annotation.MQTransactionProducer;
import com.maihaoche.starter.mq.base.AbstractMQTransactionProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 暴鸡币退款服务事务消息发送方
 *
 * @author xusisi
 */
@MQTransactionProducer(producerGroup = RocketMQConstant.GCOIN_REFUND_PRODUCER_GROUP)
public class GCoinRefundTransactionProducer extends AbstractMQTransactionProducer {

    private static Logger logger = LoggerFactory.getLogger(GCoinRefundTransactionProducer.class);
    private static final CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    private GCoinRefundRepository gcoinRefundRepository;

    @Autowired
    private GCoinPaymentRepository gcoinPaymentRepository;

    /**
     * 本地事务方法
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {

        String transactionId = msg.getTransactionId();
        GCoinRefundParams gcoinRefundParams = null;
        if (arg != null && arg instanceof GCoinRefundParams) {
            gcoinRefundParams = (GCoinRefundParams) arg;
        }
        logger.info("executeLocalTransaction >> start >> msg : {}, arg : {} ", msg, arg);

        String orderId = gcoinRefundParams.getOrderId();
        String userId = gcoinRefundParams.getUserId();
        String orderType = gcoinRefundParams.getOrderType();
        String outTradeNo = gcoinRefundParams.getOutTradeNo();
        // 提交本地事务
        try {

            //创建一个退款订单信息
            GCoinRefundOrder gcoinRefund = new GCoinRefundOrder();
            gcoinRefund.setOrderId(orderId);
            gcoinRefund.setOutRefundNo(gcoinRefundParams.getOutRefundNo());
            gcoinRefund.setOutTradeNo(outTradeNo);
            gcoinRefund.setOrderType(orderType);
            gcoinRefund.setUserId(userId);
            String amountTemp = gcoinRefundParams.getAmount();
            BigDecimal refundAmount = new BigDecimal(amountTemp).divide(new BigDecimal(100));
            gcoinRefund.setAmount(refundAmount);
            gcoinRefund.setAttach(gcoinRefundParams.getAttach());
            gcoinRefund.setSourceId(gcoinRefundParams.getSourceId());
            gcoinRefund.setBody(gcoinRefundParams.getBody());
            gcoinRefund.setSubject(gcoinRefundParams.getSubject());
            gcoinRefund.setDescription(gcoinRefundParams.getDescription());
            //退款订单状态为，退款中
            gcoinRefund.setState(GCoinRefundStateType.CREATE.getCode());
            gcoinRefundRepository.save(gcoinRefund);

            logger.debug("executeLocalTransaction >> gcoinRefund : {} ", gcoinRefund);

            String key = String.format(RedisKey.GCOIN_REFUND_KEY, userId, orderId);
            logger.debug("executeLocalTransaction >> saveToRedis >> key : {} ", key);
            //将订单信息存于redis中，过期时间为24小时
            cacheManager.set(key, gcoinRefund, 24 * 60 * 60);

            //锁定支付订单中的退款金额
            GCoinPaymentOrder gcoinPayment = gcoinPaymentRepository.findOneByOrderTypeAndOutTradeNo(orderType, outTradeNo);
            BigDecimal oldRefundingAmount = gcoinPayment.getRefundingAmount();
            //新的退款中的金额 = 旧的退款中金额+本次退款金额
            gcoinPayment.setRefundingAmount(oldRefundingAmount.add(refundAmount));
            gcoinPayment.setLastModifiedDate(new Date());
            gcoinPaymentRepository.save(gcoinPayment);
            logger.debug("executeLocalTransaction >> gcoinPayment :{} ", gcoinPayment);

        } catch (Exception e) {
            logger.error("executeLocalTransaction >> exception >> id :{} , msg :{} ,gcoinRefundParams :{} ,e :{} ", transactionId, msg, gcoinRefundParams, e);
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

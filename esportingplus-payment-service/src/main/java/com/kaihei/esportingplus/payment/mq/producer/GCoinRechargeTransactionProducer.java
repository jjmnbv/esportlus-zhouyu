package com.kaihei.esportingplus.payment.mq.producer;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.payment.api.enums.PayChannelEnum;
import com.kaihei.esportingplus.payment.api.params.GCoinRechargeUpdateParams;
import com.kaihei.esportingplus.payment.data.jpa.repository.GCoinRechargeRepository;
import com.kaihei.esportingplus.payment.domain.entity.GCoinRechargeOrder;
import com.maihaoche.starter.mq.annotation.MQTransactionProducer;
import com.maihaoche.starter.mq.base.AbstractMQTransactionProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 暴鸡币支付服务事务消息发送方
 *
 * @author xusisi
 */
@MQTransactionProducer(producerGroup = RocketMQConstant.GCOIN_RECHARGE_PRODUCER_GROUP)
public class GCoinRechargeTransactionProducer extends AbstractMQTransactionProducer {

    private static Logger logger = LoggerFactory.getLogger(GCoinRechargeTransactionProducer.class);

    @Autowired
    private GCoinRechargeRepository rechargeRepository;


    private static final CacheManager cacheManager = CacheManagerFactory.create();

    /**
     * 本地事务方法
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {

        String transactionId = msg.getTransactionId();
        GCoinRechargeUpdateParams rechargeUpdateParams = null;
        if (arg != null && arg instanceof GCoinRechargeUpdateParams) {
            rechargeUpdateParams = (GCoinRechargeUpdateParams) arg;
        }

        logger.info("executeLocalTransaction >> start >> msg : {}, arg : {} ", msg, arg);
        // 提交本地事务
        try {
            String orderId = rechargeUpdateParams.getOrderId();
            String userId = rechargeUpdateParams.getUserId();
            GCoinRechargeOrder recharge = rechargeRepository.findOneByOrderIdAndUserId(orderId, userId);
            //充值的其他信息
            String channel = rechargeUpdateParams.getChannel();
            recharge.setChannel(channel);
            //如果是apple支付需要保存收据信息、设备ID、货币类型
            if (PayChannelEnum.APPLE_PAY.getValue().equals(channel)) {
                recharge.setDescription(rechargeUpdateParams.getReceiptData());
                recharge.setDeviceId(rechargeUpdateParams.getDeviceId());
                recharge.setCurrencyType(rechargeUpdateParams.getCurrencyType());
            } else {
                //如果不是apple支付需要填写第三方支付订单ID，支付时间，支付金额
                recharge.setPaymentOrderNo(rechargeUpdateParams.getPaymentOrderNo());
                recharge.setPaymentDate(rechargeUpdateParams.getPaymentDate());
            }

            recharge.setSubject(rechargeUpdateParams.getSubject());
            recharge.setBody(rechargeUpdateParams.getBody());

            rechargeRepository.save(recharge);

            //更新redis中的充值订单数据
            String key = String.format(RedisKey.GCOIN_RECHARGE_KEY, userId, orderId);
            logger.debug("preCreatePayment >> saveToRedis >> key : {} ", key);

            //将订单信息存于redis中，过期时间为24小时
            cacheManager.set(key, recharge, 24 * 60 * 60);


        } catch (Exception e) {
            logger.error("executeLocalTransaction >> exception >> id :{} , msg :{} ,rechargeUpdateParams :{} ,e :{} ", transactionId, msg, rechargeUpdateParams, e);
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

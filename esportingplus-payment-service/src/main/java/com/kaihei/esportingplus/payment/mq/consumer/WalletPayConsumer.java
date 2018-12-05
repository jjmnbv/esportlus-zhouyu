package com.kaihei.esportingplus.payment.mq.consumer;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.payment.api.params.GCoinPaymentUpdateParams;
import com.kaihei.esportingplus.payment.api.vo.GCoinPaymentVo;
import com.kaihei.esportingplus.payment.mq.message.MQSimpleOrder;
import com.kaihei.esportingplus.payment.domain.entity.GCoinBalance;
import com.kaihei.esportingplus.payment.domain.entity.GCoinPaymentOrder;
import com.kaihei.esportingplus.payment.service.GCoinPaymentService;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import com.maihaoche.starter.mq.base.MessageExtConst;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 内部支付-暴鸡币支付消费者
 *
 * @author xusisi
 **/
@MQConsumer(topic = RocketMQConstant.PAYMENT_TOPIC, tag = RocketMQConstant.EXTERNAL_GCOIN_PAY_TAG, consumerGroup =
        RocketMQConstant.EXTERNAL_GCOIN_PAY_CONSUMER_GROUP)
public class WalletPayConsumer extends AbstractMQPushConsumer<MQSimpleOrder> {

    private static final Logger logger = LoggerFactory.getLogger(WalletPayConsumer.class);
    private CacheManager cacheManager = CacheManagerFactory.create();

    @Resource(name = "restTemplateExtrnal")
    private RestTemplate restTemplate;

    @Autowired
    private GCoinPaymentService gCoinPaymentService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean process(MQSimpleOrder mqSimpleOrder, Map map) {

        logger.info("process >> start >> mqSimpleOrder : {} ", mqSimpleOrder);
        logger.info("process >> start >> map : {} ", map);

        //MQ消费次数：第一次为0，
        int reconsumeTimes = (int) map.get(MessageExtConst.PROPERTY_EXT_RECONSUME_TIMES);
        logger.debug("process >> reconsumeTimes : {} ", reconsumeTimes);

        //订单id
        String orderId = mqSimpleOrder.getOrderId();
        //回调通知地址
        String notifyUrl = mqSimpleOrder.getNotifyUrl();
        //用户ID
        String userId = mqSimpleOrder.getUserId();

        ResponsePacket result = null;

        //消息是否消费锁标识
        String consumerLock = String.format(RedisKey.EXTERNAL_GCOIN_PAY_KEY, orderId);
        RLock lock = cacheManager.redissonClient().getLock(consumerLock);
        if (lock.isLocked()) {
            logger.info(">> mq 已经被消费 orderId : {} ", orderId);
            return true;
        }

        //mq标记
        Boolean flag = true;
        try {
            //设置锁的超时时间为3秒
            lock.expire(3, TimeUnit.SECONDS);

            //判断用户暴鸡币账户状态
            GCoinBalance gCoinBalance = gCoinPaymentService.checkGcoinAccountState(userId);
            if (gCoinBalance == null) {
                logger.error("exception : {} ", BizExceptionEnum.GCOIN_ACCOUNT_BALANCE_NOT_ENOUGH.getErrMsg());
                throw new BusinessException(BizExceptionEnum.GCOIN_ACCOUNT_BALANCE_NOT_ENOUGH);
            }

            //判断暴鸡币支付订单是否存在，是否处于待支付状态
            GCoinPaymentOrder paymentOrder = gCoinPaymentService.checkOrderState(orderId);
            if (paymentOrder == null) {
                logger.error("exception : {} ", BizExceptionEnum.GCOIN_PAYMENT_ORDER_NOT_EXIST.getErrMsg());
                throw new BusinessException(BizExceptionEnum.GCOIN_PAYMENT_ORDER_NOT_EXIST);
            }

            GCoinPaymentUpdateParams updateParams = new GCoinPaymentUpdateParams();
            updateParams.setOrderId(orderId);
            updateParams.setOutTradeNo(paymentOrder.getOutTradeNo());
            updateParams.setOrderType(paymentOrder.getOrderType());
            updateParams.setUserId(userId);
            updateParams.setNotifyUrl(notifyUrl);
            //调用暴鸡币支付接口
            GCoinPaymentVo vo = gCoinPaymentService.updateOrderInfo(updateParams);
            flag = true;
        } catch (BusinessException e) {
            logger.info("业务异常，告诉MQ已经消费成功这个消息，不需要重发了。");
            logger.debug("exception : {} ", e.getErrMsg());
            flag = true;
        } catch (Exception e) {
            logger.info("没有正常消费这个消息，需要MQ重发消息，需要重新消费了。");
            logger.error("exception : {}", e);
            flag = false;
        } finally {

            try {
                if (lock != null && lock.isLocked()) {
                    lock.unlock();
                }
            } catch (Exception e) {
                logger.error("分布式锁异常 >> {} ", e);
            }

            return flag;
        }

    }

}

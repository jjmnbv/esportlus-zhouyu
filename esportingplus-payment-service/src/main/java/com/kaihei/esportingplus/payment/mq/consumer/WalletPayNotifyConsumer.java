package com.kaihei.esportingplus.payment.mq.consumer;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.tools.HttpUtils;
import com.kaihei.esportingplus.payment.data.jpa.repository.GCoinPaymentRepository;
import com.kaihei.esportingplus.payment.domain.entity.GCoinPaymentOrder;
import com.kaihei.esportingplus.payment.mq.message.WalletPayNotifyMQ;
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
 * 内部支付-暴鸡币支付回调信息消费者
 *
 * @author xusisi
 **/
@MQConsumer(topic = RocketMQConstant.PAYMENT_TOPIC, tag = RocketMQConstant.EXTERNAL_GCOIN_NOTIFY_TAG, consumerGroup =
        RocketMQConstant.EXTERNAL_GCOIN_NOTIFY_CONSUMER_GROUP)
public class WalletPayNotifyConsumer extends AbstractMQPushConsumer<WalletPayNotifyMQ> {

    private static final Logger logger = LoggerFactory.getLogger(WalletPayNotifyConsumer.class);
    private CacheManager cacheManager = CacheManagerFactory.create();

    @Resource(name = "restTemplateExtrnal")
    private RestTemplate restTemplate;

    @Autowired
    private GCoinPaymentRepository gcoinPaymentRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean process(WalletPayNotifyMQ walletPayNotifyMQ, Map map) {

        logger.info("process >> start >> walletPayNotifyMQ : {} ", walletPayNotifyMQ);
        logger.info("process >> start >> map : {} ", map);

        //MQ消费次数：第一次为0，
        int reconsumeTimes = (int) map.get(MessageExtConst.PROPERTY_EXT_RECONSUME_TIMES);
        logger.debug("process >> reconsumeTimes : {} ", reconsumeTimes);

        //订单id
        String orderId = walletPayNotifyMQ.getOrderId();
        int code = Integer.valueOf(walletPayNotifyMQ.getCode());
        String msg = walletPayNotifyMQ.getMsg();

        ResponsePacket result = null;
        logger.debug("校验暴鸡币支付订单状态，已经处理过则不再处理");
        GCoinPaymentOrder gcoinPayment = gcoinPaymentRepository.findOneByOrderId(orderId);

        //支付订单不存在
        if (gcoinPayment == null) {
            logger.info("暴鸡币支付订单号 : {} 不存在 该消息不需要继续处理", orderId);
            return true;
        }

        //消息是否消费锁标识
        String consumerLock = String.format(RedisKey.EXTERNAL_GCOIN_NOTIFY_KEY, orderId);
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
            //支付成功时返回

            if (BizExceptionEnum.SUCCESS.getErrCode() == code) {
                result = ResponsePacket.onSuccess(gcoinPayment);
            } else {
                result = ResponsePacket.onError(code, msg);
                result.setData(gcoinPayment);
            }

            flag = true;

        } catch (Exception e) {
            logger.info("没有正常消费这个消息，需要MQ重发消息，需要重新消费了。");
            logger.error("exception : {}", e);
            flag = false;
        } finally {
            //回调通知地址
            String notifyUrl = gcoinPayment.getAttach();
            logger.debug("回调URL >> {} ", notifyUrl);
            logger.debug("提交给业务方的参数 : {} ", result);
            try {
                ResponsePacket response = restTemplate.postForObject(notifyUrl, HttpUtils.buildParam(result), ResponsePacket.class);
                logger.debug("业务方返回 >> {} ", response);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("exception : 回调通知业务方时发生异常 >> {} ", e.getMessage());
                flag = false;
            }

            //删除分布式锁
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

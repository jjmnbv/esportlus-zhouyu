package com.kaihei.esportingplus.payment.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.payment.api.enums.CloudStatusStateMappingEnum;
import com.kaihei.esportingplus.payment.data.jpa.repository.ExternalWithdrawOrderRepository;
import com.kaihei.esportingplus.payment.domain.entity.ExternalWithdrawOrder;
import com.kaihei.esportingplus.payment.service.impl.CloudAccountServiceImpl;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import com.maihaoche.starter.mq.base.MessageExtConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 云账户通知-消费类
 * @author chenzhenjun
 */
@MQConsumer(topic = RocketMQConstant.PAYMENT_TOPIC, tag = RocketMQConstant.EXTERNEL_CLOUD_NOTIFYORDER_TAG,
        consumerGroup = RocketMQConstant.EXTERNAL_CLOUD_NOTIFY_CONSUMER_GROUP)
public class CloudAccountNotifyConsumer extends AbstractMQPushConsumer<JSONObject> {

    private static final Logger logger = LoggerFactory.getLogger(CloudAccountNotifyConsumer.class);
    private CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    private ExternalWithdrawOrderRepository orderRepository;

    @Autowired
    private CloudAccountServiceImpl cloudAccountService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean process(JSONObject jsonObject, Map<String, Object> map) {
        logger.debug("CloudAccountNotifyConsumer >> process >> message >> " + jsonObject);
        logger.debug("CloudAccountNotifyConsumer >> process >> extMap >> " + map);
        try {
            //MQ消费次数：第一次为0，
            int consumeTimes = (int) map.get(MessageExtConst.PROPERTY_EXT_RECONSUME_TIMES);
            logger.debug("cloud >> notify >> process >> reconsumeTimes : {} ", consumeTimes);

            /**
             * 1.判断状态
             * 2.更新
             * 3.回调python，自身调？还是python调
             */
            String outTradeNo = jsonObject.getString("order_id");
            int status = jsonObject.getIntValue("status"); // 状态

            //判断这个订单是否正在被消费
            String consumerLock = "cloud:notify:consume:lock:" + outTradeNo;
            boolean hasLock = cacheManager.exists(consumerLock);
            if (!hasLock) {
                //设置锁失效时间为3秒
                cacheManager.set(consumerLock, "lock", 3);
            } else {
                logger.debug(">>CloudAccountNotifyConsumer >>mq已经被消费, outTradeNo : {} ", outTradeNo);
                return true;
            }

            ExternalWithdrawOrder order = orderRepository.findByOutTradeNo(outTradeNo);
            String key = String.format(RedisKey.EXTERNAL_CLOUD_PAY_KEY, outTradeNo);

            CloudStatusStateMappingEnum statusEnum = CloudStatusStateMappingEnum.lookup(status);
            cloudAccountService.updateCloudWithdrawOrderInfo(statusEnum, jsonObject, order, key, "notify");

        } catch (Exception e) {
            logger.error("系统异常:CloudAccountNotifyConsumer >> process >> exception >> " + e.getMessage());
        }

        return true;
    }

}

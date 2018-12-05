package com.kaihei.esportingplus.trade.mq.consumer;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.gamingteam.api.mq.PVPUpdateOrderStatusMessage;
import com.kaihei.esportingplus.trade.api.params.UpdateOrderStatusPVPParams;
import com.kaihei.esportingplus.trade.domain.service.PVPOrderService;
import com.kaihei.esportingplus.trade.event.SendUpdatedEvent;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 *@Description: 更新PVP订单状态、收益、退款
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/11/8 11:56
*/
@MQConsumer(topic = RocketMQConstant.TOPIC_PVP, tag = RocketMQConstant.UPDATE_ORDER_STATUS_TAG,
        consumerGroup = RocketMQConstant.UPDATE_ORDER_STATUS_CONSUMER_PVP_GROUP)
public class PVPUpdateOrderStatusConsumer extends AbstractMQPushConsumer<PVPUpdateOrderStatusMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PVPUpdateOrderStatusConsumer.class);

    @Autowired
    private PVPOrderService pvpOrderService;

    @Value("${team2order.updateOrder.retryTimes}")
    private int retryTimes;

    private CacheManager cacheManager = CacheManagerFactory.create();

    @Override
    public boolean process(PVPUpdateOrderStatusMessage msg, Map map) {
        String msg_id = map.get("MSG_ID").toString();
        int reconsume_times = 0;
        if(msg == null || msg.getUpdateOrderStatusPVPParams() == null){
            LOGGER.info(">>收到更新PVP订单状态消息为空：{}，MQ消息id:{}，不做处理",
                    msg,
                    msg_id);
            return true;
        }

        UpdateOrderStatusPVPParams updateOrderStatusPVPParams = msg.getUpdateOrderStatusPVPParams();
        msg_id = msg_id + "_" + updateOrderStatusPVPParams.hashCode();
        String json = FastJsonUtils.toJson(msg);

        LOGGER.info(">>收到更新PVP订单状态消息: {}，MQ消息id:{}",
                json,
                msg_id);

        //消息重复消费处理：消费一次缓存起来 如果存在说明此消息发生过消费，直接return true;
        String repeatConsumeKey = RedisKey.UPDATE_ORDER_HISTORY + msg_id;
        Boolean repeatConsume = cacheManager.exists(repeatConsumeKey);
        if(repeatConsume != null && repeatConsume){
            LOGGER.info(">>PVP消息重复消费，消息已经被消费过，MQ消息id:{}，忽略处理。消息：{}",
                    msg_id,
                    json);
            return true;
        }

        try {
            reconsume_times = (int) map.get("RECONSUME_TIMES");
            pvpOrderService.updateOrderStatus(updateOrderStatusPVPParams);

            //2 分钟失效：mq默认重试5次 1/5/10/30/60,将近2分钟。
            cacheManager.set(RedisKey.UPDATE_ORDER_HISTORY + msg_id, StringUtils.EMPTY,120);

            //异步设置车所有队员订单更新完成
            EventBus.post(new SendUpdatedEvent(updateOrderStatusPVPParams.getTeamSequence()));

        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            LOGGER.error(">>更新PVP订单状态失败，MQ消息id:{}，重试: {}",msg_id,reconsume_times + 1);
            if (reconsume_times >= retryTimes - 1) {
                LOGGER.error(">>更新PVP订单状态失败,MQ消息id:{}，重试次数已达阀值:{},不在处理",msg_id, retryTimes);
                return true;
            }
            return false;
        }
        LOGGER.info(">>消费PVP消息完毕: {}", msg_id);
        return true;
    }
}

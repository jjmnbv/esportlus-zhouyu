package com.kaihei.esportingplus.trade.mq.consumer;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.trade.domain.service.RPGOrderService;
import com.kaihei.esportingplus.trade.enums.GameOrderType;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@MQConsumer(topic = RocketMQConstant.TOPIC_RPG, tag = RocketMQConstant.REFUND_ORDER_TAGS_FROM_TEAM,
        consumerGroup = RocketMQConstant.REFUND_ORDER_CONSUMER_GROUP_FROM_TEAM)
public class RefundOrderFromTeamConsumer extends AbstractMQPushConsumer<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefundOrderFromTeamConsumer.class);

    @Autowired
    private RPGOrderService rpgOrderService;

    private CacheManager cacheManager = CacheManagerFactory.create();

    @Value("${refund.mq.retryTimes:5}")
    private int retryTimes;

    @Override
    public boolean process(String orderSequence, Map map) {
        int reconsume_times = (int) map.get("RECONSUME_TIMES");
        //校验消息为空
        Object msg_id = map.get("MSG_ID");

        if(StringUtils.isBlank(orderSequence)){
            LOGGER.info(">>收到车队服务发起的退款RPG消息为空：{}，MQ消息id:{}，不做处理",
                    orderSequence,
                    msg_id);
            return true;
        }

        ///消息重复消费处理：消费一次缓存起来 如果存在说明此消息发生过消费，直接return true;
        Boolean repeatConsume = cacheManager.exists(RedisKey.REFUND_HISTORY_FROM_TEAM + orderSequence);
        if(repeatConsume != null && repeatConsume){
            LOGGER.info(">>车队服务发起的退款消息重复消费，RPG订单[{}]已经发起过退款，MQ消息id:{}，忽略处理",
                    orderSequence,
                    msg_id);
            return true;
        }

        try {
            LOGGER.info("收到车队服务发起的RPG退款消息，开始退款，MQ消息id:{},订单[{}]"
                    , msg_id,orderSequence);
            rpgOrderService.refundOrder(orderSequence, GameOrderType.RPG);

            //2 分钟失效：mq默认重试5次 1/5/10/30/60,将近2分钟。
            cacheManager.set(RedisKey.REFUND_HISTORY_FROM_TEAM + orderSequence, StringUtils.EMPTY,120);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            LOGGER.error(">>RPG订单[{}]发起退款异常，MQ消息id:{}，重试: {}",orderSequence,msg_id,reconsume_times + 1);
            if (reconsume_times >= retryTimes - 1) {
                LOGGER.error(">>RPG订单[{}]发起退款异常,MQ消息id:{}，重试次数已达阀值:{},不在处理",
                        orderSequence,msg_id, retryTimes);
                return true;
            }
            return false;
        }
        LOGGER.info(">>消费RPG消息完毕: {}", msg_id);
        return true;

    }
}

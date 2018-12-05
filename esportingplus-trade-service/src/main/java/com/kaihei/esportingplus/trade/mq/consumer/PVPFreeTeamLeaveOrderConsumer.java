package com.kaihei.esportingplus.trade.mq.consumer;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPTeamLeaveMemberAfterStartedVO;
import com.kaihei.esportingplus.trade.domain.service.PVPFreeOrderService;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@MQConsumer(topic = RocketMQConstant.TOPIC_PVP_FREE, tag = RocketMQConstant.QUIT_TEAM_AFTER_RUNNING,
        consumerGroup = RocketMQConstant.UPDATE_ORDER_CONSUMER_PVP_FREE_LEAVE_TEAM_GROUP)
public class PVPFreeTeamLeaveOrderConsumer extends AbstractMQPushConsumer<PVPTeamLeaveMemberAfterStartedVO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PVPFreeTeamLeaveOrderConsumer.class);

    @Autowired
    private PVPFreeOrderService pvpFreeOrderService;

    private CacheManager cacheManager = CacheManagerFactory.create();

    @Value("${refund.mq.retryTimes:5}")
    private int retryTimes;

    @Override
    public boolean process(PVPTeamLeaveMemberAfterStartedVO message, Map map) {
        int reconsume_times = (int) map.get("RECONSUME_TIMES");
        //校验消息为空
        Object msg_id = map.get("MSG_ID");

        if(message == null){
            LOGGER.info(">>收到PVP免费车队[已开车主动退出]消息为空：{}，MQ消息id:{}，不做处理",
                    message,
                    msg_id);
            return true;
        }

        String teamSequence = message.getTeamSequence();
        String uid = message.getTeamMemberVO().getUid();
        ///消息重复消费处理：消费一次缓存起来 如果存在说明此消息发生过消费，直接return true;
        msg_id = msg_id + "_" + message.hashCode();
        Boolean repeatConsume = cacheManager.exists(RedisKey.UPDATE_ORDER_HISTORY_FROM_TEAM_END + message);
        if(repeatConsume != null && repeatConsume){
            LOGGER.info(">>PVP免费车队[已开车主动退出]消息重复消费，车队:{}，uid:{},MQ消息id:{}，忽略处理",
                    teamSequence,uid,msg_id);
            return true;
        }

        boolean result = true;
        try {
            LOGGER.info("收到PVP免费车队[已开车主动退出]消息，MQ消息id:{},入参:{}"
                    , msg_id,message);

            //更新订单状态
            result = pvpFreeOrderService.updateOrderForLeaveTeam(message);

            //2 分钟失效：mq默认重试5次 1/5/10/30/60,将近2分钟。
            cacheManager.set(RedisKey.UPDATE_ORDER_HISTORY_FROM_TEAM_END + msg_id, StringUtils.EMPTY,120);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            LOGGER.error(">>处理PVP免费车队[已开车主动退出]消息异常，车队:{},uid:{},MQ消息id:{}，重试: {}"
                    ,teamSequence,uid,msg_id,reconsume_times + 1);
            if (reconsume_times >= retryTimes - 1) {
                LOGGER.error(">>处理PVP免费车队[已开车主动退出]消息异常,车队:{},uid:{},MQ消息id:{}，重试次数已达阀值:{},不在处理",
                        teamSequence,uid,msg_id, retryTimes);
                return true;
            }
            return false;
        }
        LOGGER.info(">>消费PVP免费车队[已开车主动退出]消息完毕: {}", msg_id);
        return result;

    }
}

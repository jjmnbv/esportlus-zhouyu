package com.kaihei.esportingplus.trade.mq.consumer;

import com.kaihei.commons.cache.utils.JsonsUtils;
import com.kaihei.esportingplus.common.constant.CommonConstants;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.gamingteam.api.mq.PVPFreeTeamStartOrderMessage;
import com.kaihei.esportingplus.trade.domain.service.PVPFreeOrderService;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@MQConsumer(topic = RocketMQConstant.TOPIC_PVP_FREE, tag = RocketMQConstant.CREATE_BAOJI_ORDER_TAG, consumerGroup = RocketMQConstant.PVP_FREE_ORDER_CONSUMER_GROUP)
public class PVPFreeTeamStartOrderConsumer extends
        AbstractMQPushConsumer<PVPFreeTeamStartOrderMessage> {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(PVPFreeTeamStartOrderConsumer.class);
    @Autowired
    private PVPFreeOrderService pvpFreeOrderService;

    @Override
    public boolean process(PVPFreeTeamStartOrderMessage vo, Map map) {
        try {
            String json = JsonsUtils.toJson(vo);
            LOGGER.info(">>订单服务车队开车接受到PVP免费车队消息通知，接收MQ的数据为:{}", json);
            if (ObjectTools.isEmpty(vo)) {
                return true;
            }
            pvpFreeOrderService.createTeamStartOrder(vo.getPvpTeamStartOrderVO());
            return true;

        } catch (Exception e) {
            int reconsume_times = (int) map.get("RECONSUME_TIMES");
            LOGGER.error(">>订单服务创建PVP免费车队订单及修改订单开车状态失败，当前是第{}次，距离重试阀值次数还剩余{}次",
                    reconsume_times + 1,
                    CommonConstants.MQ_RETRY_COUNT - reconsume_times - 1, e);
            if (reconsume_times >= CommonConstants.MQ_RETRY_COUNT - 1) {
                return true;
            }
            return false;
        }
    }
}

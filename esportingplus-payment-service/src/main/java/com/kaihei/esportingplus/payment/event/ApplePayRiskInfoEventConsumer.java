package com.kaihei.esportingplus.payment.event;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.kaihei.esportingplus.common.event.EventConsumer;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.payment.mq.producer.AppleRechargeRiskProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 苹果支付成功是发送消息
 *
 * @author xusisi
 */
@Component
public class ApplePayRiskInfoEventConsumer extends EventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ApplePayRiskInfoEventConsumer.class);

    @Autowired
    private AppleRechargeRiskProducer producer;

    @Subscribe
    @AllowConcurrentEvents
    public void sendToRiskMq(ApplePayRiskInfoEvent event) {

        String msg = JacksonUtils.toJson(event);
        logger.debug(">> 苹果充值成功后将信息发给风控 msg:{}", msg);
        try {
            producer.sendMessage("esportingplus_recharge", "recharge_black", event);
        } catch (Exception e) {
            logger.error("sendToRiskMq >> {}", e.getMessage());
        }
    }
}

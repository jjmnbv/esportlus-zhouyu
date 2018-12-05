package com.kaihei.esportingplus.payment.mq.producer;


import com.maihaoche.starter.mq.annotation.MQProducer;
import com.maihaoche.starter.mq.base.AbstractMQProducer;
import com.maihaoche.starter.mq.base.MessageBuilder;
import org.apache.rocketmq.common.message.Message;

/**
 * 暴鸡币苹果支付消息发送方
 *
 * @author xusisi
 */
@MQProducer
public class AppleRechargeRiskProducer extends AbstractMQProducer {

    public void sendMessage(String topic, String tag, Object object) throws Exception {
        Message message = MessageBuilder.of(object).topic(topic).tag(tag).build();
        syncSend(message);
    }
}

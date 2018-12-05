package com.kaihei.esportingplus.payment.mq.producer;

import com.alibaba.fastjson.JSONObject;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.maihaoche.starter.mq.annotation.MQTransactionProducer;
import com.maihaoche.starter.mq.base.AbstractMQTransactionProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 云账户回调通知处理-生产者
 * @author chenzhenjun
 */
@MQTransactionProducer(producerGroup= RocketMQConstant.EXTERNAL_CLOUD_NOTIFY_PRODUCER_GROUP)
public class CloudAccountNotifyProducer extends AbstractMQTransactionProducer{

    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object object) {
        if (object instanceof JSONObject) {
            // 消息入库-事务处理
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }
}

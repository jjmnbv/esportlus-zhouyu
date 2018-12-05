package com.kaihei.esportingplus.payment.mq.producer;

import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.payment.api.vo.CloudCreateMessageVo;
import com.maihaoche.starter.mq.annotation.MQTransactionProducer;
import com.maihaoche.starter.mq.base.AbstractMQTransactionProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 云账户提现订单入库-消息处理-生产者
 * @author chenzhenjun
 */
@MQTransactionProducer(producerGroup= RocketMQConstant.EXTERNAL_CLOUD_CREATE_PRODUCER_GROUP)
public class CloudAccountCreateProducer extends AbstractMQTransactionProducer {

    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object object) {
        if (object instanceof CloudCreateMessageVo) {
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

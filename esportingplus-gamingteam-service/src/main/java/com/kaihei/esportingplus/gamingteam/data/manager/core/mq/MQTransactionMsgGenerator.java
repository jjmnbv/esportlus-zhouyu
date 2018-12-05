package com.kaihei.esportingplus.gamingteam.data.manager.core.mq;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.MessageExt;

public interface MQTransactionMsgGenerator {

    String getTopic();

    String getTag();

    Object generateMsg();

    LocalTransactionState checkTransaction(MessageExt messageExt);
}

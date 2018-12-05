package com.kaihei.esportingplus.gamingteam.data.manager.core.mq;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MQTransactionMsgSupportor implements InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;

    private Map<MQKey, MQTransactionMsgGenerator> mqKeyMQTransactionMsgGeneratorMap;

    public Object generateMsg(MQKey mqKey) {
        return Optional.ofNullable(mqKeyMQTransactionMsgGeneratorMap).map(it -> it.get(mqKey))
                .map(MQTransactionMsgGenerator::generateMsg).orElse(null);
    }

    public LocalTransactionState checkTransaction(MessageExt messageExt) {
        return Optional.ofNullable(mqKeyMQTransactionMsgGeneratorMap)
                .map(it -> it.get(MQKey.builder().topic(messageExt.getTopic())
                        .tag(messageExt.getTags()).build()))
                .map(it -> it.checkTransaction(messageExt)).orElse(LocalTransactionState.UNKNOW);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, MQTransactionMsgGenerator> transactionMsgGeneratorMap = applicationContext
                .getBeansOfType(MQTransactionMsgGenerator.class);
        mqKeyMQTransactionMsgGeneratorMap = transactionMsgGeneratorMap.entrySet().stream()
                .collect(Collectors.toMap(e -> MQKey.builder().topic(e.getValue().getTopic())
                        .tag(e.getValue().getTag()).build(), Entry::getValue));
    }

}

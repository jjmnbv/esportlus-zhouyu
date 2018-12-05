package com.kaihei.esportingplus.gamingteam.rocketmq.producer;

import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.gamingteam.api.mq.TeamStartOrderMessage;
import com.kaihei.esportingplus.gamingteam.domain.service.TeamTransactionService;
import com.kaihei.esportingplus.gamingteam.rocketmq.localtransaction.StartTeamLocalTransaction;
import com.maihaoche.starter.mq.annotation.MQTransactionProducer;
import com.maihaoche.starter.mq.base.AbstractMQTransactionProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 立即开车消息发布
 * @author liangyi
 */
@MQTransactionProducer(producerGroup = RocketMQConstant.CREATE_BAOJI_ORDER_PRODUCER_GROUP)
public class StartTeamTransactionProducer extends AbstractMQTransactionProducer {

    private static final Logger logger = LoggerFactory.getLogger(StartTeamTransactionProducer.class);

    @Autowired
    TeamTransactionService teamTransactionService;

    /**
     * 本地事务方法
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        String transactionId = msg.getTransactionId();
        StartTeamLocalTransaction startTeamLocalTransaction = null;
        if (arg != null && arg instanceof StartTeamLocalTransaction) {
            startTeamLocalTransaction = (StartTeamLocalTransaction) arg;
        }
        TeamStartOrderMessage teamStartOrderMessage = FastJsonUtils
                .fromJson(msg.getBody(), TeamStartOrderMessage.class);
        logger.info(">> MQ 回调立即开车消息成功, 事务 id: {}", transactionId);
        // 提交本地事务
        if (ObjectTools.isNotNull(startTeamLocalTransaction)) {
            boolean startResult;
            try {
                startResult = teamTransactionService.startTeam(startTeamLocalTransaction);
                if (startResult) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
            } catch (Exception e) {
                logger.error(">> 立即开车, 本地事务执行失败! 事务 id: {},"
                                + " 立即开车(创建暴鸡订单)消息: {}, 立即开车本地事务: {}",
                        transactionId, teamStartOrderMessage, startTeamLocalTransaction, e);
            }
        }
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }

    /**
     * 本地事务确认
     *
     * executeLocalTransaction返回UNKNOW 的情况下 MQ会在一段时间后调用producerGroup相同的应用
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }
}
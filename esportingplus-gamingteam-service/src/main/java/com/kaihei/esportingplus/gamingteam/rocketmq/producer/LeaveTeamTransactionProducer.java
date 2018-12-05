package com.kaihei.esportingplus.gamingteam.rocketmq.producer;

import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.gamingteam.api.mq.RPGUpdateOrderStatusMessage;
import com.kaihei.esportingplus.gamingteam.domain.service.TeamTransactionService;
import com.kaihei.esportingplus.gamingteam.rocketmq.localtransaction.LeaveTeamLocalTransaction;
import com.maihaoche.starter.mq.annotation.MQTransactionProducer;
import com.maihaoche.starter.mq.base.AbstractMQTransactionProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 离开车队消息发布
 * 场景: 退出车队、踢出车队
 * @author liangyi
 */
@MQTransactionProducer(producerGroup = RocketMQConstant.UPDATE_ORDER_STATUS_PRODUCER_GROUP)
public class LeaveTeamTransactionProducer extends AbstractMQTransactionProducer {

    private static final Logger logger = LoggerFactory.getLogger(LeaveTeamTransactionProducer.class);

    @Autowired
    TeamTransactionService teamTransactionService;

    /**
     * 本地事务方法
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        String transactionId = msg.getTransactionId();
        LeaveTeamLocalTransaction leaveTeamLocalTransaction = null;
        if (arg != null && arg instanceof LeaveTeamLocalTransaction) {
            leaveTeamLocalTransaction = (LeaveTeamLocalTransaction) arg;
        }
        RPGUpdateOrderStatusMessage updateOrderStatusRPGMessage = FastJsonUtils
                .fromJson(msg.getBody(), RPGUpdateOrderStatusMessage.class);
        logger.info(">> MQ 回调离开车队消息成功, 事务 id: {}", transactionId);
        // 提交本地事务--离开车队无本地事务
        if (ObjectTools.isNotNull(leaveTeamLocalTransaction)) {
            try {
                teamTransactionService.leaveTeam(leaveTeamLocalTransaction);
                return LocalTransactionState.COMMIT_MESSAGE;
            } catch (Exception e) {
                logger.error(">> 离开车队, 本地事务执行失败!, 事务 id: {},"
                                + " 修改订单状态消息: {}, 离开车队本地事务: {}",
                        transactionId, updateOrderStatusRPGMessage, leaveTeamLocalTransaction, e);
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
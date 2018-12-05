package com.kaihei.esportingplus.gamingteam.rocketmq.producer;

import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.gamingteam.api.mq.RPGUpdateOrderStatusMessage;
import com.kaihei.esportingplus.gamingteam.domain.service.TeamTransactionService;
import com.kaihei.esportingplus.gamingteam.rocketmq.localtransaction.EndTeamLocalTransaction;
import com.maihaoche.starter.mq.annotation.MQTransactionProducer;
import com.maihaoche.starter.mq.base.AbstractMQTransactionProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 结束车队消息发布
 * 场景: 解散车队、正常结束车队
 *
 * @author liangyi
 */
@MQTransactionProducer(producerGroup = RocketMQConstant.UPDATE_ORDER_STATUS_PRODUCER_GROUP)
public class EndTeamTransactionProducer extends AbstractMQTransactionProducer {

    private static final Logger logger = LoggerFactory.getLogger(EndTeamTransactionProducer.class);

    @Autowired
    TeamTransactionService teamTransactionService;

    /**
     * 本地事务方法
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        String transactionId = msg.getTransactionId();
        EndTeamLocalTransaction endTeamLocalTransaction = null;
        if (arg != null && arg instanceof EndTeamLocalTransaction) {
            endTeamLocalTransaction = (EndTeamLocalTransaction) arg;
        }
        RPGUpdateOrderStatusMessage updateOrderStatusRPGMessage = FastJsonUtils
                .fromJson(msg.getBody(), RPGUpdateOrderStatusMessage.class);
        logger.info(">> MQ 回调解散(或正常结束)车队消息成功, 事务 id: {}", transactionId);
        // 提交本地事务
        if (ObjectTools.isNotNull(endTeamLocalTransaction)) {
            boolean transactionResult;
            try {
                transactionResult = teamTransactionService.endTeam(endTeamLocalTransaction);
                if (transactionResult) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
            } catch (Exception e) {
                logger.error(">> 解散(或正常结束)车队, 本地事务执行失败! 事务 id: {},"
                                + " 修改订单状态消息: {}, 解散(或正常结束)本地事务: {}",
                        transactionId, updateOrderStatusRPGMessage, endTeamLocalTransaction, e);
            }
        }
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }

    /**
     * 本地事务确认
     * <p>
     * executeLocalTransaction返回UNKNOW 的情况下 MQ会在一段时间后调用producerGroup相同的应用
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }
}
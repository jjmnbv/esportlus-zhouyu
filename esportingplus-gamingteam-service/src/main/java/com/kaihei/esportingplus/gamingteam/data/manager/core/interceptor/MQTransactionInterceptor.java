package com.kaihei.esportingplus.gamingteam.data.manager.core.interceptor;

import static com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPConstant.PVP_TEAM_MQ_AOP_ORDER;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.MQTransaction;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.TeamOperation;
import com.kaihei.esportingplus.gamingteam.data.manager.core.mq.MQKey;
import com.kaihei.esportingplus.gamingteam.data.manager.core.mq.MQTransactionMsgSupportor;
import com.maihaoche.starter.mq.MQException;
import com.maihaoche.starter.mq.annotation.MQTransactionProducer;
import com.maihaoche.starter.mq.base.AbstractMQTransactionProducer;
import com.maihaoche.starter.mq.base.MessageBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;

@Slf4j
@Aspect
@MQTransactionProducer(producerGroup = "MQTransactionInterceptor")
public class MQTransactionInterceptor extends AbstractMQTransactionProducer implements Ordered {

    private ThreadLocal<Object> pjphloder = new ThreadLocal<>();

    @Autowired
    private MQTransactionMsgSupportor mqTransactionMsgSupportor;

    @Around(value = "@annotation(teamOperation)&&@annotation(mqTransaction)", argNames = "pjp,teamOperation,mqTransaction")
    public Object around(ProceedingJoinPoint pjp, TeamOperation teamOperation,
            MQTransaction mqTransaction) throws Throwable {
        String topic = mqTransaction.topic();
        String tag = mqTransaction.tag();
        Object msg = mqTransactionMsgSupportor
                .generateMsg(MQKey.builder().topic(topic).tag(tag).build());
        //没有生成消息、直接执行方法
        if (msg == null) {
            return pjp.proceed();
        }

        pjphloder.set(pjp);
        //发送事务消息
        try {
            log.info("发送MQ消息{}", msg);
            SendResult sendResult = this.sendMessageInTransaction(
                    MessageBuilder.of(msg)
                            .topic(topic).tag(tag)
                            .build(),
                    null);
            //MQ发送失败
            if (sendResult == null || !SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
                throw new BusinessException(BizExceptionEnum.TEAM_MQ_SEND_FAIL);
            }
        } catch (MQException e) {
            log.error("MQ消息发送失败", e);
            throw new BusinessException(BizExceptionEnum.TEAM_MQ_SEND_FAIL);
        }

        Object result = pjphloder.get();

        if (result instanceof Throwable) {
            throw (Throwable) result;
        }
        log.info("方法返回 -> {}", result);
        return result;
    }

    /**
     * 重写此方法处理发送后的逻辑
     *
     * @param message 发送消息体
     * @param e 报错信息
     */
    @Override
    protected void doAfterThrow(Message message, Throwable e) {
        log.error("------------MQ消息发送失败原始错误------------\n {}", e.getStackTrace()[0]);
    }

    @AfterReturning(value = "@annotation(teamOperation)&&@annotation(mqTransaction)", argNames = "teamOperation,mqTransaction")
    public void afterReturning(TeamOperation teamOperation, MQTransaction mqTransaction) {
        pjphloder.remove();
    }

    @AfterThrowing(value = "@annotation(teamOperation)&&@annotation(mqTransaction)", argNames = "teamOperation,mqTransaction")
    public void afterThrowing(TeamOperation teamOperation, MQTransaction mqTransaction) {
        pjphloder.remove();
    }

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try {
            log.info("执行本地事务");
            Object proceed = ((ProceedingJoinPoint) pjphloder.get()).proceed();
            pjphloder.set(proceed);
            return LocalTransactionState.COMMIT_MESSAGE;
        } catch (Throwable throwable) {
            pjphloder.set(throwable);
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        return mqTransactionMsgSupportor.checkTransaction(msg);
    }

    @Override
    public int getOrder() {
        return PVP_TEAM_MQ_AOP_ORDER;
    }
}

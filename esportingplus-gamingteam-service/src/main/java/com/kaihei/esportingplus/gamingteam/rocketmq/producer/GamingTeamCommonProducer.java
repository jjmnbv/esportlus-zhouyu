package com.kaihei.esportingplus.gamingteam.rocketmq.producer;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.CommonConstants;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.gamingteam.api.mq.PVPUpdateOrderStatusMessage;
import com.kaihei.esportingplus.gamingteam.api.mq.RPGUpdateOrderStatusMessage;
import com.maihaoche.starter.mq.annotation.MQProducer;
import com.maihaoche.starter.mq.base.AbstractMQProducer;
import com.maihaoche.starter.mq.base.MessageBuilder;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * 车队非事务消息生产者
 * @author liangyi
 */
@MQProducer
public class GamingTeamCommonProducer extends AbstractMQProducer {

    @Value("${boss.confirm.paid.refund.retry}")
    private String refundMQRetry;

    private static final Logger logger = LoggerFactory.getLogger(GamingTeamCommonProducer.class);

    protected static CacheManager cacheManager = CacheManagerFactory.create();

    /**
     * 老板确认支付,此时老板已不在车队,发送退款消息
     * @param bossUid 老板 uid
     * @param teamSequence 车队序列号
     * @param orderSequence 订单序列号
     */
    public void sendRefundMessageAfterBossPaidSuccess(String bossUid,
            String teamSequence, String orderSequence) {
        String refundMQKey = String.format(RedisKey.TEAM_REFUND_MQ, teamSequence, bossUid);
        Boolean exists = cacheManager.exists(refundMQKey);
        if (exists) {
            // 标记存在, 直接跳过不做任何处理
            logger.warn(">> 老板[{}]在车队[{}]支付成功后,已成功发送过退款消息到MQ,订单号:[{}]",
                    bossUid, teamSequence, orderSequence);
            return;
        }
        String[] refundMQRetryArr = refundMQRetry.split("/");
        int retryTime = 0;
        do {
            try {
                // 重试 3 次发起退款
                // 这里的 message 直接传订单号
                Message message = MessageBuilder.of(orderSequence).topic(RocketMQConstant.TOPIC_RPG)
                        .tag(RocketMQConstant.REFUND_ORDER_TAGS_FROM_TEAM).build();
                SendResult sendResult = super.syncSend(message);
                if (ObjectTools.isNotNull(sendResult)
                        && SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
                    logger.info(">> 老板[{}]在车队[{}]支付成功后,发送退款消息到MQ成功,"
                                    + "订单号:[{}],消息id:[{}]",
                            bossUid, teamSequence, orderSequence, sendResult.getMsgId());
                    // 加个标识表示这个消息已经发过了
                    // team:teamSequence:bossUid -> orderSequence 过期时间设置为 2分钟
                    cacheManager.set(refundMQKey, orderSequence,
                            CommonConstants.ONE_HOUR_SECONDS/30);
                    return;
                }
                // retryTime++;
                logger.error(">> 老板[{}]在车队[{}]中支付成功,"
                                + "发送退款消息到MQ失败,订单号:[{}],准备重试第[{}]次!",
                        bossUid, teamSequence, orderSequence, retryTime+1);
                Long interval = Long.valueOf(refundMQRetryArr[retryTime]);
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    logger.error(">> 老板[{}]在车队[{}]中支付成功,发送退款消息到MQ失败,"
                                    + "订单号:[{}],准备重试第[{}]次时线程中断异常!",
                            bossUid, teamSequence, orderSequence, retryTime+1, e);
                }
            } catch (Exception e) {
                logger.error(">> 老板[{}]在车队[{}]中支付成功,发送退款消息到MQ异常!订单号:[{}]",
                        bossUid, teamSequence, orderSequence, e);
            }
            retryTime++;
        } while (retryTime < refundMQRetryArr.length);
    }


    /**
     * 队员离开车队发送更新订单状态消息
     * 开车前: 老板支付后被踢出、老板支付后主动退出
     * 开车后: 老板主动退出、暴鸡主动退出
     *
     * @param memberUid
     * @param updateOrderStatusRPGMessage
     */
    public void sendUpdateOrderStatusMessage4LeaveTeam(String memberUid,
            RPGUpdateOrderStatusMessage updateOrderStatusRPGMessage) {
        String teamSequence = updateOrderStatusRPGMessage
                .getUpdateOrderStatusRPGParams().getTeamSequence();
        Message message = MessageBuilder.of(updateOrderStatusRPGMessage).topic(RocketMQConstant.TOPIC_RPG)
                .tag(RocketMQConstant.UPDATE_ORDER_STATUS_TAG).build();
        SendResult sendResult = super.syncSend(message);
        if (ObjectTools.isNotNull(sendResult)
                && SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
            logger.info(">> 用户[{}]离开车队[{}],发送修改订单状态消息到MQ成功,消息id:[{}]",
                    memberUid, teamSequence, sendResult.getMsgId());
        } else {
            logger.error(">> 用户[{}]离开车队[{}],发送修改订单状态消息到MQ失败,消息id:[{}],发送结果:{}",
                    memberUid, teamSequence, sendResult.getMsgId(), sendResult);
            throw new BusinessException(BizExceptionEnum.TEAM_MQ_SEND_FAIL);
        }
    }

    public void sendRollbackTeamOrderStatus(String memberUid,
            PVPUpdateOrderStatusMessage updateOrderStatusPVPMessage){
        String teamSequence = updateOrderStatusPVPMessage
                .getUpdateOrderStatusPVPParams().getTeamSequence();
        Message message = MessageBuilder.of(updateOrderStatusPVPMessage).topic(RocketMQConstant.TOPIC_PVP)
                .tag(RocketMQConstant.UPDATE_ORDER_STATUS_TAG).build();
        SendResult sendResult = super.syncSend(message);
        if (ObjectTools.isNotNull(sendResult)
                && SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
            logger.info(">> 用户[{}]离开车队[{}],发送回退订单状态消息到MQ成功,消息id:[{}]",
                    memberUid, teamSequence, sendResult.getMsgId());
        } else {
            logger.error(">> 用户[{}]离开车队[{}],发送回退订单状态消息到MQ失败,消息id:[{}],发送结果:{}",
                    memberUid, teamSequence, sendResult.getMsgId(), sendResult);
            throw new BusinessException(BizExceptionEnum.TEAM_MQ_SEND_FAIL);
        }
    }
}
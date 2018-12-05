package com.kaihei.esportingplus.gamingteam.event;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.gamingteam.rocketmq.producer.GamingTeamCommonProducer;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * 老板确认支付成功
 *
 * @author liangyi
 */
public abstract class AbstractBossConfirmPaidSuccessHandler<T> {

    @Autowired
    GamingTeamCommonProducer gamingTeamCommonProducer;

    @Value("${boss.confirm.paid.check.retry}")
    private String checkPaidRetry;

    protected static final Logger logger = LoggerFactory
            .getLogger(AbstractBossConfirmPaidSuccessHandler.class);

    /**
     * 处理
     * @param bossUid 老板 uid
     * @param teamSequence 车队序列号
     * @param orderSequence 订单序列号
     */
    public void process(String bossUid, String teamSequence, String orderSequence) {
        T bossMember = getBossMember(bossUid, teamSequence);
        if (ObjectTools.isNotNull(bossMember)) {
            memberInTeam(bossUid, teamSequence, orderSequence, bossMember);
        }
        memberNotInTeam(bossUid, teamSequence, orderSequence);
    }

    /**
     * 获取车队队员
     * @param bossUid
     * @param teamSequence
     * @return
     */
    protected abstract T getBossMember(String bossUid, String teamSequence);

    /**
     * 获取所有车队队员
     * @param teamSequence
     * @return
     */
    protected abstract List<T> getAllTeamMember(String teamSequence);

    /**
     * 队员在车队中的逻辑
     */
    protected void memberInTeam(String bossUid, String teamSequence,
            String orderSequence, T bossMember) {
        checkBossPaidSuccess(bossUid, teamSequence, orderSequence, bossMember);
    }

    /**
     * 队员不在车队中的逻辑
     *
     * @param bossUid 老板 uid
     * @param teamSequence 车队序列号
     * @param orderSequence 订单序列号
     */
    protected void memberNotInTeam(String bossUid, String teamSequence, String orderSequence) {
        // 发送 MQ 退款消息
        sendRefundMessage(bossUid, teamSequence, orderSequence);
    }

    /**
     * 发送 MQ 退款消息, 内部会重试 3次
     * @param bossUid 老板 uid
     * @param teamSequence 车队序列号
     * @param orderSequence 订单序列号
     */
    protected void sendRefundMessage(String bossUid, String teamSequence, String orderSequence) {
        gamingTeamCommonProducer.sendRefundMessageAfterBossPaidSuccess(bossUid,
                teamSequence, orderSequence);
    }

    /**
     * 调用订单服务校验支付结果
     * @param orderSequence 订单序列号
     * @return
     */
    protected abstract ResponsePacket<Void> callOrderServiceCheckTeamMemberPayed(
            String orderSequence);


    /**
     * 校验老板支付状态
     *
     * @param bossUid 老板 uid
     * @param teamSequence 车队序列号
     * @param bossMember 老板队员
     */
    public void checkBossPaidSuccess(String bossUid, String teamSequence,
            String orderSequence, T bossMember) {
        String[] checkPaidRetryArr = checkPaidRetry.split("/");
        int retryTime = 0;

        do {
            try {
                // 老板支付成功--调用订单服务校验其是否已成功支付
                logger.info(">> 老板[{}]在车队[{}]中订单[{}]支付成功,开始调用交易服务校验支付状态.",
                        bossUid, teamSequence, orderSequence);
                long start = System.currentTimeMillis();
                ResponsePacket<Void> checkPayedResponse =
                        callOrderServiceCheckTeamMemberPayed(orderSequence);
                logger.info(">> 老板[{}]在车队[{}]中订单[{}]支付成功,调用交易服务校验支付状态耗时:{}ms",
                        bossUid, teamSequence, orderSequence, (System.currentTimeMillis() - start));
                if (checkPayedResponse.responseSuccess()) {
                    // 支付成功
                    afterBossPaidSuccessInTeam(teamSequence, orderSequence, bossMember);
                    break;
                }
                // retryTime++;
                logger.error(">> 老板[{}]在车队[{}]中订单[{}]支付成功,"
                                + "调用交易服务校验支付状态错误: {},准备重试第[{}]次!",
                        bossUid, teamSequence, orderSequence, checkPayedResponse, retryTime+1);
                Long interval = Long.valueOf(checkPaidRetryArr[retryTime]);
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    logger.error(">> 老板[{}]在车队[{}]中订单[{}]支付成功,"
                                    + "调用交易服务校验支付状态错误,准备重试第[{}]次时线程中断异常!",
                            bossUid, teamSequence, orderSequence, retryTime+1, e);
                }
            } catch (Exception e) {
                logger.error(">> 老板[{}]在车队[{}]中支付成功,调用交易服务校验支付异常!",
                        bossUid, teamSequence, e);
            }
            retryTime++;
        }
        while (retryTime < checkPaidRetryArr.length);
    }

    /**
     * 老板还在车队中, 确认支付成功后的处理
     * @param teamSequence
     * @param orderSequence
     * @param bossMember
     */
    protected abstract void afterBossPaidSuccessInTeam(String teamSequence,
            String orderSequence, T bossMember);


}

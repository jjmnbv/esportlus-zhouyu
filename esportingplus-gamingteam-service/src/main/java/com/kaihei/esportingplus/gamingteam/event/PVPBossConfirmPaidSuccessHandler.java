package com.kaihei.esportingplus.gamingteam.event;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.gamingteam.api.enums.PVPTeamMemberStatusEnum;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPRedisTeamVO;
import com.kaihei.esportingplus.gamingteam.data.manager.TeamServiceSupport;
import com.kaihei.esportingplus.gamingteam.data.manager.cache.PVPTeamCacheManager;
import com.kaihei.esportingplus.gamingteam.data.manager.cache.PVPTeamMemberCacheManager;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamMemberPVP;
import com.kaihei.esportingplus.trade.api.feign.RPGOrdersServiceClient;
import com.kaihei.esportingplus.trade.api.params.PVPBossCurrentPaidAmountParams;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * PVP 付费车队老板确认支付成功事件消费
 *
 * @author liangyi
 */
@Component
public class PVPBossConfirmPaidSuccessHandler
        extends AbstractBossConfirmPaidSuccessHandler<TeamMemberPVP> {

    @Autowired
    PVPTeamCacheManager pvpTeamCacheManager;
    @Autowired
    PVPTeamMemberCacheManager pvpTeamMemberCacheManager;
    @Autowired
    RPGOrdersServiceClient rpgOrdersServiceClient;
    @Autowired
    TeamServiceSupport teamServiceSupport;

    @Override
    protected TeamMemberPVP getBossMember(String bossUid, String teamSequence) {
        return pvpTeamMemberCacheManager.getTeamMember(teamSequence, bossUid, TeamMemberPVP.class);
    }

    @Override
    protected List<TeamMemberPVP> getAllTeamMember(String teamSequence) {
        return pvpTeamMemberCacheManager.getTeamMemberList(teamSequence, TeamMemberPVP.class);
    }

    // TODO pvpOrderServiceClient
    @Override
    protected ResponsePacket<Void> callOrderServiceCheckTeamMemberPayed(String orderSequence) {
        return rpgOrdersServiceClient.checkTeamMemberPayed(orderSequence);
    }

    /**
     * 老板还在车队中, 确认支付成功后的处理
     * @param teamSequence
     * @param orderSequence
     * @param bossMember
     */
    @Override
    public void afterBossPaidSuccessInTeam(String teamSequence, String orderSequence,
            TeamMemberPVP bossMember) {
        String bossUid = bossMember.getUid();
        // 是否需要退款
        boolean shouldRefund = checkShouldRefund(teamSequence,
                orderSequence, bossMember.getGameDanId());

        if (shouldRefund) {
            // 退款
            sendRefundMessage(bossUid, teamSequence, orderSequence);
            return;
        }
        // 如果此时老板状态为已准备, 更新老板状态为已支付
        if (bossMember.getStatus() != PVPTeamMemberStatusEnum.PREPARE_READY.getCode()) {
            logger.warn(">> 老板[{}]在车队[{}]中支付成功,当前老板状态为[{}]", bossUid,
                    teamSequence, PVPTeamMemberStatusEnum.of(bossMember.getStatus()).getMsg());
        }
        bossMember.setStatus((byte)PVPTeamMemberStatusEnum.PAID.getCode());
        bossMember.setGmtModified(new Date());
        pvpTeamMemberCacheManager.restoreTeamMember(bossMember, teamSequence);

        logger.info(">> 老板[{}]在车队[{}]中支付成功,更新其状态为[{}]",
                bossUid, teamSequence, PVPTeamMemberStatusEnum.PREPARE_READY.getMsg());

        // 删除老板定时任务
        teamServiceSupport.removeBossPaymentTimeoutJob(teamSequence, bossMember.getUid());

        // 推送老板支付成功状态通知
        teamServiceSupport.postBossPaidSuccessEvent(teamSequence);
    }

    /**
     * 校验老板是否应该退款, 以下几种情况需要退款
     *  1. 实际支付金额 != 当前支付金额
     *  2. 车队未满员
     *  3. 车队中有尚未准备的队员(流程是全员都准备了才能去支付)
     *
     * @param teamSequence
     * @param orderSequence
     * @param bossGameDanId
     * @return
     */
    private boolean checkShouldRefund(String teamSequence,
            String orderSequence, Integer bossGameDanId) {

        PVPRedisTeamVO pvpRedisTeamVO = pvpTeamCacheManager.queryTeamInfoBySequence(teamSequence);
        List<TeamMemberPVP> allTeamMember = getAllTeamMember(teamSequence);

        // 实际支付金额 TODO
        PVPBossCurrentPaidAmountParams.builder()
                .orderSequence(orderSequence)
                .gameId(pvpRedisTeamVO.getGameId())
                .settlementType(pvpRedisTeamVO.getSettlementType().intValue())
                .settlementNumber(pvpRedisTeamVO.getSettlementNumber())
                .bossGameDanId(bossGameDanId)
                .baojiLevelCodeList(allTeamMember.stream()
                        // 排除老板
                        .filter(m -> m.getUserIdentity() != UserIdentityEnum.BOSS.getCode())
                        .map(TeamMemberPVP::getBaojiLevel)
                        .collect(Collectors.toList())).build();

        // 车队未满员
        if (allTeamMember.size() != pvpRedisTeamVO.getActuallyPositionCount()) {
            return true;
        }
        // 车队中有未准备的队员
        /*
         * 这种情况不需要退款了, 因为前面已经在当前金额和实际支付金额处理了
         * 出现这种情况的原因只有车队A先退出, B上车, 此时B未准备
         * 假设A是暴鸡, B是老板, 则前面的实际金额和当前金额已经不一样了
         * 假设A是老板, B也是老板, 则应付款金额一样, 不需要退款, 需要告诉思勇全员准备推送通知的情况
         * 假设A是超级暴鸡, B是普通暴鸡, 则前面的实际金额和当前金额已经不一样了
         */
        /*return allTeamMember.stream()
                .filter(m -> PVPTeamMemberStatusEnum.WAIT_READY.getCode() == m.getStatus())
                .findAny()
                .isPresent();*/
        return false;
    }


}

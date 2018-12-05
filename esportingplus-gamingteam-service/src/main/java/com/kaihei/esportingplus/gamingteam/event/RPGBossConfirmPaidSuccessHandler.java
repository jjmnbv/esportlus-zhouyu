package com.kaihei.esportingplus.gamingteam.event;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.gamingteam.api.enums.TeamMemberStatusEnum;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGRedisTeamMemberVO;
import com.kaihei.esportingplus.gamingteam.data.manager.TeamServiceSupport;
import com.kaihei.esportingplus.gamingteam.data.manager.rpg.RPGTeamMemberCacheManager;
import com.kaihei.esportingplus.trade.api.feign.RPGOrdersServiceClient;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * RPG 车队老板确认支付成功事件消费
 *
 * @author liangyi
 */
@Component
public class RPGBossConfirmPaidSuccessHandler
        extends AbstractBossConfirmPaidSuccessHandler<RPGRedisTeamMemberVO> {

    @Autowired
    RPGTeamMemberCacheManager rpgTeamMemberCacheManager;
    @Autowired
    RPGOrdersServiceClient rpgOrdersServiceClient;
    @Autowired
    TeamServiceSupport teamServiceSupport;

    @Override
    protected RPGRedisTeamMemberVO getBossMember(String bossUid, String teamSequence) {
        return rpgTeamMemberCacheManager.getRedisTeamMember(teamSequence, bossUid);
    }

    @Override
    protected List<RPGRedisTeamMemberVO> getAllTeamMember(String teamSequence) {
        return null;
    }

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
            RPGRedisTeamMemberVO bossMember) {
        String bossUid = bossMember.getUid();
        // 如果此时老板的状态为待支付, 则更新其在 redis中状态为: 待入团
        if (bossMember.getStatus() != TeamMemberStatusEnum.WAIT_FOR_PAY.getCode()) {
            logger.warn(">> 老板[{}]在车队[{}]中支付成功,老板在车队中的当前状态为[{}]", bossUid,
                    teamSequence, TeamMemberStatusEnum.of(bossMember.getStatus()).getMsg());
        }
        bossMember.setStatus(TeamMemberStatusEnum.PREPARE_JOIN_TEAM.getCode());
        bossMember.setGmtModified(new Date());
        rpgTeamMemberCacheManager.updateRPGTeamMember(bossMember);

        logger.info(">> 老板[{}]在车队[{}]中支付成功,更新其状态为[{}]",
                bossUid, teamSequence,
                TeamMemberStatusEnum.PREPARE_JOIN_TEAM.getMsg());

        // 删除老板定时任务
        teamServiceSupport.removeBossPaymentTimeoutJob(teamSequence, bossUid);

        // 推送老板支付成功状态通知
        teamServiceSupport.postBossPaidSuccessEvent(teamSequence);
    }


}

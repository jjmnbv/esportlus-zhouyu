package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.operation.freechance;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.SettlementTypeEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.KickOutTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.after.AfterOperation;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamMemberPVPFree;
import com.kaihei.esportingplus.marketing.api.feign.UserFreeCouponsServiceClient;
import com.kaihei.esportingplus.marketing.api.vo.UserFreeCouponsQueryResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 老板被踢或主动离开车队返还免费机会
 */
@Component
@OnScene(includes = {KickOutTeamScene.class})
public class TeamMemberBeKickoutBackFreeChanceOperation extends
        AfterOperation<TeamGamePVPFree> {

    @Autowired
    private PVPContextHolder<TeamGamePVPFree, TeamMemberPVPFree> pvpContextHolder;

    @Autowired
    private UserFreeCouponsServiceClient userFreeCouponsServiceClient;

    @Override
    public void doOperate() {
        Team team = pvpContextHolder.getContext().getTeam();
        SettlementTypeEnum round = SettlementTypeEnum.ROUND;
        int freecount = 1;
        if (round.equals(SettlementTypeEnum.getByCode(team.getSettlementType()))) {
            freecount = team.getSettlementNumber().intValue();
        }

        TeamMemberPVPFree leave = pvpContextHolder.getContext().getLeave().get(0);
        log.info("返回用户{} 消费的{}张 优惠卷,{}", leave.getUid(), freecount, leave.getCouponsIds());
        UserFreeCouponsQueryResultVo data = userFreeCouponsServiceClient
                .returnFreeCoupons(leave.getCouponsIds()).getData();

        if (data.getOprResult() == null || !data.getOprResult()) {
            throw new BusinessException(BizExceptionEnum.TEAM_OPERATE_TOO_FAST);
        }
        
        log.info("返回消费的{}张优惠卷,{} -> {}", freecount, leave.getCouponsIds(), data);
    }
}

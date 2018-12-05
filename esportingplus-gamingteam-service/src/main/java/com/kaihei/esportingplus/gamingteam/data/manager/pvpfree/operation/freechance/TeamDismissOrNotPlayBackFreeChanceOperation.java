package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.operation.freechance;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.GameResultEnum;
import com.kaihei.esportingplus.common.enums.SettlementTypeEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnCondition;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.condition.TeamEndWithNotPlayCondition;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.DismissTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.after.AfterOperation;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamMemberPVPFree;
import com.kaihei.esportingplus.marketing.api.feign.UserFreeCouponsServiceClient;
import com.kaihei.esportingplus.marketing.api.vo.UserFreeCouponsQueryResultVo;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 车队解散或存在未玩，返还免费机会
 */
@Component
@OnScene(includes = {DismissTeamScene.class})
@OnCondition(TeamEndWithNotPlayCondition.class)
public class TeamDismissOrNotPlayBackFreeChanceOperation extends
        AfterOperation<TeamGamePVPFree> {

    @Autowired
    private UserFreeCouponsServiceClient userFreeCouponsServiceClient;
    @Autowired
    private PVPContextHolder<TeamGamePVPFree, TeamMemberPVPFree> pvpContextHolder;
    @Autowired
    private TeamEndWithNotPlayCondition teamEndWithNotPlayCondition;

    @Override
    public void doOperate() {
        PVPContext<TeamGamePVPFree, TeamMemberPVPFree> context = pvpContextHolder.getContext();
        List<TeamMemberPVPFree> teamMemberList = context.getTeamMemberList();

        Team team = context.getTeam();

        SettlementTypeEnum round = SettlementTypeEnum.ROUND;
        int freecount = 0;

        if (round.equals(SettlementTypeEnum.getByCode(team.getSettlementType()))) {
            if (pvpContextHolder.getScene() instanceof DismissTeamScene) {
                freecount = team.getSettlementNumber().intValue();
            } else if (teamEndWithNotPlayCondition.condition()) {
                long wins = context.getTeamGameResults().stream()
                        .filter(it -> GameResultEnum.ROUNDS_VICTORY
                                .equals(GameResultEnum.fromCode(it.getGameResult())))
                        .count();
                freecount = (int) wins;
            }
        }
        int ffc = freecount;

        List<Long> coupons = teamMemberList.stream().map(TeamMemberPVPFree::getCouponsIds)
                //null过虑
                .filter(it -> !Objects.isNull(it))
                //倒序
                .peek(Collections::reverse)
                //只返回没打的次数
                .flatMap(it -> it.stream().limit(ffc))
                .collect(Collectors.toList());

        if (!coupons.isEmpty()) {
            UserFreeCouponsQueryResultVo data = userFreeCouponsServiceClient
                    .returnFreeCoupons(coupons)
                    .getData();
            if (data.getOprResult() == null || !data.getOprResult()) {
                throw new BusinessException(BizExceptionEnum.TEAM_OPERATE_TOO_FAST);
            }
            log.info("返回消费的免费卷,{} -> {}", coupons, data);
        }

    }
}

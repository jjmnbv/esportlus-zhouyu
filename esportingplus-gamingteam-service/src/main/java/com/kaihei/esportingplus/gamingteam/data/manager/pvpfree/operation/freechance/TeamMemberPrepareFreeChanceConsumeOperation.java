package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.operation.freechance;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.SettlementTypeEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.BossConfirmPrepareScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.after.AfterOperation;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamMemberPVPFree;
import com.kaihei.esportingplus.marketing.api.feign.UserFreeCouponsServiceClient;
import com.kaihei.esportingplus.marketing.api.vo.UserFreeCouponsQueryResultVo;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 老板车队准备、消费免费机会
 */
@Component
@OnScene(includes = BossConfirmPrepareScene.class)
public class TeamMemberPrepareFreeChanceConsumeOperation extends
        AfterOperation<TeamGamePVPFree> {

    @Autowired
    private UserFreeCouponsServiceClient userFreeCouponsServiceClient;

    @Autowired
    private PVPContextHolder<TeamGamePVPFree, TeamMemberPVPFree> pvpContextHolder;

    @Override
    public void doOperate() {
        Team team = pvpContextHolder.getContext().getTeam();
        SettlementTypeEnum round = SettlementTypeEnum.ROUND;
        int freecount = 1;
        if (round.equals(SettlementTypeEnum.getByCode(team.getSettlementType()))) {
            freecount = team.getSettlementNumber().intValue();
        }
        TeamMemberPVPFree me = pvpContextHolder.getContext().getMe();
        List<Long> couponsIds = me.getCouponsIds();
        //准备 -> 取消  -> 准备 不再消费免费卷
        if (couponsIds == null || couponsIds.isEmpty() || couponsIds.size() < freecount) {
            int fc = couponsIds == null ? freecount : freecount - couponsIds.size();
            //消费优惠卷
            UserFreeCouponsQueryResultVo userFreeCouponsQueryResultVo = userFreeCouponsServiceClient
                    .reduceFreeCoupons(me.getUid(), fc)
                    .getData();
            log.info("消费 {} 张优惠卷 -> {}", fc, userFreeCouponsQueryResultVo);

            Boolean oprResult = userFreeCouponsQueryResultVo.getOprResult();
            if (!oprResult) {
                throw new BusinessException(BizExceptionEnum.TEAM_FREE_CHANCE_NOT_ADEQATE);
            }

            me.setCouponsIds(userFreeCouponsQueryResultVo.getCouponsIds());
            me.setGmtModified(new Date());
        }

    }
}

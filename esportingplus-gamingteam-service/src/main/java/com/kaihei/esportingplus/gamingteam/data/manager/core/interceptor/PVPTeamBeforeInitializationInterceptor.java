package com.kaihei.esportingplus.gamingteam.data.manager.core.interceptor;

import static com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPConstant.PVP_TEAM_BEFORE_INIT_AOP_ORDER;

import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.api.enums.TeamStatusEnum;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.TeamOperation;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPConstant;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.populator.RoomNumberGenerator;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import java.util.ArrayList;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @author 谢思勇
 *
 * 根据注解@see {@link TeamInitialization}
 *
 * 在有该注解的方法前初始化 {@link PVPContext}并放入 {@link PVPContextHolder}
 *
 * AOP序号为102
 */
@Slf4j
@Aspect
@Component
public class PVPTeamBeforeInitializationInterceptor implements Ordered {

    private final String teamSequencePrefix = PVPConstant.TEAM_SEQUENCE_PREFIX;

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private PVPContextHolder<TeamGame, TeamMember> pvpContextHolder;

    @Autowired
    private RoomNumberGenerator roomNumGenerator;

    @Override
    public int getOrder() {
        return PVP_TEAM_BEFORE_INIT_AOP_ORDER;
    }

    @Before("@annotation(teamOperation)")
    public void before(TeamOperation teamOperation) {
        //初始化Team和TeamGamePVP对象
        if (!teamOperation.init()) {
            return;
        }

        //线程已经有值了
        if (pvpContextHolder.getContext() != null) {
            return;
        }

        log.info("PVPTeamInitializationInterceptor 前处理 -> 初始化器");
        if (pvpContextHolder.getUser() == null) {
            pvpContextHolder.setUser(UserSessionContext.getUser());
        }

        Team team = initTeam();

        PVPContext<TeamGame, TeamMember> pvpContext = PVPContext.builder()
                .team(team)
                .teamMemberList(new ArrayList<>())
                .build();

        pvpContextHolder.setContext(pvpContext);
    }

    private Team initTeam() {
        String sequence = teamSequencePrefix + snowFlake.nextId();

        return Team.builder()
                .sequence(sequence)
                .groupId(sequence)
                .roomNum(roomNumGenerator.next())
                .status(((byte) (TeamStatusEnum.PREPARING.getCode())))

                .gmtCreate(new Date())
                .gmtModified(new Date())
                .build();
    }
}

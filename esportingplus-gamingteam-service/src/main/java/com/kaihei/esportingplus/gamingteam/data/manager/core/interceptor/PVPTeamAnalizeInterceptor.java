package com.kaihei.esportingplus.gamingteam.data.manager.core.interceptor;

import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.tools.CollectionUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.TeamOperation;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPConstant;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * {@link #before(TeamOperation)} 数据操作、对比备份的数据，整理出新上车的用户、和退出车队的用户，供其他AOP，如：推送、缓存操作使用
 *
 * @author 谢思勇
 */
@Slf4j
@Aspect
@Component
public class PVPTeamAnalizeInterceptor implements Ordered {

    @Autowired
    private PVPContextHolder<TeamGame, TeamMember> pvpContextHolder;

    @Value("${python.identity-uri}")
    private String pythonUri;

    @Before("@annotation(teamOperation)")
    public void before(TeamOperation teamOperation) {
        log.info("数据操作前处理....");
        preAnalyze(pvpContextHolder.getBackup());
        preAnalyze(pvpContextHolder.getContext());
    }

    /**
     * 比较context和备份内容，筛选出被修改过的TeamMember
     */
    @SuppressWarnings("unchecked")
    @AfterReturning("@annotation(teamOperation)")
    public void afterReturning(TeamOperation teamOperation) {
        log.info("数据操作后处理....");
        preAnalyze(pvpContextHolder.getContext());
        afterReturningAnalyze();
    }

    void afterReturningAnalyze() {
        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        PVPContext<TeamGame, TeamMember> backup = pvpContextHolder.getBackup();

        List<TeamMember> teamMemberList = context.getTeamMemberList();
        List<TeamMember> backupTeamMemberList = backup.getTeamMemberList();

        Map<String, TeamMember> uidTeamMemberMap = context.getUidTeamMemberMap();
        Map<String, TeamMember> backupUidTeamMemberMap = backup.getUidTeamMemberMap();
        context.setRestores(new ArrayList<>());
        //找出加入的 和 需要重新缓存的
        for (int i = 0; i < teamMemberList.size(); i++) {
            TeamMember teamMember = teamMemberList.get(i);
            TeamMember backupTM = backupUidTeamMemberMap.get(teamMember.getUid());
            if (backupTM != null && backupTM.getGmtModified().equals(teamMember.getGmtModified())) {
                continue;
            }
            if (backupTM == null) {
                context.setJoin(teamMember);
            } else if (!backupTM.getGmtModified().equals(teamMember.getGmtModified())) {
                context.getRestores().add(teamMember);
            }
        }

        //两个列表相等、直接返回
        if (backupTeamMemberList.size() == teamMemberList.size()) {
            return;
        }
        //找出要移除的
        List<TeamMember> leaves = new ArrayList<>();
        context.setLeave(leaves);

        for (int i = 0; i < backupTeamMemberList.size(); i++) {
            TeamMember teamMember = backupTeamMemberList.get(i);
            TeamMember tm = uidTeamMemberMap.get(teamMember.getUid());
            if (tm == null) {
                leaves.add(teamMember);
            }
        }
    }

    private void preAnalyze(PVPContext<TeamGame, TeamMember> pvpContext) {
        List<TeamMember> teamMemberList = pvpContext.getTeamMemberList();
        //转成Uid member Map
        Map<String, TeamMember> teamMemberMap = teamMemberList.stream()
                .collect(Collectors.groupingBy(TeamMember::getUid))
                .entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey, e -> e.getValue().get(0)));

        pvpContext.setUidTeamMemberMap(teamMemberMap);

        //找出队长
        UserIdentityEnum leaderEnum = UserIdentityEnum.LEADER;
        TeamMember leader = CollectionUtils.find(teamMemberList,
                it -> leaderEnum.equals(UserIdentityEnum.of(it.getUserIdentity()))).orElse(null);
        pvpContext.setLeader(leader);

        //找到自己
        UserSessionContext user = pvpContextHolder.getUser();
        pvpContext.setMe(teamMemberMap.get(user.getUid()));

    }

    @Override
    public int getOrder() {
        return PVPConstant.PVP_TEAM_MEMBER_OPERATION_AOP_ORDER;
    }
}

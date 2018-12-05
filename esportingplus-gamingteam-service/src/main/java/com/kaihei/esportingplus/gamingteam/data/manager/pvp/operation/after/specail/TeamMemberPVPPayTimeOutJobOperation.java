//package com.kaihei.esportingplus.gamingteam.data.manager.pvp.operation.after.specail;
//
//import com.alibaba.fastjson.JSONObject;
//import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
//import com.kaihei.esportingplus.common.schedule.job.JobScheduleService;
//import com.kaihei.esportingplus.gamingteam.config.GamingTeamConfig;
//import com.kaihei.esportingplus.gamingteam.data.manager.pvp.anno.OnCondition;
//import com.kaihei.esportingplus.gamingteam.data.manager.pvp.condition.TeamMemberLeaveCondition;
//import com.kaihei.esportingplus.gamingteam.data.manager.pvp.context.PVPContext;
//import com.kaihei.esportingplus.gamingteam.data.manager.pvp.context.PVPContextHolder;
//import com.kaihei.esportingplus.gamingteam.data.manager.pvp.job.PVPBossPayTimeOutJob;
//import com.kaihei.esportingplus.gamingteam.data.manager.pvp.model.TeamGame;
//import com.kaihei.esportingplus.gamingteam.data.manager.pvp.model.TeamMember;
//import com.kaihei.esportingplus.gamingteam.data.manager.pvp.operation.after.AfterRestoreOperation;
//import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
//import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVP;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//import org.apache.commons.lang3.time.DateUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 老板支付超时创建定时任务
// */
//@OnCondition(TeamMemberLeaveCondition.class)
//@Component
//public class TeamMemberPVPPayTimeOutJobOperation extends AfterRestoreOperation<TeamGamePVP> {
//
//    @Autowired
//    private PVPContextHolder<TeamGame, TeamMember> pvpContextHolder;
//
//    @Autowired
//    private GamingTeamConfig gamingTeamConfig;
//
//    @Autowired
//    private JobScheduleService jobScheduleService;
//
//    @Autowired
//    private PVPBossPayTimeOutJob pvpBossPayTimeOutJob;
//
//    @Override
//    public void doOperate() {
//        List<TeamMember> joinTeamMembers = pvpContextHolder.getJoinTeamMembers();
//        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
//
//        Team team = context.getTeam();
//        TeamGame teamGame = context.getTeamGame();
//
//        Optional.ofNullable(joinTeamMembers)
//                .filter(it -> !it.isEmpty())
//                .ifPresent(it ->
//                        it.stream()
//                                .filter(tm -> UserIdentityEnum.of(tm.getUserIdentity())
//                                        .equals(UserIdentityEnum.BOSS))
//                                .forEach(tm -> {
//                                    Date timeout = DateUtils
//                                            .addSeconds(new Date(),
//                                                    gamingTeamConfig.getPaymentTimeout());
//
//                                    //拼接参数
//                                    JSONObject params = new JSONObject()
//                                            .fluentPut("teamSequence", team.getSequence())
//                                            .fluentPut("gameId", teamGame.getGameId())
//                                            .fluentPut("uid", tm.getUid());
//
//                                    //添加定时任务
//                                    jobScheduleService
//                                            .addJob(pvpBossPayTimeOutJob, "PVPBossPayTimeOut"
//                                                    , timeout, params.toJSONString());
//                                })
//                );
//    }
//}

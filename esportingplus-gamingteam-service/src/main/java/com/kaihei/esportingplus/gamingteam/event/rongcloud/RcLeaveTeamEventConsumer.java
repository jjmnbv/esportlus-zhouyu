package com.kaihei.esportingplus.gamingteam.event.rongcloud;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.event.EventConsumer;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupCommonParams;
import com.kaihei.esportingplus.gamingteam.api.params.TeamImCmdMsgOutMemberParams;
import com.kaihei.esportingplus.gamingteam.api.params.UserNameImCmdMsgParams;
import com.kaihei.esportingplus.gamingteam.data.manager.ImService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 离开车队事件处理--退出车队、踢出车队
 * @author liangyi
 */
@Component
public class RcLeaveTeamEventConsumer extends EventConsumer {

    @Autowired
    ImService imService;

    private static final Logger logger = LoggerFactory.getLogger(RcLeaveTeamEventConsumer.class);

    /**
     * 退出融云群组
     * @param rcLeaveTeamEvent
     */
    @Subscribe
    @AllowConcurrentEvents
    public void quitRongCloudGroup(RcLeaveTeamEvent rcLeaveTeamEvent){
        Boolean kickOut = rcLeaveTeamEvent.getKickOut();
        String teamSequence = rcLeaveTeamEvent.getTeamSequence();
        TeamImCmdMsgOutMemberParams teamImCmdMsgOutMemberParams = new TeamImCmdMsgOutMemberParams();
        List<String> reciverList = new ArrayList<>();
        reciverList.add(teamSequence);
        teamImCmdMsgOutMemberParams.setReciever(reciverList);
        teamImCmdMsgOutMemberParams.setTeamSequence(teamSequence);
        if (kickOut) {
            List<String> toUsers = new ArrayList<>();
            toUsers.add(rcLeaveTeamEvent.getUid());
            // 被踢出
            teamImCmdMsgOutMemberParams.setToUsers(toUsers);
            teamImCmdMsgOutMemberParams.setOutReasonType(rcLeaveTeamEvent.getOutReason().getCode());
            ResponsePacket<Void> kickOutTeamResp = imService
                    .outTeam(teamImCmdMsgOutMemberParams);
            logger.info(">> 融云推送[踢出车队]通知, 车队序列号: {}, 其他队员 uid: {}, 融云返回结果: {}",
                    teamSequence, toUsers, kickOutTeamResp);
        }
        // 退出车队
        UserNameImCmdMsgParams userNameImCmdMsgParams = BeanMapper
                .map(teamImCmdMsgOutMemberParams, UserNameImCmdMsgParams.class);
        userNameImCmdMsgParams.setUserName(rcLeaveTeamEvent.getUsername());
        List<String> otherUserList = rcLeaveTeamEvent.getOtherUidList();
        if (ObjectTools.isNotEmpty(otherUserList)) {
            userNameImCmdMsgParams.setToUsers(otherUserList);
        }
        ResponsePacket<Void> quitTeamResp = imService.exitTeam(userNameImCmdMsgParams);
        logger.info(">> 融云推送[退出车队]通知, 车队序列号: {}, 其他队员 uid: {}, 融云返回结果: {}",
                teamSequence, otherUserList, quitTeamResp);
        // 离开融云群组
        ImGroupCommonParams imGroupCommonParams = new ImGroupCommonParams();
        imGroupCommonParams.setGroupName(rcLeaveTeamEvent.getTeamTitle());
        imGroupCommonParams.setGroupId(teamSequence);
        imGroupCommonParams.setUid(rcLeaveTeamEvent.getUid());
        ResponsePacket<Void> leaveGroupResp = imService.leaveGroup(imGroupCommonParams);
        logger.info(">> 融云推送[离开融云群组]通知, 车队序列号: {}, 融云返回结果: {}",
                teamSequence, leaveGroupResp);
    }
}

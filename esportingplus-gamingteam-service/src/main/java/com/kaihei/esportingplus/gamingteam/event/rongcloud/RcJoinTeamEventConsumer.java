package com.kaihei.esportingplus.gamingteam.event.rongcloud;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.event.EventConsumer;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupCommonParams;
import com.kaihei.esportingplus.gamingteam.api.params.UserNameImCmdMsgParams;
import com.kaihei.esportingplus.gamingteam.data.manager.ImService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 加入车队事件处理--创建车队、加入车队
 * @author liangyi
 */
@Component
public class RcJoinTeamEventConsumer extends EventConsumer {

    @Autowired
    ImService imService;

    private static final Logger logger = LoggerFactory.getLogger(RcJoinTeamEventConsumer.class);

    /**
     * 创建并加入融云群组
     * @param rcJoinTeamEvent
     */
    @Subscribe
    @AllowConcurrentEvents
    public void joinRongCloudGroup(RcJoinTeamEvent rcJoinTeamEvent){
        String teamSequence = rcJoinTeamEvent.getTeamSequence();
        String uid = rcJoinTeamEvent.getUid();
        ImGroupCommonParams imGroupCommonParams = new ImGroupCommonParams();
        imGroupCommonParams.setGroupName(rcJoinTeamEvent.getTeamTitle());
        imGroupCommonParams.setGroupId(teamSequence);
        imGroupCommonParams.setUid(uid);
        boolean isCreate = rcJoinTeamEvent.getCreateTeam();
        if (isCreate) {
            // 创建车队--创建融云群组
            ResponsePacket<Void> createGroupResp = imService.createGroup(imGroupCommonParams);
            logger.info(">> 融云推送[创建融云群组]通知, 车队序列号: {}, 队长 uid: {}, 融云返回结果: {}",
                    teamSequence, uid, createGroupResp);
        } else {
            // 加入融云群组
            ResponsePacket<Void> joinGroupResp = imService.joinGroup(imGroupCommonParams);
            logger.info(">> 融云推送[加入融云群组]通知, 车队序列号: {}, 队员 uid: {}, 融云返回结果: {}",
                    teamSequence, uid, joinGroupResp);
        }
        // 加入车队 -- 加入车队需要对车队其他的用户推送
        UserNameImCmdMsgParams userNameImCmdMsgParams = new UserNameImCmdMsgParams();
        userNameImCmdMsgParams.setUserName(rcJoinTeamEvent.getUsername());
        List<String> reciverList = new ArrayList<>();
        reciverList.add(teamSequence);
        userNameImCmdMsgParams.setReciever(reciverList);
        userNameImCmdMsgParams.setTeamSequence(teamSequence);
        List<String> otherUserList = rcJoinTeamEvent.getOtherUidList();
        if (ObjectTools.isNotEmpty(otherUserList)) {
            userNameImCmdMsgParams.setToUsers(otherUserList);
        }
        ResponsePacket<Void> joinTeamResp = imService.joinTeam(userNameImCmdMsgParams);
        logger.info(">> 融云推送[加入车队]通知, 车队序列号: {}, 其他队员 uid: {}, 融云返回结果: {}",
                teamSequence, otherUserList, joinTeamResp);
    }
}

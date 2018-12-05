package com.kaihei.esportingplus.gamingteam.event.rongcloud;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.event.EventConsumer;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupBaseParams;
import com.kaihei.esportingplus.gamingteam.api.params.TeamImCmdMsgBaseParams;
import com.kaihei.esportingplus.gamingteam.data.manager.ImService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 结束车队事件处理--解散车队、结束车队
 * @author liangyi
 */
@Component
public class RcEndTeamEventConsumer extends EventConsumer {

    @Autowired
    ImService imService;

    private static final Logger logger = LoggerFactory.getLogger(RcEndTeamEventConsumer.class);

    /**
     * 删除融云群组
     * @param rcEndTeamEvent
     */
    @Subscribe
    @AllowConcurrentEvents
    public void deleteRongCloudGroup(RcEndTeamEvent rcEndTeamEvent){
        Boolean dismissTeam = rcEndTeamEvent.getDismissTeam();
        String teamSequence = rcEndTeamEvent.getTeamSequence();
        TeamImCmdMsgBaseParams teamImCmdMsgBaseParams = new TeamImCmdMsgBaseParams();
        List<String> reciverList = new ArrayList<>();
        reciverList.add(teamSequence);
        teamImCmdMsgBaseParams.setReciever(reciverList);
        teamImCmdMsgBaseParams.setTeamSequence(teamSequence);
        if (dismissTeam) {
            // 解散车队--向其他队员推送解散信息
            List<String> otherUidList = rcEndTeamEvent.getOtherUidList();
            if (ObjectTools.isNotEmpty(otherUidList)) {
                teamImCmdMsgBaseParams.setToUsers(otherUidList);
                ResponsePacket<Void> dismissTeamResp = imService
                        .dismissTeam(teamImCmdMsgBaseParams);
                logger.info(">> 融云推送[解散车队]通知, 车队序列号: {}, 其他队员 uid: {},"
                                + " 融云返回结果: {}", teamSequence, otherUidList, dismissTeamResp);
            }
            // 车队中只有队长一个人不推送融云解散车队通知
        } else {
            // 结束车队
            ResponsePacket<Void> endTeamResp = imService
                    .endTeamServer(teamImCmdMsgBaseParams);
            logger.info(">> 融云推送[结束车队]通知, 车队序列号: {}, 融云返回结果: {}",
                    teamSequence, endTeamResp);
        }
        ImGroupBaseParams imGroupBaseParams = new ImGroupBaseParams();
        imGroupBaseParams.setUid(rcEndTeamEvent.getLeaderUid());
        imGroupBaseParams.setGroupId(teamSequence);
        // 销毁融云群组
        ResponsePacket<Void> dismissGroupResp = imService.dismissGroup(imGroupBaseParams);
        logger.info(">> 融云推送[销毁融云群组]通知, 车队序列号: {}, 融云返回结果: {}",
                teamSequence, dismissGroupResp);
    }
}

package com.kaihei.esportingplus.gamingteam.event.rongcloud;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.event.EventConsumer;
import com.kaihei.esportingplus.gamingteam.api.params.TeamImCmdMsgBaseParams;
import com.kaihei.esportingplus.gamingteam.data.manager.ImService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 确认入团事件处理
 * @author liangyi
 */
@Component
public class RcConfirmJoinTeamEventConsumer extends EventConsumer {

    @Autowired
    ImService imService;

    private static final Logger logger = LoggerFactory.getLogger(
            RcConfirmJoinTeamEventConsumer.class);

    /**
     * 融云推送确认入团通知
     * @param rcConfirmJoinTeamEvent
     */
    @Subscribe
    @AllowConcurrentEvents
    public void pushConfirmJoinTeam(RcConfirmJoinTeamEvent rcConfirmJoinTeamEvent){
        String teamSequence = rcConfirmJoinTeamEvent.getTeamSequence();
        TeamImCmdMsgBaseParams teamImCmdMsgBaseParams = new TeamImCmdMsgBaseParams();
        teamImCmdMsgBaseParams.setTeamSequence(teamSequence);
        List<String> reciverList = new ArrayList<>();
        reciverList.add(teamSequence);
        teamImCmdMsgBaseParams.setReciever(reciverList);
        ResponsePacket<Void> confirmJoinTeamResp = imService.confirmInTeam(teamImCmdMsgBaseParams);
        logger.info(">> 融云推送[确认入团]通知, 车队序列号: {}, 融云返回结果: {}",
                        teamSequence, confirmJoinTeamResp);
    }

}

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
 * 立即开车事件处理
 * @author liangyi
 */
@Component
public class RcStartTeamEventConsumer extends EventConsumer {

    @Autowired
    ImService imService;

    private static final Logger logger = LoggerFactory.getLogger(RcStartTeamEventConsumer.class);

    /**
     * 融云群组开车通知
     * @param RcStartTeamEvent
     */
    @Subscribe
    @AllowConcurrentEvents
    public void pushStartTeamMessage(RcStartTeamEvent RcStartTeamEvent){
        String teamSequence = RcStartTeamEvent.getTeamSequence();
        TeamImCmdMsgBaseParams teamImCmdMsgBaseParams = new TeamImCmdMsgBaseParams();
        List<String> reciverList = new ArrayList<>();
        reciverList.add(teamSequence);
        teamImCmdMsgBaseParams.setReciever(reciverList);
        teamImCmdMsgBaseParams.setTeamSequence(teamSequence);
        ResponsePacket<Void> startTeamResp = imService.startTeam(teamImCmdMsgBaseParams);
        logger.info(">> 融云推送[立即开车]通知, 车队序列号: {}, 融云返回结果: {}",
                teamSequence, startTeamResp);
    }
}

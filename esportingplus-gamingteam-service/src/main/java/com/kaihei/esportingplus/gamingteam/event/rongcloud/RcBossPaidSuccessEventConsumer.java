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
 * 老板支付成功事件处理
 * @author liangyi
 */
@Component
public class RcBossPaidSuccessEventConsumer extends EventConsumer {

    @Autowired
    ImService imService;

    private static final Logger logger = LoggerFactory.getLogger(
            RcBossPaidSuccessEventConsumer.class);

    /**
     * 向融云推送老板支付结果通知
     * @param rcBossPaidSuccessEvent
     */
    @Subscribe
    @AllowConcurrentEvents
    public void pushBossPaidSuccess(RcBossPaidSuccessEvent rcBossPaidSuccessEvent){
        String teamSequence = rcBossPaidSuccessEvent.getTeamSequence();
        TeamImCmdMsgBaseParams teamImCmdMsgBaseParams = new TeamImCmdMsgBaseParams();
        teamImCmdMsgBaseParams.setTeamSequence(teamSequence);
        List<String> reciverList = new ArrayList<>();
        reciverList.add(teamSequence);
        teamImCmdMsgBaseParams.setReciever(reciverList);
        ResponsePacket<Void> bossPaidFinishResp = imService.paidFinish(teamImCmdMsgBaseParams);
        logger.info(">> 融云推送[老板支付成功]通知, 车队序列号: {}, 融云返回结果: {}",
                teamSequence, bossPaidFinishResp);
    }

}

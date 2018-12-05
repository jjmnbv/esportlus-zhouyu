package com.kaihei.esportingplus.marketing.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.marketing.api.event.TeamFreeEvent;
import com.kaihei.esportingplus.marketing.api.event.TeamFreeObtainstarEvent;
import com.kaihei.esportingplus.marketing.api.event.UserEventHandler;
import com.kaihei.esportingplus.marketing.api.feign.UserTaskFreeTeamServiceClient;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO 功能描述
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/11 11:12
 */
@RestController
@RequestMapping("/usertask/freeteam")
public class UserTaskFreeTeamController implements UserTaskFreeTeamServiceClient {

    @Resource(name = "teamFreeFinishEventHandler")
    UserEventHandler teamFreeFinishEventHandler;

//    @Resource(name = "teamFreeObtainstarEventHandler")
//    UserEventHandler teamFreeObtainstarEventHandler;

    /**
     * 免费车队结束车队
     */
    @Override
    public ResponsePacket<Void> finish(@RequestBody TeamFreeEvent params) {
        teamFreeFinishEventHandler.handle(params);
        return ResponsePacket.onSuccess();
    }

    /**
     * 免费车队获得评分
     */
    @Override
    public ResponsePacket<Void> obtainstar(@RequestBody TeamFreeObtainstarEvent params) {
//        teamFreeObtainstarEventHandler.handle(params);
        return ResponsePacket.onError();
    }
}

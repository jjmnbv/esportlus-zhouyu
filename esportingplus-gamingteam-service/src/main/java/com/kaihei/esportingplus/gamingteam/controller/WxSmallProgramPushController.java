package com.kaihei.esportingplus.gamingteam.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.gamingteam.api.feign.WxSmallProgramPushClient;
import com.kaihei.esportingplus.gamingteam.api.params.WxTeamEndParams;
import com.kaihei.esportingplus.gamingteam.api.params.WxTeamOrderCancelParams;
import com.kaihei.esportingplus.gamingteam.api.params.WxTeamStartParams;
import com.kaihei.esportingplus.gamingteam.data.manager.WxSmallProgramPushService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/program/push")
@Api(tags = {"微信小程序推送接口"})
public class WxSmallProgramPushController implements WxSmallProgramPushClient {

    @Autowired
    private WxSmallProgramPushService wxSmallProgramPushService;

    @ApiOperation(value = "车队订单结束通知")
    @Override
    public ResponsePacket<Void> pushOrderEnd(@RequestBody WxTeamEndParams pushParams) {
        return  wxSmallProgramPushService.pushOrderEnd(pushParams);
    }

    @ApiOperation(value = "车队订单取消通知")
    @Override
    public ResponsePacket<Void> pushOrderCancel(@RequestBody WxTeamOrderCancelParams cancelParams) {
        return wxSmallProgramPushService.pushOrderCancel(cancelParams);
    }

    @ApiOperation(value = "车队开车通知")
    @Override
    public ResponsePacket<Void> pushTeamStart(WxTeamStartParams startParams) {
        return wxSmallProgramPushService.pushTeamStart(startParams);
    }
}

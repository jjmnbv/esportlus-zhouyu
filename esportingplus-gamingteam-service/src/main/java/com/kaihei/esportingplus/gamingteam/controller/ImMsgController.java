package com.kaihei.esportingplus.gamingteam.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.gamingteam.api.feign.ImMsgServiceClient;
import com.kaihei.esportingplus.gamingteam.api.params.ImDismissGroupParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImEndTeamMsgParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImFullMsgParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupCommonParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupJoinParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupLeavelParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImMatchParams;
import com.kaihei.esportingplus.gamingteam.data.manager.ImMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/immsg")
@Api(tags = {"命令模式消息"})
public class ImMsgController implements ImMsgServiceClient {
    @Autowired
    private ImMsgService imMsgService;
    @ApiOperation(value = "创建群组")
    @Override
    public ResponsePacket<Boolean> createGroup(@RequestBody ImGroupCommonParams params) {
        imMsgService.createGroup(params);
        return ResponsePacket.onSuccess(true);
    }
    @ApiOperation(value = "加入群组")
    @Override
    public ResponsePacket<Boolean> joinGroup(@RequestBody ImGroupJoinParams params) {
        imMsgService.joinGroup(params);
        return ResponsePacket.onSuccess(true);
    }
    @ApiOperation(value = "离开群组")
    @Override
    public ResponsePacket<Void> leaveGroup(@RequestBody ImGroupLeavelParams params) {
        imMsgService.leaveGroup(params);
        return ResponsePacket.onSuccess();
    }
    @ApiOperation(value = "发送车队满员消息通知")
    @Override
    public ResponsePacket<Void> sendFullMembersMsg(@RequestBody ImFullMsgParams params) {
        imMsgService.sendFullMembersMsg(params);
        return ResponsePacket.onSuccess();
    }
    @ApiOperation(value = "结束车队")
    @Override
    public ResponsePacket<Void> endTeam(@RequestBody ImEndTeamMsgParams params) {
        imMsgService.endTeam(params);
        return ResponsePacket.onSuccess();
    }
    @ApiOperation(value = "解散群组")
    @Override
    public ResponsePacket<Void> dismissGroup(@RequestBody ImDismissGroupParams params) {
        imMsgService.dismissGroup(params);
        return ResponsePacket.onSuccess();
    }
    @ApiOperation(value = "匹配失败消息")
    @Override
    public ResponsePacket<Void> sendMatchMsg(@RequestBody ImMatchParams params) {
        imMsgService.sendMatchMsg(params);
        return ResponsePacket.onSuccess();
    }
}

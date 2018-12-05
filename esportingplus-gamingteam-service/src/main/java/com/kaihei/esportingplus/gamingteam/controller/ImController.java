package com.kaihei.esportingplus.gamingteam.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.gamingteam.api.feign.ImServiceClient;
import com.kaihei.esportingplus.gamingteam.api.params.TeamImCmdMsgBaseParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupBaseParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupCommonParams;
import com.kaihei.esportingplus.gamingteam.api.params.TeamImCmdMsgOutMemberParams;
import com.kaihei.esportingplus.gamingteam.api.params.UserNameImCmdMsgParams;
import com.kaihei.esportingplus.gamingteam.data.manager.ImService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/im")
@Api(tags = {"消息通知"})
public class ImController implements ImServiceClient{
    @Autowired
    private ImService imService;
    @ApiOperation(value = "创建群组")
    @Override
    public ResponsePacket<Void> createGroup( @ApiParam(value = "创建群组参数", required = true)@RequestBody ImGroupCommonParams params) {
        return imService.createGroup(params);
    }
    @ApiOperation(value = "加入群组")
    @Override
    public ResponsePacket<Void> joinGroup(@ApiParam(value = "加入群组参数", required = true)@RequestBody ImGroupCommonParams params) {
        return imService.joinGroup(params);
    }
    @ApiOperation(value = "离开群组")
    @Override
    public ResponsePacket<Void> leaveGroup(@ApiParam(value = "离开群组参数", required = true) @RequestBody ImGroupBaseParams params) {
        return imService.leaveGroup(params);
    }
    @ApiOperation(value = "解散群组")
    @Override
    public ResponsePacket<Void> dismissGroup(@ApiParam(value = "解散群组参数", required = true) @RequestBody ImGroupBaseParams params) {
        return imService.dismissGroup(params);
    }

    @ApiOperation(value = "车队队长开车通知")
    @Override
    public ResponsePacket<Void> startTeam(@ApiParam(value = "车队队长开车通知参数", required = true) @RequestBody TeamImCmdMsgBaseParams params) {
        return imService.startTeam(params);
    }
    @ApiOperation(value = "车队队长踢人通知")
    @Override
    public ResponsePacket<Void> outTeam(@RequestBody TeamImCmdMsgOutMemberParams params) {
        return imService.outTeam(params);
    }
    @ApiOperation(value = "车队解散通知")
    @Override
    public ResponsePacket<Void> dismissTeam(@RequestBody TeamImCmdMsgBaseParams params) {
        return imService.dismissTeam(params);
    }
    @ApiOperation(value = "车队位置修改通知")
    @Override
    public ResponsePacket<Void> changeTeamCount(@RequestBody TeamImCmdMsgBaseParams params) {
        return imService.changeTeamCount(params);
    }
    @ApiOperation(value = "车队服务结束通知")
    @Override
    public ResponsePacket<Void> endTeamServer(@RequestBody TeamImCmdMsgBaseParams params) {
        return imService.endTeamServer(params);
    }
    @ApiOperation(value = "车队队员完成支付通知")
    @Override
    public ResponsePacket<Void> paidFinish(@RequestBody TeamImCmdMsgBaseParams params) {
        return imService.paidFinish(params);
    }
    @ApiOperation(value = "车队队员确认入团通知")
    @Override
    public ResponsePacket<Void> confirmInTeam(@RequestBody TeamImCmdMsgBaseParams params) {
        return imService.confirmInTeam(params);
    }
    @ApiOperation(value = "车队队员加入通知")
    @Override
    public ResponsePacket<Void> joinTeam(@RequestBody UserNameImCmdMsgParams params) {
        return imService.joinTeam(params);
    }
    @ApiOperation(value = "车队队员退出通知")
    @Override
    public ResponsePacket<Void> exitTeam(@RequestBody UserNameImCmdMsgParams params) {
        return imService.exitTeam(params);
    }


}

package com.kaihei.esportingplus.gamingteam.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.api.params.UpdatePositionCountParams;
import com.kaihei.esportingplus.gamingteam.api.params.pvp.PVPTeamCreateParams;
import com.kaihei.esportingplus.gamingteam.api.params.pvp.PVPTeamEndParams;
import com.kaihei.esportingplus.gamingteam.api.params.pvp.PVPTeamFreeJoinParams;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamSequenceVO;
import com.kaihei.esportingplus.gamingteam.domain.service.PVPTeamServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <一句话功能描述>
 *
 * @Author LiuQing.Qin
 * @Date 2018/11/6 11:01:33
 */
@RestController
@RequestMapping("/gamingteam/pvp")
@Api("付费车队")
@Slf4j
public class PVPGamingTeamController {


    @Autowired
    private PVPTeamServiceImpl pvpTeamService;

    @ApiOperation("创建车队")
    @PostMapping
    public ResponsePacket<TeamSequenceVO> createTeam(@Valid
    @RequestBody PVPTeamCreateParams pvpTeamCreateParams) {
        return ResponsePacket.onSuccess(pvpTeamService.createTeam(pvpTeamCreateParams));
    }

    @ApiOperation("加入车队")
    public ResponsePacket<?> joinGamingTeam(@RequestHeader(name = "Authorization") String token,
            @RequestBody PVPTeamFreeJoinParams pvpTeamFreeJoinParams) {
        pvpTeamService.joinTeam(pvpTeamFreeJoinParams);
        return ResponsePacket.onSuccess();
    }


    @ApiOperation(value = "退出车队")
    @DeleteMapping("/quit/{sequence}")
    public ResponsePacket<?> quitGamingTeam(@RequestHeader(name = "Authorization") String token,
            @PathVariable("sequence") String sequence) {
        pvpTeamService.quitTeam(sequence);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "踢出队员")
    @DeleteMapping("/kickOut/{sequence}/{uid}")
    public ResponsePacket<?> kickOutMember(@RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队序列号", required = true) @PathVariable("sequence") String sequence,
            @ApiParam(value = "被踢出队员的uid", required = true) @PathVariable("uid") String uid) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, sequence, uid);
        pvpTeamService.kickOutTeamMember(sequence, uid);
        return ResponsePacket.onSuccess();
    }

    /**
     * 立即开车
     *
     * @param token auth 认证的token
     * @param sequence 车队序列号
     */
    @ApiOperation(value = "立即开车")
    @GetMapping("/startTeam/{sequence}")
    public ResponsePacket<?> startGamingTeam(@RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队序列号", required = true) @PathVariable("sequence") String sequence) {
        pvpTeamService.startTeam(sequence);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "更新车队位置数")
    public ResponsePacket<Void> updateTeamPositioncount(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "更新车队未知数参数", required = true) @RequestBody UpdatePositionCountParams positionCountParams) {
        pvpTeamService.updateTeamPositioncount(UserSessionContext.getUser().getUid(),
                positionCountParams);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "确认准备")
    @GetMapping("/confirmReady/{sequence}")
    public ResponsePacket<Void> confirmReadyGamingTeam(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队序列号", required = true)
            @PathVariable("sequence") String sequence) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, sequence);
        pvpTeamService.confirmReadyGamingTeam(sequence);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "取消准备")
    @GetMapping("/cancelReady/{sequence}")
    public ResponsePacket<Void> cancelReadyGamingTeam(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队序列号", required = true)
            @PathVariable("sequence") String sequence) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, sequence);
        pvpTeamService.cancelReadyGamingTeam(sequence);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "结束车队")
    @PostMapping("endTeam")
    public ResponsePacket<Void> endGamingTeam(@RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队查询列表请求参数", required = true) @RequestBody PVPTeamEndParams pvpTeamEndParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, pvpTeamEndParams);
        pvpTeamService.endTeam(pvpTeamEndParams);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "解散车队")
    @DeleteMapping("/dismiss/team/{teamSequence}")
    public ResponsePacket<Void> dismissGamingTeam(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("teamSequence") String teamSequence) {
        pvpTeamService.dismissTeam(teamSequence);
        return ResponsePacket.onSuccess();
    }
}

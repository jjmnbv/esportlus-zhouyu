package com.kaihei.esportingplus.gamingteam.controller;

import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.api.feign.PVPFreeTeamServiceClient;
import com.kaihei.esportingplus.gamingteam.api.params.PVPFreeTeamsForBackupParams;
import com.kaihei.esportingplus.gamingteam.api.params.UpdatePositionCountParams;
import com.kaihei.esportingplus.gamingteam.api.params.pvp.PVPFreeTeamMatchingParams;
import com.kaihei.esportingplus.gamingteam.api.params.pvp.PVPTeamEndParams;
import com.kaihei.esportingplus.gamingteam.api.params.pvp.PVPTeamFreeCreateParams;
import com.kaihei.esportingplus.gamingteam.api.params.pvp.PVPTeamFreeJoinParams;
import com.kaihei.esportingplus.gamingteam.api.vo.PVPFreeTeamBackupVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamGameResultVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamSequenceVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeMemberInTeamVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamBaseVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamCurrentInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamCurrentMatchingInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamMemberInfoVO;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.PVPFreeTeamBossMatchingVO;
import com.kaihei.esportingplus.gamingteam.domain.service.PVPTeamFreeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * pvp 免费车队
 *
 * @author liangyi
 */
@RestController
@RequestMapping("/gamingteam/pvp/free")
@Api("pvp 免费车队")
@Slf4j
public class PVPFreeGamingTeamController implements PVPFreeTeamServiceClient {

    @Autowired
    @Lazy
    private PVPTeamFreeService pvpTeamFreeService;

    @ApiOperation("创建车队")
    @Override
    public ResponsePacket<TeamSequenceVO> createTeam(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody PVPTeamFreeCreateParams pvpTeamFreeCreateParams) {
        return ResponsePacket.onSuccess(pvpTeamFreeService.createTeam(pvpTeamFreeCreateParams));
    }


    @ApiOperation("获取车队基本信息")
    @Override
    public ResponsePacket<PVPFreeTeamBaseVO> getTeamBaseInfo(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队序列号", required = true)
            @PathVariable("sequence") String sequence) {

        PVPFreeTeamBaseVO teamBaseInfo = pvpTeamFreeService.getTeamBaseInfo(sequence);
        return ResponsePacket.onSuccess(teamBaseInfo);
    }

    @ApiOperation("获取车队实时数据信息")
    @Override
    public ResponsePacket<PVPFreeTeamCurrentInfoVO> getTeamCurrentInfo(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队序列号", required = true)
            @PathVariable("sequence") String sequence) {

        PVPFreeTeamCurrentInfoVO teamCurrentInfo = pvpTeamFreeService.getTeamCurrentInfo(sequence);
        return ResponsePacket.onSuccess(teamCurrentInfo);
    }

    @Override
    @ApiOperation("加入车队")
    public ResponsePacket<?> joinGamingTeam(@RequestHeader(name = "Authorization") String token,
            @RequestBody PVPTeamFreeJoinParams pvpTeamFreeJoinParams) {
        pvpTeamFreeService.joinTeam(pvpTeamFreeJoinParams);
        return ResponsePacket.onSuccess();
    }


    @Override
    @ApiOperation(value = "退出车队")
    @DeleteMapping("/quit/{sequence}")
    public ResponsePacket<?> quitGamingTeam(@RequestHeader(name = "Authorization") String token,
            @PathVariable("sequence") String sequence) {
        pvpTeamFreeService.quitTeam(sequence);
        return ResponsePacket.onSuccess();
    }

    @Override
    @ApiOperation(value = "踢出队员")
    @DeleteMapping("/kickOut/{sequence}/{uid}")
    public ResponsePacket<?> kickOutMember(@RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队序列号", required = true) @PathVariable("sequence") String sequence,
            @ApiParam(value = "被踢出队员的uid", required = true) @PathVariable("uid") String uid) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, sequence, uid);
        pvpTeamFreeService.kickOutTeamMember(sequence, uid);
        return ResponsePacket.onSuccess();
    }

    /**
     * 立即开车
     *
     * @param token auth 认证的token
     * @param sequence 车队序列号
     */
    @Override
    @ApiOperation(value = "立即开车")
    @GetMapping("/startTeam/{sequence}")
    public ResponsePacket<?> startGamingTeam(@RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队序列号", required = true) @PathVariable("sequence") String sequence) {
        pvpTeamFreeService.startTeam(sequence);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "更新车队位置数")
    @Override
    public ResponsePacket<Void> updateTeamPositioncount(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "更新车队未知数参数", required = true) @RequestBody UpdatePositionCountParams positionCountParams) {
        pvpTeamFreeService.updateTeamPositioncount(UserSessionContext.getUser().getUid(),
                positionCountParams);
        return ResponsePacket.onSuccess();
    }

    @Override
    @ApiOperation(value = "确认准备")
    @GetMapping("/confirmReady/{sequence}")
    public ResponsePacket<Void> confirmReadyGamingTeam(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队序列号", required = true)
            @PathVariable("sequence") String sequence) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, sequence);
        pvpTeamFreeService.confirmReadyGamingTeam(sequence);
        return ResponsePacket.onSuccess();
    }

    @Override
    @ApiOperation(value = "取消准备")
    @GetMapping("/cancelReady/{sequence}")
    public ResponsePacket<Void> cancelReadyGamingTeam(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队序列号", required = true)
            @PathVariable("sequence") String sequence) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, sequence);
        pvpTeamFreeService.cancelReadyGamingTeam(sequence);
        return ResponsePacket.onSuccess();
    }

    @Override
    @ApiOperation(value = "获取当前用户的匹配信息")
    public ResponsePacket<PVPFreeTeamCurrentMatchingInfoVO> getCurrentMatchingInfo(
            @RequestHeader(name = "Authorization") String token) {
        PVPFreeTeamCurrentMatchingInfoVO matchingInfoVO = pvpTeamFreeService.getCurrentMatchingInfo();
        return ResponsePacket.onSuccess(matchingInfoVO);
    }


    @ApiOperation(value = "后管系统获取免费车队列表")
    @Override
    public ResponsePacket<PagingResponse<PVPFreeTeamBackupVO>> getPvpFreeTeamBackup(
            @RequestBody PVPFreeTeamsForBackupParams params) {
        return ResponsePacket.onSuccess(pvpTeamFreeService.getTeamInfosForBackup(params));
    }

    @Override
    @ApiOperation(value = "获取用户在某个车队中的信息")
    public ResponsePacket<PVPFreeMemberInTeamVO> getMemberInTeam(
            @RequestHeader(name = "Authorization") String token) {
        PVPFreeMemberInTeamVO memberInTeam = pvpTeamFreeService.getMemberInTeam();
        return ResponsePacket.onSuccess(memberInTeam);
    }

    @ApiOperation(value = "结束车队")
    @PostMapping("endTeam")
    @Override
    public ResponsePacket<Void> endGamingTeam(@RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队查询列表请求参数", required = true) @RequestBody PVPTeamEndParams pvpTeamEndParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, pvpTeamEndParams);
        pvpTeamFreeService.endTeam(pvpTeamEndParams);
        return ResponsePacket.onSuccess();
    }

    @Override
    @ApiOperation(value = "解散车队")
    @DeleteMapping("/dismiss/team/{teamSequence}")
    public ResponsePacket<Void> dismissGamingTeam(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("teamSequence") String teamSequence) {
        pvpTeamFreeService.dismissTeam(teamSequence);
        return ResponsePacket.onSuccess();
    }

    @Override
    @ApiOperation(value = "获取车队比赛结果")
    public ResponsePacket<List<TeamGameResultVO>> getTeamGameResult(
            @RequestHeader(name = "Authorization", required = false) String token,
            @PathVariable("teamSequence") String teamSequence) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, teamSequence);
        List<TeamGameResultVO> teamGameResult = pvpTeamFreeService.getTeamGameResult(teamSequence);
        return ResponsePacket.onSuccess(teamGameResult);
    }

    @Override
    @ApiOperation(value = "获取车队及队员信息")
    public ResponsePacket<PVPFreeTeamMemberInfoVO> getTeamMemberInfo(
            @RequestHeader(name = "Authorization", required = false) String token,
            @PathVariable("teamSequence") String teamSequence) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, teamSequence);
        PVPFreeTeamMemberInfoVO teamMemberInfo = pvpTeamFreeService.getTeamMemberInfo(teamSequence);
        return ResponsePacket.onSuccess(teamMemberInfo);
    }

    @ApiOperation(value = "开始匹配")
    @PostMapping("/matching")
    public ResponsePacket matching(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody PVPFreeTeamMatchingParams matchingParams) {
        System.out.println("matchingParams = " + matchingParams);
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, matchingParams);
        // 当前用户
        UserSessionContext user = UserSessionContext.getUser();
        String uid = user.getUid();
        PVPFreeTeamBossMatchingVO matchingVO = BeanMapper
                .map(matchingParams, PVPFreeTeamBossMatchingVO.class);
        matchingVO.setUid(uid);
        matchingVO.setAvatar(user.getAvatar());
        matchingVO.setUsername(user.getUsername());
        matchingVO.setChickenId(user.getChickenId());

        String teamSequence = pvpTeamFreeService.matchingTeam(matchingVO);

        Map<String,String> data = new HashMap<>();
        data.put("teamSequence",teamSequence);
        return ResponsePacket.onSuccess(data);
    }

    @ApiOperation(value = "取消匹配")
    @DeleteMapping("/cancenlMatching")
    public ResponsePacket cancelMatching(
            @RequestHeader(name = "Authorization") String token) {
        pvpTeamFreeService.cancelMatching();
        return ResponsePacket.onSuccess();
    }

}

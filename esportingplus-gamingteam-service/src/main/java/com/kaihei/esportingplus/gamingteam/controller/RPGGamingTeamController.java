package com.kaihei.esportingplus.gamingteam.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.tools.UserSessionContextUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.api.feign.RPGTeamServiceClient;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.BossConfirmPaidSuccessParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.RPGTeamCreateParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.RPGTeamJoinParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.RPGTeamEndParams;
import com.kaihei.esportingplus.gamingteam.api.params.TeamInfoBatchParams;
import com.kaihei.esportingplus.gamingteam.api.params.TeamQueryParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.RPGTeamRolesParams;
import com.kaihei.esportingplus.gamingteam.api.params.UpdatePositionCountParams;
import com.kaihei.esportingplus.gamingteam.api.vo.BossInfoForOrderVO;
import com.kaihei.esportingplus.gamingteam.api.vo.GameTeamTotal;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGMemberInTeamVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGRedisTeamBaseVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamStartOrderVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamCurrentInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamGameResultVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamListVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamMemberCompaintAdminVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamSequenceUidVO;
import com.kaihei.esportingplus.gamingteam.data.repository.TeamMemberRPGRepository;
import com.kaihei.esportingplus.gamingteam.data.repository.TeamRepository;
import com.kaihei.esportingplus.gamingteam.domain.service.RPGTeamService;
import com.kaihei.esportingplus.user.api.feign.UserServiceClient;
import com.kaihei.esportingplus.user.api.params.UpdateAvatarParams;
import com.kaihei.esportingplus.user.api.vo.QrCodeVo;
import com.kaihei.esportingplus.user.api.vo.UserGameAboardVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * RPG 车队
 *
 * @author liangyi
 */
@RestController
@RequestMapping("/gamingteam/rpg")
@Api(tags = {"RPG车队服务接口"})
public class RPGGamingTeamController implements RPGTeamServiceClient {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    TeamMemberRPGRepository teamMemberRPGRepository;
    @Autowired
    UserServiceClient userServiceClient;
    @Autowired
    RPGTeamService rpgTeamService;

    @ApiOperation(value = "创建车队")
    @Override
    public ResponsePacket<String> createGamingTeam(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody RPGTeamCreateParams RPGTeamCreateParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, RPGTeamCreateParams);
        String teamSequence = rpgTeamService.createTeam(RPGTeamCreateParams);
        return ResponsePacket.onSuccess(teamSequence);
    }

    @ApiOperation(value = "根据身份获取角色列表, 准备加入车队")
    @Override
    public ResponsePacket<List<UserGameAboardVo>> getGamingTeamRoles(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody RPGTeamRolesParams RPGTeamRolesParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, RPGTeamRolesParams);
        List<UserGameAboardVo> userGameAboardVos = rpgTeamService.getTeamRoles(RPGTeamRolesParams);
        return ResponsePacket.onSuccess(userGameAboardVos);
    }

    @ApiOperation(value = "加入车队")
    @Override
    public ResponsePacket<Void> joinGamingTeam(@RequestHeader(name = "Authorization") String token,
            @RequestBody RPGTeamJoinParams teamJoinParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, teamJoinParams);
        rpgTeamService.joinTeam(teamJoinParams);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "退出车队")
    @Override
    public ResponsePacket<Void> quitGamingTeam(@RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队序列号", required = true) @PathVariable("sequence") String sequence) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, sequence);
        rpgTeamService.quitTeam(sequence);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "踢出队员")
    @Override
    public ResponsePacket<Void> kickOutMember(@RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队序列号", required = true) @PathVariable("sequence") String sequence,
            @ApiParam(value = "被踢出队员的uid", required = true) @PathVariable("uid") String uid) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, sequence, uid);
        rpgTeamService.kickOutTeamMember(sequence, uid);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "解散车队")
    @Override
    public ResponsePacket<Void> dismissGamingTeam(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队查询列表请求参数", required = true) @RequestBody RPGTeamEndParams RPGTeamEndParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, RPGTeamEndParams);
        rpgTeamService.dismissTeam(RPGTeamEndParams);
        return ResponsePacket.onSuccess();
    }


    @ApiOperation(value = "确认入团")
    @Override
    public ResponsePacket<Void> confirmJoinGamingTeam(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队序列号", required = true)
            @PathVariable("sequence") String sequence) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, sequence);
        rpgTeamService.confirmJoinTeam(sequence);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "立即开车")
    @Override
    public ResponsePacket<Void> startGamingTeam(@RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队序列号", required = true) @PathVariable("sequence") String sequence) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, sequence);
        rpgTeamService.startTeam(sequence);
        return ResponsePacket.onSuccess();
    }


    @ApiOperation(value = "结束车队")
    @Override
    public ResponsePacket<Void> endGamingTeam(@RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队查询列表请求参数", required = true) @RequestBody RPGTeamEndParams RPGTeamEndParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, RPGTeamEndParams);
        rpgTeamService.endTeam(RPGTeamEndParams);
        return ResponsePacket.onSuccess();
    }


    @ApiOperation(value = "获取车队列表")
    @Override
    public ResponsePacket<PagingResponse<RPGTeamListVO>> getTeamList(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode,
            @ApiParam(value = "车队查询列表请求参数", required = true) @RequestBody(required = true) TeamQueryParams teamQueryParams) {
        return ResponsePacket.onSuccess(rpgTeamService.findTeamList(gameCode, teamQueryParams));
    }

    @ApiOperation(value = "获取车队列表总数")
    @Override
    public ResponsePacket<GameTeamTotal> getTeamTotal(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("game_code") Integer gameCode,
            @RequestBody(required = false) TeamQueryParams teamQueryParams) {
        return ResponsePacket.onSuccess(rpgTeamService.findTeamTotal(gameCode, teamQueryParams));
    }


    @ApiOperation(value = "获取小程序分享链接")
    @Override
    public ResponsePacket<QrCodeVo> getTeamQrCode(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "游戏代码", required = true) @PathVariable("game_code") Integer gameCode,
            @ApiParam(value = "车队序列号", required = true) @PathVariable("sequence") String sequence) {
        UserSessionContext user = UserSessionContext.getUser();
        UpdateAvatarParams updateAvatarParams = new UpdateAvatarParams();
        updateAvatarParams.setUid(user.getUid());
        updateAvatarParams.setAvatar(user.getAvatar());
        //转换头像链接
        ResponsePacket<String> responsePacket = userServiceClient
                .changeAndUpdateAvatar(updateAvatarParams);
        if(!responsePacket.responseSuccess()){
            throw new BusinessException(responsePacket.getCode(),responsePacket.getMsg());
        }
        //生成车队二维码
        String qrCodeLink = rpgTeamService.getSmallProgramTeamQrCode(gameCode, sequence,user.getUid());
        if (ObjectTools.isNotEmpty(responsePacket.getData()) && !responsePacket.getData()
                .equals(user.getAvatar())) {
            UserSessionContextUtils.updateUserAvatar(token, responsePacket.getData());
        }
        return ResponsePacket.onSuccess(new QrCodeVo(responsePacket.getData()==null?"":responsePacket.getData(), qrCodeLink));
    }

    @ApiOperation(value = "更新车队位置数")
    @Override
    public ResponsePacket<Void> updateTeamPositioncount(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "更新车队未知数参数", required = true) @RequestBody UpdatePositionCountParams positionCountParams) {
        rpgTeamService.updateTeamPositioncount(UserSessionContext.getUser().getUid(),
                positionCountParams);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "获取车队基本信息")
    @Override
    public ResponsePacket<RPGRedisTeamBaseVO> getTeamBaseInfo(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队序列号", required = true)
            @PathVariable("sequence") String sequence) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, sequence);
        RPGRedisTeamBaseVO RPGRedisTeamBaseVO = rpgTeamService.getTeamBaseInfo(sequence);
        return ResponsePacket.onSuccess(RPGRedisTeamBaseVO);
    }

    @ApiOperation(value = "当前队员获取车队实时数据")
    @Override
    public ResponsePacket<RPGTeamCurrentInfoVO> getTeamCurrentInfo(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队序列号", required = true) @PathVariable("sequence")
                    String sequence) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, sequence);
        RPGTeamCurrentInfoVO memberInfo = rpgTeamService.getTeamCurrentInfo(sequence);
        return ResponsePacket.onSuccess(memberInfo);
    }

    @ApiOperation(value = "获取老板创建订单的详细信息")
    @Override
    public ResponsePacket<BossInfoForOrderVO> getBossInfoForOrder(
            @ApiParam(value = "车队序列号", required = true) @PathVariable("sequence") String sequence,
            @ApiParam(value = "老板uid", required = true) @PathVariable("uid") String uid) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, sequence, uid);
        BossInfoForOrderVO bossInfoForOrder = rpgTeamService.getBossInfoForOrder(sequence, uid);
        return ResponsePacket.onSuccess(bossInfoForOrder);
    }

    @ApiOperation(value = "获取老板创建订单的详细信息")
    @Override
    public ResponsePacket<RPGTeamStartOrderVO> getBaojiInfoForOrder(
            @ApiParam(value = "车队序列号", required = true) @PathVariable("sequence") String sequence) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, sequence);
        RPGTeamStartOrderVO baojiInfoForOrder = rpgTeamService.getBaojiInfoForOrder(sequence);
        return ResponsePacket.onSuccess(baojiInfoForOrder);
    }

    @ApiOperation(value = "获取车队队员加入的车队信息")
    @Override
    public ResponsePacket<RPGMemberInTeamVO> getMemberInTeam(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "游戏code", required = true) @PathVariable("game_code") Integer gameCode) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, gameCode);
        RPGMemberInTeamVO memberInTeamVO = rpgTeamService.getMemberInTeam(gameCode);
        return ResponsePacket.onSuccess(memberInTeamVO);
    }

    @ApiOperation(value = "获取老板支付倒计时")
    @Override
    public ResponsePacket<Integer> getBossPayCountdown(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队序列号", required = true) @PathVariable("sequence")
                    String sequence) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, sequence);
        Integer payCountdown = rpgTeamService.getBossPayCountdown(sequence);
        return ResponsePacket.onSuccess(payCountdown);
    }

    @ApiOperation(value = "获取车队游戏结果")
    @Override
    public ResponsePacket<TeamGameResultVO> getGamingTeamGameResult(
            @ApiParam(value = "车队序列号", required = true)
            @PathVariable("sequence") String sequence) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, sequence);
        TeamGameResultVO teamGameResultVO = rpgTeamService.getTeamGameResult(sequence);
        return ResponsePacket.onSuccess(teamGameResultVO);
    }

    @Override
    public ResponsePacket<List<TeamMemberCompaintAdminVO>> getGamingTeamMemberBriefInfo(
            @ApiParam(value = "车队序列号", required = true) @PathVariable("sequence")
                    String sequence) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, sequence);
        List<TeamMemberCompaintAdminVO> teamMemberBriefInfo = rpgTeamService
                .getTeamMemberBriefInfo(sequence);
        return ResponsePacket.onSuccess(teamMemberBriefInfo);
    }

    @Override
    public ResponsePacket<List<TeamInfoVO>> getBatchGamingTeamInfo(
            @ApiParam(value = "车队序列号", required = true)
                    @RequestBody TeamInfoBatchParams teamInfoBatchParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, teamInfoBatchParams);
        List<TeamInfoVO> batchTeamInfo = rpgTeamService
                .getBatchTeamInfo(teamInfoBatchParams.getTeamSequenceList());
        return ResponsePacket.onSuccess(batchTeamInfo);
    }

    /**
     * 根据uid查找用户（暴鸡）所有已完成的车队唯一Id[去重]
     */
    @Override
    public ResponsePacket<List<TeamSequenceUidVO>> getBaojiTeamSequencesByUids(
            @RequestParam List<String> uids) {
        return ResponsePacket.onSuccess(rpgTeamService.getBaojiTeamSequencesByUids(uids));
    }

    @ApiOperation(value = "老板支付成功后前端调用")
    @Override
    public ResponsePacket<Void> bossConfirmPaidSuccess(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "老板支付成功参数", required = true)
            @RequestBody BossConfirmPaidSuccessParams bossConfirmPaidSuccessParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, bossConfirmPaidSuccessParams);
        rpgTeamService.bossConfirmPaidSuccess(bossConfirmPaidSuccessParams);
        return ResponsePacket.onSuccess();
    }

}

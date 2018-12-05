package com.kaihei.esportingplus.user.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.user.api.feign.UserGameRoleServiceClient;
import com.kaihei.esportingplus.user.api.params.UserGameQueryBaseParams;
import com.kaihei.esportingplus.user.api.params.UserGameRaidCodeQueryParams;
import com.kaihei.esportingplus.user.api.params.UserGameRoleAcrossQueryParams;
import com.kaihei.esportingplus.user.api.params.UserSingeRoleQueryParams;
import com.kaihei.esportingplus.user.api.vo.CertRoleWithJoinRaidVo;
import com.kaihei.esportingplus.user.api.vo.UserGameAboardVo;
import com.kaihei.esportingplus.user.api.vo.UserGameBaseRoleInfoVo;
import com.kaihei.esportingplus.user.api.vo.UserGameDetailRoleInfoVo;
import com.kaihei.esportingplus.user.api.vo.UserGameRoleSimpleVo;
import com.kaihei.esportingplus.user.api.vo.UserSingleRoleDetailInfoVo;
import com.kaihei.esportingplus.user.domain.service.UserGameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/game")
@Api(tags = {"游戏角色服务接口"})
public class UserGameRoleController implements UserGameRoleServiceClient {

    @Autowired
    private UserGameService userGameService;

    @ApiOperation(value = "App调用-用户ID及游戏code获取游戏角色")
    @Override
    public ResponsePacket<List<UserGameBaseRoleInfoVo>> getUserAllBaseRolesApp(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "游戏code", required = true) @PathVariable("game_code") Integer gameCode) {
        return this.getUserAllBaseRoles(UserSessionContext.getUser().getUid(), gameCode);
    }

    @ApiOperation(value = "内部调用-用户上车根据身份校验是否具有权限")
    @Override
    public ResponsePacket<UserSingleRoleDetailInfoVo> getUserIdentityRoleInfo(
            @RequestBody UserSingeRoleQueryParams params) {
        return ResponsePacket.onSuccess(userGameService.getUserIdentityRoleInfo(params));
    }

    @ApiOperation(value = "APP调用-用户创建车队时获取认证角色列表")
    @Override
    public ResponsePacket<List<CertRoleWithJoinRaidVo>> getUserCredentialsAndRaidRoles(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "游戏code", required = true) @PathVariable("game_code") Integer gameCode) {
        String uid = UserSessionContext.getUser().getUid();
        return ResponsePacket
                .onSuccess(userGameService.getUserCredentialsAndRaidRoles(uid, gameCode));
    }

    @ApiOperation(value = "用户ID及游戏code获取游戏角色")
    @Override
    public ResponsePacket<List<UserGameBaseRoleInfoVo>> getUserAllBaseRoles(
            @ApiParam(value = "用户ID", required = true) @PathVariable("user_id") String userId,
            @ApiParam(value = "游戏code", required = true) @PathVariable("game_code") Integer gameCode) {
        return ResponsePacket.onSuccess(userGameService.getUserAllBaseRoles(userId, gameCode));
    }

    @ApiOperation(value = "App调用-用户ID及游戏code获取游戏角色详情")
    @Override
    public ResponsePacket<List<UserGameDetailRoleInfoVo>> getUserAllDetailRolesApp(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "游戏code", required = true) @PathVariable("game_code") Integer gameCode,
            @ApiParam(value = "条件参数", required = false) @RequestBody(required = false) UserGameQueryBaseParams params) {
        return this.getUserAllDetailRoles(UserSessionContext.getUser().getUid(), gameCode, params);
    }

    @ApiOperation(value = "用户ID及游戏code获取游戏角色详情")
    @Override
    public ResponsePacket<List<UserGameDetailRoleInfoVo>> getUserAllDetailRoles(
            @ApiParam(value = "用户ID", required = true) @PathVariable("user_id") String userId,
            @ApiParam(value = "游戏code", required = true) @PathVariable("game_code") Integer gameCode,
            @ApiParam(value = "条件参数", required = false) @RequestBody(required = false) UserGameQueryBaseParams params) {
        return ResponsePacket
                .onSuccess(userGameService.getAllUserGameRoles(userId, gameCode, params));
    }

    @ApiOperation(value = "App调用-用户ID及游戏code获取游戏认证角色详情")
    @Override
    public ResponsePacket<List<UserGameDetailRoleInfoVo>> getCertificalUserGameRolesApp(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "游戏code", required = true) @PathVariable("game_code") Integer gameCode,
            @ApiParam(value = "条件参数", required = false) @RequestBody(required = false) UserGameRaidCodeQueryParams params) {
        return this.getCertificalUserGameRoles(UserSessionContext.getUser().getUid(), gameCode,
                params);
    }

    @ApiOperation(value = "用户ID及游戏code获取游戏认证角色详情")
    @Override
    public ResponsePacket<List<UserGameDetailRoleInfoVo>> getCertificalUserGameRoles(
            @ApiParam(value = "用户ID", required = true) @PathVariable("user_id") String userId,
            @ApiParam(value = "游戏code", required = true) @PathVariable("game_code") Integer gameCode,
            @ApiParam(value = "条件参数", required = false) @RequestBody(required = false) UserGameRaidCodeQueryParams params) {
        return ResponsePacket
                .onSuccess(userGameService.getUserCredentialsRoles(userId, gameCode, params));
    }

    @ApiOperation(value = "用户游戏是否认证")
    @Override
    public ResponsePacket<Boolean> isCertifica(
            @ApiParam(value = "用户ID", required = true) @PathVariable("user_id") String userId,
            @ApiParam(value = "游戏code", required = true) @PathVariable("game_code") Integer gameCode) {
        return ResponsePacket.onSuccess(
                !userGameService.getUserCredentialsRoles(userId, gameCode, null).isEmpty());
    }

    @ApiOperation(value = "用户上车时获取角色列表")
    @Override
    public ResponsePacket<List<UserGameAboardVo>> getAboardGameRole(
            @ApiParam(value = "查询参数", required = true) @RequestBody UserGameRoleAcrossQueryParams params) {
        return ResponsePacket.onSuccess(userGameService.getAboardGameRole(params));
    }

    @ApiOperation(value = "App调用-用户游戏是否认证")
    @Override
    public ResponsePacket<Boolean> isCertificaApp(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "游戏code", required = true) @PathVariable("game_code") Integer gameCode) {
        return ResponsePacket.onSuccess(
                !userGameService
                        .getUserCredentialsRoles(UserSessionContext.getUser().getUid(), gameCode,
                                null).isEmpty());
    }

    @ApiOperation(value = "创建用户角色")
    @Override
    public ResponsePacket<Void> addUserGameRole(@RequestHeader(name = "Authorization") String token,
            @RequestBody UserGameRoleSimpleVo vo) {
        userGameService.addUserGameRole(UserSessionContext.getUser().getUid(), vo);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "删除用户角色")
    @Override
    public ResponsePacket<Void> deleteUserGameRole(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "角色id", required = true) @PathVariable("role_id") Long roleId) {
        userGameService.deleteUserGameRole(UserSessionContext.getUser().getUid(), roleId);
        return ResponsePacket.onSuccess();
    }
}

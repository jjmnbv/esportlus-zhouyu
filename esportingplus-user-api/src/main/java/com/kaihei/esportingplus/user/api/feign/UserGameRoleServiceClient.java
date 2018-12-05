package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.params.UserGameQueryBaseParams;
import com.kaihei.esportingplus.user.api.params.UserGameRaidCodeQueryParams;
import com.kaihei.esportingplus.user.api.params.UserGameRoleAcrossQueryParams;
import com.kaihei.esportingplus.user.api.params.UserSingeRoleQueryParams;
import com.kaihei.esportingplus.user.api.vo.*;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "esportingplus-user-service", path = "/user/game", fallbackFactory = UserGameRoleServiceClientFallbackFactory.class)
public interface UserGameRoleServiceClient {

    /**
     * 获取用户基本信息，不包含认证信息
     *
     * @param userId 用户id
     * @param gameCode 游戏代码
     */
    @GetMapping("/role/base/{user_id}/{game_code}")
    ResponsePacket<List<UserGameBaseRoleInfoVo>> getUserAllBaseRoles(
            @PathVariable("user_id") String userId, @PathVariable("game_code") Integer gameCode);

    /**
     * 获取用户游戏角色，如果有认证信息则附带认证信息
     *
     * @param userId 用户id
     * @param gameCode 游戏代码
     * @param params 可选参数
     */
    @PostMapping("/role/details/{user_id}/{game_code}")
    ResponsePacket<List<UserGameDetailRoleInfoVo>> getUserAllDetailRoles(
            @PathVariable("user_id") String userId, @PathVariable("game_code") Integer gameCode,
            @RequestBody(required = false)
                    UserGameQueryBaseParams params);

    /**
     * 获取用户游戏角色认证，如果没有认证则不输出,保证角色取得认证
     *
     * @param userId 用户id
     * @param gameCode 游戏代码
     * @param params 可选参数
     */
    @PostMapping("/role/credential/{user_id}/{game_code}")
    ResponsePacket<List<UserGameDetailRoleInfoVo>> getCertificalUserGameRoles(
            @PathVariable("user_id") String userId, @PathVariable("game_code") Integer gameCode,
            @RequestBody(required = false)
                    UserGameRaidCodeQueryParams params);


    /**
     * 判断用户游戏是否认证过
     *
     * @param userId 用户id
     * @param gameCode 游戏代码
     */
    @GetMapping("/role/is_credential/{user_id}/{game_code}")
    public ResponsePacket<Boolean> isCertifica(@PathVariable("user_id") String userId,
            @PathVariable("game_code") Integer gameCode);

    /**
     * 获取用户上车身份列表
     */
    @PostMapping("role/aboard/list")
    public ResponsePacket<List<UserGameAboardVo>> getAboardGameRole(
            @RequestBody UserGameRoleAcrossQueryParams params);

    /**
     * 添加用户角色
     */
    @PostMapping("/role/add")
    public ResponsePacket<Void> addUserGameRole(@RequestHeader(name = "Authorization") String token,
            @RequestBody UserGameRoleSimpleVo vo);

    /**
     * 删除用户角色
     */
    @DeleteMapping("/role/{role_id}/delete")
    public ResponsePacket<Void> deleteUserGameRole(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("role_id") Long roleId);

    /**
     * 用户是否具有认证身份
     */
    @GetMapping("/role/is_credential/{game_code}")
    public ResponsePacket<Boolean> isCertificaApp(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("game_code") Integer gameCode);

    /**
     * 获取具有身份认证的游戏角色
     */
    @PostMapping("/role/credential/{game_code}")
    public ResponsePacket<List<UserGameDetailRoleInfoVo>> getCertificalUserGameRolesApp(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("game_code") Integer gameCode,
            @RequestBody(required = false) UserGameRaidCodeQueryParams params);

    /**
     * 获取用户角色，如果有身份认证则返回身份认证信息
     */
    @PostMapping("/role/details/{game_code}")
    public ResponsePacket<List<UserGameDetailRoleInfoVo>> getUserAllDetailRolesApp(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("game_code") Integer gameCode,
            @RequestBody(required = false) UserGameQueryBaseParams params);

    /**
     * 获取用户角色基本信息
     */
    @GetMapping("/role/base/{game_code}")
    public ResponsePacket<List<UserGameBaseRoleInfoVo>> getUserAllBaseRolesApp(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "游戏code", required = true) @PathVariable("game_code") Integer gameCode);

    /**
     * 用户上车根据身份校验是否具有权限
     * @param params
     * @return
     */
    @PostMapping("/role/check")
    public ResponsePacket<UserSingleRoleDetailInfoVo> getUserIdentityRoleInfo(@RequestBody UserSingeRoleQueryParams params);

    /**
     * 创建车队时调用，获取认证身份信息和对应副本
     * @param gameCode
     * @param token
     * @return
     */
    @GetMapping("/role/team/create/{game_code}")
    public ResponsePacket<List<CertRoleWithJoinRaidVo>> getUserCredentialsAndRaidRoles(@RequestHeader(name = "Authorization") String token,
            @PathVariable("game_code")Integer gameCode);
}

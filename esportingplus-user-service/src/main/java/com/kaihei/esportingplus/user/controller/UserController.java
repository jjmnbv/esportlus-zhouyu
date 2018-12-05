package com.kaihei.esportingplus.user.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.user.api.feign.UserServiceClient;
import com.kaihei.esportingplus.user.api.params.UpdateAvatarParams;
import com.kaihei.esportingplus.user.domain.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Api(tags = {"用户服务接口"})
public class UserController implements UserServiceClient {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "根据用户uid批量查询用户信息")
    @Override
    public ResponsePacket<List<UserSessionContext>> getUserInfosByUids(
            @ApiParam(value = "用户uid集合", required = true) @RequestBody List<String> uids) {
        return ResponsePacket.onSuccess(userService.getUserInfosByIds(uids));
    }

    @ApiOperation(value = "根据用户uid查询用户信息")
    @Override
    public ResponsePacket<UserSessionContext> getUserInfosByUid(
            @ApiParam(value = "用户uid", required = true) @PathVariable("uid") String uid) {
        return ResponsePacket.onSuccess(userService.getUserInfoByUid(uid));
    }

    @ApiOperation(value = "转换并修改用户头像")
    @Override
    public ResponsePacket<String> changeAndUpdateAvatar(@RequestBody UpdateAvatarParams params) {
        return ResponsePacket.onSuccess(userService.changeAndUpdateAvatar(params.getUid(), params.getAvatar()));
    }

    @ApiOperation(value = "获取用户七牛头像")
    @Override
    public ResponsePacket<String> getAvatarLink(
            @ApiParam(value = "用户uid", required = true) @PathVariable("uid") String uid) {
        return ResponsePacket.onSuccess(userService.getAvatarLink(uid));
    }
    @ApiOperation(value = "通过用户id获取用户七牛头像并修改")
    @Override
    public ResponsePacket<String> getAvatarLinkAndNotifyUpdateAvatar(@ApiParam(value = "用户uid", required = true) @PathVariable("uid")String uid) {
        return ResponsePacket.onSuccess(userService.getAvatarLinkAndNotifyUpdateAvatar(uid));
    }
}

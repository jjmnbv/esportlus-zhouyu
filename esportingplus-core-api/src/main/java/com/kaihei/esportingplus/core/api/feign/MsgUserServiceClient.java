package com.kaihei.esportingplus.core.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.core.api.params.UserBlackParam;
import com.kaihei.esportingplus.core.api.params.UserBlockParam;
import com.kaihei.esportingplus.core.api.params.MsgUserParam;
import com.kaihei.esportingplus.core.api.params.UserTagParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author liuyang
 * @Description 用户管理api
 * @Date 2018/10/26 11:26
 **/
@FeignClient(name = "esportingplus-core-service", path = "/message/user", fallbackFactory = MsgUserServiceClientFallback.class)
public interface MsgUserServiceClient {

    /**
     * 获取融云token
     * @param param
     */
    @PostMapping("/token")
    ResponsePacket<String> getToken(@RequestBody MsgUserParam param);

    /**
     * 刷新融云用户信息
     * @param param
     */
    @PostMapping("/refresh")
    ResponsePacket<Boolean> updateUser(@RequestBody MsgUserParam param);

    /**
     * 封禁用户
     * @param blockUserParam
     */
    @PostMapping("/block")
    ResponsePacket<Boolean> blockUser(@RequestBody UserBlockParam blockUserParam);

    /**
     * 解除用户封禁
     *
     * @param userIds
     */
    @PostMapping("/unblock")
    ResponsePacket<Boolean> unBlockUser(@RequestParam(name = "userIds", required = true) List<String> userIds);

    /**
     * 添加用户到黑名单
     *
     * @param blackUserParam
     */
    @PostMapping("/blacklist/add")
    ResponsePacket<Boolean> addUserToBlacklist(@RequestBody UserBlackParam blackUserParam);

    /**
     * 移除黑名单中用户
     * @param blackUserParam
     */
    @PostMapping("/blacklist/remove")
    ResponsePacket<Boolean> removeUserFromBlacklist(@RequestBody UserBlackParam blackUserParam);

    /**
     * 获取某用户黑名单列表
     *
     * @param uid uid
     */
    @GetMapping("/blacklist/query/{uid}")
    ResponsePacket<String[]> queryUsersBlacklist(@PathVariable(name = "uid", required = true) String uid);

    /**
     * 获取某用户的融云uid
     *
     * @param uid uid
     */
    @GetMapping("/ronyun/query/{uid}")
    ResponsePacket<String> queryRonyunUid(@PathVariable(name = "uid", required = true) String uid);

    @PostMapping("/tag/set")
    ResponsePacket<Boolean> setTag(@RequestBody UserTagParam userTagParam);

}

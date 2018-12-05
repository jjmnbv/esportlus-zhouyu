package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.user.api.params.UpdateAvatarParams;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户Feign
 * @zhangfang
 */
@FeignClient(name = "esportingplus-user-service", path = "/user", fallbackFactory = UserServiceClientFallbackFactory.class)
public interface UserServiceClient {

    /**
     * 根据用户uid集合批量查询用户信息
     * @param uids
     * @return
     */
    @PostMapping("/info/uids/list")
    public ResponsePacket<List<UserSessionContext>> getUserInfosByUids(@RequestBody  List<String> uids);

    /**
     * 根据用户uid查询用户信息
     * @param uid
     * @return
     */
    @GetMapping("/info/uid/{uid}")
    public ResponsePacket<UserSessionContext> getUserInfosByUid(@PathVariable("uid")  String uid);

    /**
     * 转换用户头像，如果不是七牛地址则转成七牛头像,并且同步修改python库头像地址
     * @param params
     * @return
     */
    @PostMapping("/info/avatar/change/update")
    public ResponsePacket<String> changeAndUpdateAvatar(@RequestBody UpdateAvatarParams params);

    /**
     * 获取用户头像，如果不是七牛地址则转成七牛头像
     * @param uid
     * @return
     */
    @GetMapping("/info/avatar/uid/{uid}")
    public ResponsePacket<String> getAvatarLink(String uid);
    /**
     * 获取用户头像，如果不是七牛地址则转成七牛头像,并且同步修改python库头像地址
     * @param uid
     * @return
     */
    @GetMapping("/info/avatar/uid/{uid}/update")
    public ResponsePacket<String> getAvatarLinkAndNotifyUpdateAvatar(String uid);
}

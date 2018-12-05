package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.params.PhoneBindParams;
import com.kaihei.esportingplus.user.api.params.ThirdPartLoginParams;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户绑定第三方账号以及手机号服务
 *
 * @author yangshidong
 * @date 2018/10/30
 */
@FeignClient(name = "esportingplus-user-service", path = "/bind", fallbackFactory = UserBindServiceClientFallbackFactory.class)
public interface UserBindServiceClient {
    /**
     * 绑定第三方账号:wx/qq
     *
     * @param params
     */
    @PostMapping("/auth3/bind")
    ResponsePacket auth3Bind(@RequestBody ThirdPartLoginParams params);


    /**
     * 绑定手机号
     *
     * @param params
     */
    @PostMapping("/phone/bind")
    ResponsePacket phoneBind(@RequestBody PhoneBindParams params);

    /**
     * H5绑定第三方账号:wx/qq
     *
     * @param params
     */
    @PostMapping("/h5/phone/bind")
    ResponsePacket h5PhoneBind(@RequestBody PhoneBindParams params);

    /**
     * 获取绑定列表
     *
     * @param token
     */
    @GetMapping("/list")
    ResponsePacket bindList(@RequestHeader(name = "Authorization", required = false) String token);

    /**
     * 绑定手机号验证
     *
     * @param params
     * @param token
     */
    @PostMapping("/verify/old_phone")
    ResponsePacket verifyOldPhone(@RequestBody PhoneBindParams params,
                                  @RequestHeader(name = "Authorization", required = false) String token);

    /**
     * 更改绑定手机号
     *
     * @param params
     * @param token
     */
    @PostMapping("/update/phone")
    ResponsePacket updatePhone(@RequestBody PhoneBindParams params,
                               @RequestHeader(name = "Authorization", required = false) String token);

    /**
     * 解绑第三方账号:wx/qq
     *
     * @param platform
     * @param token
     */
    @DeleteMapping("/unbind/auth3/{unbind_type}")
    ResponsePacket unbindAuth3(@PathVariable("unbind_type") String platform,
                               @RequestHeader(name = "Authorization", required = false) String token);

    /**
     * 根据uid获取绑定的微信unionid
     *
     * @param uids 批量查询
     * @return uid:unionid的键值对集合
     */
    @PostMapping("/getUinon")
    ResponsePacket getUnionIdByUids(@RequestBody List<String> uids);

}

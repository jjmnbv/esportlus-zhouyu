package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.vo.MembersUserVo;
import com.kaihei.esportingplus.user.api.vo.UserRiskVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 风控服务内部调用，不对外开放
 * @author chenzhenjun
 */
@FeignClient(name = "esportingplus-user-service",
        path = "/user/risk", fallbackFactory = UserRiskClientFallbackFactory.class)
public interface UserRiskServiceClient {

    /**
     * 查询用户风险详情
     * @param userId
     * @param deviceId
     * @return
     */
    @GetMapping("/detail")
    public ResponsePacket<UserRiskVo> getRiskNextDataInfo(@RequestParam(value = "user_id",required = true) String userId,
                                                      @RequestParam(value = "device_id", required = true) String deviceId);

    /**
     * 查询用户是否注册过
     * @param userId
     * @param deviceId
     * @return
     */
    @GetMapping("/device")
    public ResponsePacket<Integer> getRiskNextDataCount(@RequestParam(value = "user_id",required = true) String userId,
                                                   @RequestParam(value = "device_id", required = true) String deviceId);

    /**
     * 通过uids获取deviceIds
     * @param uids
     * @return
     */
    @GetMapping("/uids")
    public ResponsePacket<List<UserRiskVo>> getDeviceIds(@RequestParam(value = "uids",required = true) String uids);

    /**
     * 通过uid查询用户信息
     * @param uid
     * @return
     */
    @GetMapping("/uid")
    public ResponsePacket<MembersUserVo> getUserInfo(@RequestParam(value = "uid",required = true) String uid);

}

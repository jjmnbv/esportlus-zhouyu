package com.kaihei.esportingplus.gamingteam.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.gamingteam.api.params.WxTeamEndParams;
import com.kaihei.esportingplus.gamingteam.api.params.WxTeamOrderCancelParams;
import com.kaihei.esportingplus.gamingteam.api.params.WxTeamStartParams;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "esportingplus-gamingteam-service",
        path = "/program/push", fallbackFactory = WxSmallProgramClientFallbackFactory.class)
public interface WxSmallProgramPushClient {

    @PostMapping("/order/end")
    public ResponsePacket<Void> pushOrderEnd(@RequestBody WxTeamEndParams pushParams);

    /**
     * 订单取消服务通知
     * @param cancelParams
     */
    @PostMapping("/order/cancel")
    public ResponsePacket<Void> pushOrderCancel(WxTeamOrderCancelParams cancelParams);

    /**
     * 团长开车微信小程序推送，团长除外
     * @param startParams
     */
    @PostMapping("/team/start")
    public ResponsePacket<Void> pushTeamStart(WxTeamStartParams startParams);
}

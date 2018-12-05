package com.kaihei.esportingplus.riskrating.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.riskrating.api.params.LoginParams;
import com.kaihei.esportingplus.riskrating.api.params.RechargeRiskParams;
import com.kaihei.esportingplus.riskrating.api.vo.RiskBaseResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 基于 feign 实现风控服务接口调用
 * 1. esportingplus-risk-rating-service为服务名
 * 2. fallbackFactory指定断路器实现类
 * @author chenzhenjun
 */
@FeignClient(name = "esportingplus-risk-rating-service",
        path = "/risk", fallbackFactory = RiskRatingFallbackFactory.class)
public interface RiskRatingServiceClient {

    /**
     * 登陆或注册-风控接口
     * @param loginParams loginParams
     * @return ResponsePacket<String>
     */
    @PostMapping("/api/v1/risk/login_register")
    public ResponsePacket<String> checkLoginOrRegister(@RequestBody LoginParams loginParams);

    /**
     *  充值前检查，能否充值
     * @param token
     * @param amount
     * @param deviceNo
     * @return
     */
    @GetMapping("/recharge_check")
    public ResponsePacket<RiskBaseResponse> checkRechargeStatus(@RequestHeader(name = "Authorization") String token,
            @RequestParam(value="amount", required=true) Integer amount,
            @RequestParam(value="device_no", required=true) String deviceNo);

}

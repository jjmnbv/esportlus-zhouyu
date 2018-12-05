package com.kaihei.esportingplus.riskrating.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.riskrating.api.enums.RiskEnum;
import com.kaihei.esportingplus.riskrating.api.feign.RiskRatingServiceClient;
import com.kaihei.esportingplus.riskrating.api.params.LoginParams;
import com.kaihei.esportingplus.riskrating.api.params.RechargeRiskParams;
import com.kaihei.esportingplus.riskrating.api.vo.RiskBaseResponse;
import com.kaihei.esportingplus.riskrating.service.RiskBlackRechargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/risk")
@Api(tags = {"风控相关API"})
public class RiskRatingController implements RiskRatingServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(RiskRatingController.class);
    @Autowired
    private RiskBlackRechargeService riskBlackRechargeService;
    @ApiOperation(value = "登陆注册风控服务接口")
    @Override
    public ResponsePacket checkLoginOrRegister(@RequestBody LoginParams loginParams) {
        logger.debug("打印登陆注册风控服务接口-入参信息：" + loginParams.toString());
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "充值前检查能否充值接口")
    @Override
    public ResponsePacket<RiskBaseResponse> checkRechargeStatus(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(value = "amount", required = true) Integer amount,
            @RequestParam(value = "device_no", required = true) String deviceNo) {
        RechargeRiskParams rechargeRiskParams = new RechargeRiskParams();
        rechargeRiskParams.setUid(String.valueOf(UserSessionContext.getUser().getUid()));
        rechargeRiskParams.setAmount(amount);
        rechargeRiskParams.setDeviceNo(deviceNo);
        logger.debug("充值前检查能否充值接口-入参信息：" + rechargeRiskParams.toString());
        return ResponsePacket
                .onSuccess(riskBlackRechargeService.checkRechargeStatus(rechargeRiskParams));
    }
}

package com.kaihei.esportingplus.riskrating.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.riskrating.api.feign.RiskDeviceUserClient;
import com.kaihei.esportingplus.riskrating.api.params.RiskDeviceUserLogQueryParams;
import com.kaihei.esportingplus.riskrating.api.params.RiskDeviceWhiteQueryParams;
import com.kaihei.esportingplus.riskrating.api.params.RiskDeviceWhiteUpdateParams;
import com.kaihei.esportingplus.riskrating.api.vo.RiskDeviceUserRechargeLogVo;
import com.kaihei.esportingplus.riskrating.api.vo.RiskDeviceWhiteVo;
import com.kaihei.esportingplus.riskrating.service.RiskDeviceUserRechargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"风控设备相关api"})
@RequestMapping("/risk/device/user")
public class RiskDeviceUserController implements RiskDeviceUserClient {
    @Autowired
    private RiskDeviceUserRechargeService riskDeviceUserRechargeService;
    /**
     * 分页查询设备充值记录
     * @param params
     * @return
     */
    @ApiOperation(value = "分页查询设备充值记录")
    @Override
    public ResponsePacket<Object> findRiskDeviceRechargeLogByPage(@RequestBody RiskDeviceUserLogQueryParams params){
        return ResponsePacket.onSuccess(riskDeviceUserRechargeService.findRiskDeviceRechargeLogByPage(params));
    }

    /**
     * 分页查询设备白名单
     * @param params
     * @return
     */
    @ApiOperation(value = "分页查询设备白名单")
    @Override
    public ResponsePacket<Object> findRiskDeviceBindByPage(@RequestBody RiskDeviceWhiteQueryParams params){
        return ResponsePacket.onSuccess(riskDeviceUserRechargeService.findRiskDeviceBindByPage(params));
    }

    /**
     * 通过id查询设备白名单
     * @param id
     * @return
     */
    @ApiOperation(value = "通过id查询设备白名单")
    @Override
    public ResponsePacket<RiskDeviceWhiteVo> findRiskDeviceWhiteVoById(@ApiParam(value = "白名单id", required = true)@PathVariable("id")Long id){
        return ResponsePacket.onSuccess(riskDeviceUserRechargeService.findRiskDeviceWhiteVoById(id));
    }

    /**
     * 增加一个设备白名单
     * @param params
     */
    @ApiOperation(value = "增加一个设备白名单")
    @Override
    public ResponsePacket<Void> saveRiskDeviceWhite(@RequestBody RiskDeviceWhiteUpdateParams params){
        riskDeviceUserRechargeService.saveRiskDeviceWhite(params);
        return ResponsePacket.onSuccess();
    }

    /**
     * 修改设备白名单
     * @param params
     */
    @ApiOperation(value = "修改设备白名单")
    @Override
    public ResponsePacket<Void> updateRiskDeviceWhite(@RequestBody RiskDeviceWhiteUpdateParams params){
        riskDeviceUserRechargeService.updateRiskDeviceWhite(params);
        return ResponsePacket.onSuccess();
    }
}

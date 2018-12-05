package com.kaihei.esportingplus.riskrating.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.riskrating.api.enums.FreeTeamEnum;
import com.kaihei.esportingplus.riskrating.api.feign.FreeTeamClient;
import com.kaihei.esportingplus.riskrating.api.params.FreeTeamBasicParams;
import com.kaihei.esportingplus.riskrating.api.params.FreeTeamWhiteQueryParams;
import com.kaihei.esportingplus.riskrating.api.vo.FreeTeamConfigVo;
import com.kaihei.esportingplus.riskrating.api.vo.FreeTeamResponse;
import com.kaihei.esportingplus.riskrating.service.FreeTeamService;
import com.kaihei.esportingplus.riskrating.service.RiskDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 免费车队controller类
 *
 * @author chenzhenjun
 */
@RestController
@Api(tags = {"免费车队风控相关API"})
@RequestMapping("/freeteam")
public class FreeTeamController implements FreeTeamClient {

    private static final Logger logger = LoggerFactory.getLogger(FreeTeamController.class);

    @Autowired
    private FreeTeamService freeTeamService;

    @Autowired
    private RiskDictService riskDictService;

    @ApiOperation(value = "用户注册绑定数美ID")
    @Override
    public ResponsePacket<FreeTeamResponse> checkUserRegister(@RequestBody FreeTeamBasicParams freeTeamBasicParams) {
        if (!riskDictService.checkRiskSwitchStatus()) {
            FreeTeamResponse response = new FreeTeamResponse(FreeTeamEnum.SUCCESS.getCode(), FreeTeamEnum.SUCCESS.getMsg());
            return ResponsePacket.onSuccess(response);
        }
        return ResponsePacket.onSuccess(freeTeamService.checkRegisterReward(freeTeamBasicParams));
    }

    @ApiOperation(value = "用户发起免费车队前校验")
    @Override
    public ResponsePacket<FreeTeamResponse> checkFreeTeamChance(@RequestBody FreeTeamBasicParams freeTeamBasicParams) {
        if (!riskDictService.checkRiskSwitchStatus()) {
            FreeTeamResponse response = new FreeTeamResponse(FreeTeamEnum.SUCCESS.getCode(), FreeTeamEnum.SUCCESS.getMsg());
            return ResponsePacket.onSuccess(response);
        }
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, freeTeamBasicParams);
        logger.debug("checkFreeTeamChance >> 入参 >> " + freeTeamBasicParams.toString());
        FreeTeamResponse response = freeTeamService.checkFreeTeamChance(freeTeamBasicParams);
        return ResponsePacket.onSuccess(response);
    }

    @ApiOperation(value = "核减免费车队上车次数")
    @Override
    public ResponsePacket updateChanceTimes(@RequestParam(value = "uids", required = true) String uids) {
        logger.debug("updateChanceTimes >> 入参 >> " + uids);
        try {
            if (!riskDictService.checkRiskSwitchStatus()) {
                return ResponsePacket.onSuccess();
            }
            ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, uids);
            freeTeamService.updateTimes(uids);

        } catch (BusinessException e) {
            logger.error("updateChanceTimes >> Exception : " + e.getErrMsg());
            return ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        }

        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "恶意设备加白名单")
    @Override
    public ResponsePacket insertWhiteList(@RequestParam(value = "device_id", required = true) String deviceId) {
        logger.debug("insertWhiteList >> 入参 >> " + deviceId);
        try {
            if(!riskDictService.checkRiskSwitchStatus()){
                return ResponsePacket.onSuccess();
            }
            ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, deviceId);
            freeTeamService.insertWhiteList(deviceId);

        } catch (BusinessException e) {
            logger.error("insertWhiteList >> Exception : " + e.getErrMsg());
            return ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        }

        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "查询白名单列表")
    @Override
    public ResponsePacket<Map<String, Object>> getWhiteList(@RequestParam(value = "device_id", required = false) String deviceId,
                                                            @RequestParam(value = "begin_date", required = false) String beginDate,
                                                            @RequestParam(value = "end_date", required = false) String endDate,
                                                            @RequestParam(value = "page", defaultValue = "1") String page,
                                                            @RequestParam(value = "size", defaultValue = "20") String size) {
        logger.debug("getWhiteList >> params >> deviceId :{}, beginDate:{}, endDate:{}, page:{}, size:{}", deviceId, beginDate, endDate, page, size);
        FreeTeamWhiteQueryParams queryParams = new FreeTeamWhiteQueryParams();
        queryParams.setDeviceId(deviceId);
        queryParams.setBeginDate(beginDate);
        queryParams.setEndDate(endDate);
        queryParams.setPage(page);
        queryParams.setSize(size);
        Map<String, Object> restultMap = new HashMap<>();
        try {
            if (!riskDictService.checkRiskSwitchStatus()) {
                return ResponsePacket.onSuccess();
            }
            logger.debug("getWhiteList >> 入参 >> queryParams ={}  ", queryParams);
            restultMap = freeTeamService.getWhiteList(queryParams);
        } catch (BusinessException e) {
            logger.error("getWhiteList >> Exception : " + e.getErrMsg());
            return ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        }
        return ResponsePacket.onSuccess(restultMap);
    }

    @ApiOperation(value = "设备移出白名单")
    @Override
    public ResponsePacket<Boolean> deleteWhite(@PathVariable("id") long id) {
        if (!riskDictService.checkRiskSwitchStatus()) {
            return ResponsePacket.onSuccess();
        }
        return ResponsePacket.onSuccess(freeTeamService.deleteWhite(id));
    }

    @ApiOperation(value = "查询免费车队风控配置")
    @Override
    public ResponsePacket<FreeTeamConfigVo> getFreeTeamConfig() {
        if (!riskDictService.checkRiskSwitchStatus()) {
            return ResponsePacket.onSuccess();
        }
        return ResponsePacket.onSuccess(freeTeamService.getConfig());
    }

    @ApiOperation(value = "修改免费车队风控配置")
    @Override
    public ResponsePacket updateFreeTeamConfig(@RequestBody FreeTeamConfigVo configVo) {
        if (!riskDictService.checkRiskSwitchStatus()) {
            return ResponsePacket.onSuccess();
        }
        freeTeamService.updateFreeTeamConfig(configVo);
        return ResponsePacket.onSuccess();
    }
}

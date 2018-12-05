package com.kaihei.esportingplus.riskrating.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.riskrating.api.enums.FreeTeamEnum;
import com.kaihei.esportingplus.riskrating.api.enums.ShumeiEventEnum;
import com.kaihei.esportingplus.riskrating.api.feign.ImMachineBackendClient;
import com.kaihei.esportingplus.riskrating.api.params.ImMachineUpdateParams;
import com.kaihei.esportingplus.riskrating.api.vo.*;
import com.kaihei.esportingplus.riskrating.service.ImMachineService;
import com.kaihei.esportingplus.riskrating.service.RiskDictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/im_machine")
public class ImMachineBackendController implements ImMachineBackendClient {

    private static final Logger logger = LoggerFactory.getLogger(ImMachineBackendController.class);

    @Autowired
    private ImMachineService imMachineService;

    @Autowired
    private RiskDictService riskDictService;

    @Override
    public ResponsePacket<ImMachineConfigVo> getImMachineConfig() {
        return ResponsePacket.onSuccess(imMachineService.getImMachineConfig());
    }

    @Override
    public ResponsePacket updateImMachineConfig(@RequestBody ImMachineConfigVo configVo) {
        imMachineService.updateImMachineConfig(configVo);
        return ResponsePacket.onSuccess();
    }

    @Override
    public ResponsePacket<PageInfo<ImMachineListVo>> getDeviceBlackList(@RequestParam(value = "device_id", required = false) String deviceId,
                                                                        @RequestParam(value = "page", defaultValue = "1") String page,
                                                                        @RequestParam(value = "size", defaultValue = "20") String size) {
        return ResponsePacket.onSuccess(imMachineService.getDeviceBlackList(deviceId, page, size));
    }

    @Override
    public ResponsePacket insertDeviceBlack(@RequestBody ImMachineListVo listVo) {
        imMachineService.insertDeviceBlack(listVo.getDeviceId());
        return ResponsePacket.onSuccess();
    }

    @Override
    public ResponsePacket deleteDeviceBlack(@PathVariable("id") long id) {
        imMachineService.deleteDeviceBlack(id);
        return ResponsePacket.onSuccess();
    }

    @Override
    public ResponsePacket<PageInfo<ImMachineListVo>> getUserDeviceBindList(@RequestParam(value = "type", required = true) String type,
                                                                           @RequestParam(value = "column", required = false) String column,
                                                                           @RequestParam(value = "page", defaultValue = "1") String page,
                                                                           @RequestParam(value = "size", defaultValue = "20") String size) {
        return ResponsePacket.onSuccess(imMachineService.getUserDeviceBindList(type, column, page, size));
    }

    @Override
    public ResponsePacket unbindUserDeviceRelation(@RequestBody ImMachineListVo listVo) {
        imMachineService.unbindUserDeviceRelation(listVo.getId());
        return ResponsePacket.onSuccess();
    }

    @Override
    public ResponsePacket<PageInfo<ImMachineListVo>> getUserImBlackList(@RequestParam(value = "user_id", required = false) String userId,
                                                                        @RequestParam(value = "page", defaultValue = "1") String page,
                                                                        @RequestParam(value = "size", defaultValue = "20") String size) {
        return ResponsePacket.onSuccess(imMachineService.getUserImBlackList(userId, page, size));
    }

    @Override
    public ResponsePacket deleteUserImBlack(@PathVariable("id") long id) {
        imMachineService.deleteUserImBlack(id);
        return ResponsePacket.onSuccess();
    }

    /**
     * 注册登陆Controller
     * @param imMachineUpdateParams
     * @return
     */
    @Override
    public ResponsePacket<FreeTeamResponse> loginRegisterCheck(@RequestBody ImMachineUpdateParams imMachineUpdateParams) {
        if (!riskDictService.checkRiskSwitchStatus()) {
            FreeTeamResponse response = new FreeTeamResponse(FreeTeamEnum.SUCCESS.getCode(), FreeTeamEnum.SUCCESS.getMsg());
            return ResponsePacket.onSuccess(response);
        }
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, imMachineUpdateParams);
        FreeTeamResponse response = null;
        String type = imMachineUpdateParams.getType();
        if (ShumeiEventEnum.REGISTER.getEventCode().equals(type)) {
            response = imMachineService.registerCheck(imMachineUpdateParams);
        } else {
            response = imMachineService.loginCheck(imMachineUpdateParams);
        }
        return ResponsePacket.onSuccess(response);
    }

    @Override
    public ResponsePacket<FreeTeamResponse> registerCheckBeforeGenerateUserId(
            @RequestParam(value = "device_id", required = true) String deviceId,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "version", required = false) String version) {
        if (!riskDictService.checkRiskSwitchStatus()) {
            FreeTeamResponse response = new FreeTeamResponse(FreeTeamEnum.SUCCESS.getCode(), FreeTeamEnum.SUCCESS.getMsg());
            return ResponsePacket.onSuccess(response);
        }
        return ResponsePacket.onSuccess(imMachineService.registerCheckBeforeGenerateUserId(
                deviceId, phone, version));
    }

    @Override
    public ResponsePacket<UserDeviceBehaviourRecordVo> getBehaviourRecordInfo(@RequestParam(value = "user_id", required = true) String userId) {
        return ResponsePacket.onSuccess(imMachineService.getBehaviourRecordInfo(userId));
    }

    @Override
    public ResponsePacket updateBehaviourRecord(@RequestBody UserDeviceBehaviourRecordVo vo) {
        imMachineService.updateBehaviourRecord(vo);
        return ResponsePacket.onSuccess();
    }
}

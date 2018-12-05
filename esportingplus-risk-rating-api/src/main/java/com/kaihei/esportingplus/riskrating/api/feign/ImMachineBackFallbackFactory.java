package com.kaihei.esportingplus.riskrating.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.riskrating.api.params.ImMachineUpdateParams;
import com.kaihei.esportingplus.riskrating.api.vo.*;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ImMachineBackFallbackFactory implements FallbackFactory<ImMachineBackendClient> {
    @Override
    public ImMachineBackendClient create(Throwable throwable) {
        return new ImMachineBackendClient() {
            @Override
            public ResponsePacket<ImMachineConfigVo> getImMachineConfig() {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket updateImMachineConfig(ImMachineConfigVo configVo) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PageInfo<ImMachineListVo>> getDeviceBlackList(String deviceId, String page, String size) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket insertDeviceBlack(ImMachineListVo listVo) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket deleteDeviceBlack(long id) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PageInfo<ImMachineListVo>> getUserDeviceBindList(String type, String column, String page, String size) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket unbindUserDeviceRelation(ImMachineListVo listVo) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PageInfo<ImMachineListVo>> getUserImBlackList(String userId, String page, String size) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket deleteUserImBlack(long id) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<FreeTeamResponse> loginRegisterCheck(ImMachineUpdateParams imMachineUpdateParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<FreeTeamResponse> registerCheckBeforeGenerateUserId(String deviceId, String phone, String version) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<UserDeviceBehaviourRecordVo> getBehaviourRecordInfo(String userId) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket updateBehaviourRecord(UserDeviceBehaviourRecordVo vo) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

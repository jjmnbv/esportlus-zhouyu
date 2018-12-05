package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.vo.MembersUserVo;
import com.kaihei.esportingplus.user.api.vo.UserRiskVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRiskClientFallbackFactory implements FallbackFactory<UserRiskServiceClient> {

    @Override
    public UserRiskServiceClient create(Throwable throwable) {
        return new UserRiskServiceClient() {
            @Override
            public ResponsePacket<Integer> getRiskNextDataCount(String userId, String deviceId) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<UserRiskVo> getRiskNextDataInfo(String userId, String deviceId) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<UserRiskVo>> getDeviceIds(String uids) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<MembersUserVo> getUserInfo(String uid) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

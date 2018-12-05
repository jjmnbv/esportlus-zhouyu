package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.params.UserPictureParams;
import com.kaihei.esportingplus.user.api.params.UserUpdateParams;
import com.kaihei.esportingplus.user.api.vo.*;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class BaoJiInfoServiceClientFallbackFactory implements
        FallbackFactory<BaoJiInfoServiceClient> {

    @Override
    public BaoJiInfoServiceClient create(Throwable throwable) {
        return new BaoJiInfoServiceClient() {

            @Override
            public ResponsePacket<Integer> getIdentityByUid(String uid) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PointDateVo> getUserPointDate(String uid) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

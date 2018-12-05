package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.vo.MembersUserPointItemVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDataServiceClientFallbackFactory implements
        FallbackFactory<UserDataServiceClient> {

    @Override
    public UserDataServiceClient create(Throwable throwable) {
        return new UserDataServiceClient() {

            @Override
            public ResponsePacket incrUserFreeData(List<String> acceptList, List<String> placeList) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

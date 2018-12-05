package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.params.PhoneBindParams;
import com.kaihei.esportingplus.user.api.params.ThirdPartLoginParams;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * @author yangshidong
 * @date 2018/10/30
 */
@Component
public class UserBindServiceClientFallbackFactory implements FallbackFactory<UserBindServiceClient> {

    @Override
    public UserBindServiceClient create(Throwable cause) {
        return new UserBindServiceClient() {

            @Override
            public ResponsePacket auth3Bind(ThirdPartLoginParams params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket phoneBind(PhoneBindParams params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket h5PhoneBind(PhoneBindParams params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket bindList(String token) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket verifyOldPhone(PhoneBindParams params, String token) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket updatePhone(PhoneBindParams params, String token) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket unbindAuth3(String platform, String token) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket getUnionIdByUids(List<String> uids) {
                return ResponsePacket.onHystrix();
            }


        };
    }
}


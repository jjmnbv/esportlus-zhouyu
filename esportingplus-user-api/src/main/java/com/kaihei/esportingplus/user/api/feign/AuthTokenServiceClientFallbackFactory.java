package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/29 20:10
 */
@Component
public class AuthTokenServiceClientFallbackFactory implements
        FallbackFactory<AuthTokenServiceClient> {

    @Override
    public AuthTokenServiceClient create(Throwable throwable) {
        return new AuthTokenServiceClient() {
            @Override
            public ResponsePacket<String> refreshToken(String pythonToken, String version) {
                return ResponsePacket.onHystrix();
            }

        };
    }
}

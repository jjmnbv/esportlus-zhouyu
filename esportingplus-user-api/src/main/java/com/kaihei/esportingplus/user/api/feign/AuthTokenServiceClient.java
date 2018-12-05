package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 鉴权token服务
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/29 20:00
 */
@FeignClient(name = "esportingplus-user-service", path = "/token", fallbackFactory = AuthTokenServiceClientFallbackFactory.class)
public interface AuthTokenServiceClient {

    /**
     * 刷新token
     */
    @GetMapping("/refresh-token")
    public ResponsePacket<String> refreshToken(
            @RequestParam(value = "pythonToken", required = true) String pythonToken,
            @RequestParam(value = "version", required = true) String version);
}

package com.kaihei.esportingplus.user.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.feign.AuthTokenServiceClient;
import com.kaihei.esportingplus.user.domain.service.AuthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 鉴权token控制器类
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/29 20:12
 */
@RestController
@RequestMapping("/token")
public class AuthTokenController implements AuthTokenServiceClient {

    @Autowired
    private AuthTokenService authTokenService;

    @Override
    public ResponsePacket<String> refreshToken(
            @RequestParam(value = "pythonToken", required = true) String pythonToken,
            @RequestParam(value = "version", required = true) String version) {
        String token = authTokenService.refreshToken(pythonToken, version);
        return ResponsePacket.onSuccess(token);
    }
}

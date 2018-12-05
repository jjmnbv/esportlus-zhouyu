package com.kaihei.esportingplus.auth.server.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2RequestValidator;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class OauthController{

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @DeleteMapping(value = "/oauth/logout")
    @ResponseBody
    public ResponsePacket revokeToken(String accessToken) {
        if (consumerTokenServices.revokeToken(accessToken)) {
            return ResponsePacket.onSuccess("Token注销成功");
        } else {
            return ResponsePacket.onError(BizExceptionEnum.AUTH_REVOKE_FAIL);
        }
    }

    /**
     * 覆盖了 spring-security-oauth2 内部的 endpoint oauth/token
     * spring-security-oauth2 内部原有的该控制器 TokenEndpoint，入参不支持对象自动封装，故覆盖之。
     */
//    @PostMapping(value = {"/oauth/token"})
//    public ResponseEntity<OAuth2AccessToken> postAccessToken(
//            Principal principal, @RequestBody Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
//        return tokenEndpoint.postAccessToken(principal,parameters);
//    }

}

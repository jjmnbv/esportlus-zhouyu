package com.kaihei.esportingplus.auth.server.common;

import com.kaihei.esportingplus.common.exception.BusinessException;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

@Component("customWebResponseExceptionTranslator")
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        if (e instanceof OAuth2Exception) {
            OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
            LOGGER.error(oAuth2Exception.getMessage()+", Status: " + oAuth2Exception.getHttpErrorCode(),e);
            return ResponseEntity
                    .status(oAuth2Exception.getHttpErrorCode())
                    .body(new CustomOauthException(oAuth2Exception.getMessage()));
        }else if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            LOGGER.error(businessException.getErrMsg()+", Status: " + businessException.getErrCode());
            return ResponseEntity
                    .status(HttpStatus.SC_UNAUTHORIZED)
                    .body(new CustomOauthException(businessException.getErrMsg()));
        }else {
            LOGGER.error(e.getMessage(),e);
            return ResponseEntity
                    .status(HttpStatus.SC_UNAUTHORIZED)
                    .body(new CustomOauthException(e.getMessage()));
        }

    }
}
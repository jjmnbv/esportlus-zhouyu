package com.kaihei.esportingplus.core.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.core.api.params.AuthenticationParam;
import com.kaihei.esportingplus.core.api.params.SmsCredentialParam;
import com.kaihei.esportingplus.core.api.params.SmsSendParam;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author liuyang
 * @Description //TODO
 * @Date 2018/10/24 14:37
 **/
@Component
public class SMSServiceClientFallback implements FallbackFactory<SMSServiceClient> {
    @Override
    public SMSServiceClient create(Throwable throwable) {
        return new SMSServiceClient() {
            @Override
            public ResponsePacket<Boolean> smsSend(SmsSendParam param) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Boolean> authenticationSend(AuthenticationParam param) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Boolean> credential(@RequestBody SmsCredentialParam param) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

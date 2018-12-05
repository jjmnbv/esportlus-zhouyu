package com.kaihei.esportingplus.core.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.core.api.params.MessageCustomParam;
import com.kaihei.esportingplus.core.api.params.MessageSendParam;
import com.kaihei.esportingplus.core.api.params.PushParam;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @Author liuyang
 * @Description
 * @Date 2018/10/29 11:53
 **/
@Component
public class MsgSendServiceClientFallback implements FallbackFactory<MsgSendServiceClient> {
    @Override
    public MsgSendServiceClient create(Throwable throwable) {
        return new MsgSendServiceClient() {
            @Override
            public ResponsePacket<Boolean> send(MessageSendParam messageSendParam) {
                return ResponsePacket.onHystrix(throwable);
            }

//            @Override
//            public ResponsePacket<Boolean> sendCustom(MessageCustomParam messageSendParam) {
//                return ResponsePacket.onHystrix(throwable);
//            }

            @Override
            public ResponsePacket<String> push(PushParam pushParam) {
                return ResponsePacket.onHystrix(throwable);
            }
        };
    }
}

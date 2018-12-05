package com.kaihei.esportingplus.core.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.core.api.params.MPPushParam;
import com.kaihei.esportingplus.core.api.params.MpFormUploadParam;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author liuyang
 * @Date 2018/11/5 14:27
 **/
@Component
public class MpPushServiceClientFallback implements FallbackFactory<MpPushServiceClient> {
    @Override
    public MpPushServiceClient create(Throwable throwable) {
        return new MpPushServiceClient() {
            @Override
            public ResponsePacket<Boolean> push(List<MPPushParam> params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Boolean> upload(MpFormUploadParam params) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

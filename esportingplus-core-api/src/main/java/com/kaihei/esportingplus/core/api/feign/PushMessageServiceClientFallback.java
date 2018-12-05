package com.kaihei.esportingplus.core.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.core.api.params.PushMessageParam;
import com.kaihei.esportingplus.core.api.vo.PageInfo;
import feign.hystrix.FallbackFactory;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: esportingplus
 * @description:
 * @author: xusisi
 * @create: 2018-12-04 16:07
 **/
public class PushMessageServiceClientFallback implements FallbackFactory<PushMessageServiceClient> {

    @Override
    public PushMessageServiceClient create(Throwable throwable) {
        return new PushMessageServiceClient() {
            @Override
            public ResponsePacket createPushMessage(PushMessageParam pushMessageParam) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<PageInfo> getRecords(@RequestParam(value = "page", required = true) Integer page,
                                                       @RequestParam(value = "size", required = true) Integer size) {
                return ResponsePacket.onError();
            }
        };
    }
}

package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.user.api.vo.*;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BaoJiCelebrityServiceClientFallbackFactory implements
        FallbackFactory<BaoJiCelebrityServiceClient> {

    @Override
    public BaoJiCelebrityServiceClient create(Throwable throwable) {
        return new BaoJiCelebrityServiceClient() {

            @Override
            public ResponsePacket<PagingResponse<BaoJiCelebrityVo>> getCelebrityList(Integer game, Integer offset, Integer limit) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

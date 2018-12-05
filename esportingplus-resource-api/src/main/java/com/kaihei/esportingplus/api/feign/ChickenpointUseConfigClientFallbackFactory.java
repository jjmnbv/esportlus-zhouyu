package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.freeteam.ChickenpointUseConfigVO;
import com.kaihei.esportingplus.api.vo.freeteam.DictionaryVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ChickenpointUseConfigClientFallbackFactory implements
        FallbackFactory<ChickenpointUseConfigClient> {

    @Override
    public ChickenpointUseConfigClient create(Throwable throwable) {
        return new ChickenpointUseConfigClient() {
            @Override
            public ResponsePacket<DictionaryVO<ChickenpointUseConfigVO>> getChickenpointUseConfigVo() {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

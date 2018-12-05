package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.freeteam.ChickenpointEvaluateConfigVO;
import com.kaihei.esportingplus.api.vo.freeteam.DictionaryVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ChickenpointEvaluateConfigClientFallbackFactory implements
        FallbackFactory<ChickenpointEvaluateConfigClient> {

    @Override
    public ChickenpointEvaluateConfigClient create(Throwable throwable) {
        return new ChickenpointEvaluateConfigClient() {
            @Override
            public ResponsePacket<List<DictionaryVO<ChickenpointEvaluateConfigVO>>> getChickenpointEvaluateConfigVo() {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

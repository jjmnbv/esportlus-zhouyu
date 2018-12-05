package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.freeteam.FootTabConfigVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FootTabFallbackFactory implements FallbackFactory<FootTabClient> {

    @Override
    public FootTabClient create(Throwable throwable) {
        return new FootTabClient() {


            @Override
            public ResponsePacket<List<?>> findTabsByCategoryCodeForAPP(String token,
                    String categoryCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<?>> findTabsByCategoryCodeWithInsideList(
                    String categoryCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<?>> findTabsByCategoryCode(String categoryCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<?>> batchInsert(String token,
                    List<FootTabConfigVO> footTabConfigs) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<?>> batchUpdate(String token,
                    List<FootTabConfigVO> footTabConfigs) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

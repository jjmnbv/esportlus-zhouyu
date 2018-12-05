package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.freeteam.FootTabConfigVO;
import com.kaihei.esportingplus.api.vo.freeteam.FootTabItemConfigVO;
import com.kaihei.esportingplus.api.vo.freeteam.FootTabItemVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FootTabItemFallbackFactory implements FallbackFactory<FootTabItemClient> {

    @Override
    public FootTabItemClient create(Throwable throwable) {
        return new FootTabItemClient() {

            @Override
            public ResponsePacket<List<?>> findTabsInsideList(String token, String categoryCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<?>> batchInsert(String token,
                    List<FootTabItemVO> footTabItems) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<?>> batchUpdate(String token,
                    List<FootTabItemVO> footTabItems) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<?>> batchConfigUpdate(String token,
                    List<FootTabItemConfigVO> footTabItemConfigs) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

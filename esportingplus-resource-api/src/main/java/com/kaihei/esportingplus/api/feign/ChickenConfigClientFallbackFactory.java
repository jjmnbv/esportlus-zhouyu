package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.ChickenConfigVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ChickenConfigClientFallbackFactory implements
        FallbackFactory<ChickenConfigServiceClient> {

    @Override
    public ChickenConfigServiceClient create(Throwable throwable) {
        return new ChickenConfigServiceClient() {

            @Override
            public ResponsePacket<List<ChickenConfigVo>> findChickConfig() {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<ChickenConfigVo> findChickConfigByType(Integer type) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<ChickenConfigVo> findChickConfigForApp(String token,
                    Integer type) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> saveChickConfig(ChickenConfigVo vo) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

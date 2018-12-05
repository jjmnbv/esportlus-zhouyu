package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.freeteam.ChickenpointTaskConfigVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ChickenpointConfigServiceClientFallbackFactory implements
        FallbackFactory<ChickenpointConfigServiceClient> {


    @Override
    public ChickenpointConfigServiceClient create(Throwable throwable) {
        return new ChickenpointConfigServiceClient() {
            @Override
            public ResponsePacket<Void> saveChickpointTaskConfig(ChickenpointTaskConfigVo vo) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> updateChickpointTaskConfig(ChickenpointTaskConfigVo vo) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<ChickenpointTaskConfigVo>> findChickpointTaskConfig() {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<ChickenpointTaskConfigVo>> findChickpointTaskConfigForApp(
                    String token) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> deleteChickpointTaskConfig(Long id) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

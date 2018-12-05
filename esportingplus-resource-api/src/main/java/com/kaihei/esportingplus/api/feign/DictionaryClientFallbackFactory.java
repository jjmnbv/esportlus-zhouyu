package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.DictBaseVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DictionaryClientFallbackFactory implements FallbackFactory<DictionaryClient> {

    @Override
    public DictionaryClient create(Throwable throwable) {
        return new DictionaryClient() {

            @Override
            public ResponsePacket<DictBaseVO<Object>> findByCodeAndCategoryCode(String categoryCode,
                    String code, Byte status) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<DictBaseVO<Object>>> findByCategoryCode(String categoryCode,
                    Byte status) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<DictBaseVO<Object>> findById(Integer id) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<DictBaseVO<Object>>> findByDictcionayPidAndCategoryCode(
                    Integer pid, String categoryCode) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

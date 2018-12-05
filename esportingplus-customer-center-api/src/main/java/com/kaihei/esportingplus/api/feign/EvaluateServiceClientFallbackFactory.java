package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.params.EvaluateCreateParam;
import com.kaihei.esportingplus.api.params.EvaluateQueryParam;
import com.kaihei.esportingplus.api.vo.EvaluateListVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author yangshidong
 * @date 2018/11/15
 */
@Component
public class EvaluateServiceClientFallbackFactory implements FallbackFactory<EvaluateServiceClient> {
    @Override
    public EvaluateServiceClient create(Throwable throwable) {
        return new EvaluateServiceClient() {
            @Override
            public ResponsePacket submitEvaluate(EvaluateCreateParam evaluateCreateParam) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<EvaluateListVo> queryEvaluateList(EvaluateQueryParam evaluateQueryParam) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

package com.kaihei.esportingplus.payment.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.params.CloudAccountOrderParams;
import com.kaihei.esportingplus.payment.api.vo.CloudAccountDealerInfoVo;
import com.kaihei.esportingplus.payment.api.vo.CloudAccountRespVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class CloudAccountClientFallbackFactory implements FallbackFactory<CloudAccountServiceClient> {

    @Override
    public CloudAccountServiceClient create(Throwable throwable) {

        return new CloudAccountServiceClient() {
            @Override
            public ResponsePacket<CloudAccountRespVo> create(CloudAccountOrderParams orderParams, String appId, String tag,
                                                             HttpServletRequest httpServletRequest) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<CloudAccountDealerInfoVo>> queryAccount(String appId, String tag) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket queryOrder(String appId, String tag, String outTradeNo) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

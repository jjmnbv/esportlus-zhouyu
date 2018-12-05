package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.params.BaojiPricingConfigParams;
import com.kaihei.esportingplus.api.vo.PVPBaojiGameDanIncomeVO;
import com.kaihei.esportingplus.api.vo.PVPBaojiPricingConfigVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 暴鸡计价配置 fallbackFactory
 * @author liangyi
 */
@Component
public class PVPBaojiPricingConfigServiceClientFallbackFactory implements
        FallbackFactory<PVPBaojiPricingConfigServiceClient> {

    @Override
    public PVPBaojiPricingConfigServiceClient create(Throwable throwable) {
        return new PVPBaojiPricingConfigServiceClient() {


            @Override
            public ResponsePacket<PVPBaojiPricingConfigVO> getBaojiPricingConfigDetail(
                    BaojiPricingConfigParams baojiPricingConfigParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<PVPBaojiGameDanIncomeVO>> getBaojiGameDanIncome(
                    BaojiPricingConfigParams baojiPricingConfigParams) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

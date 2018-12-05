package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.params.BaojiDanRangeBackendQueryParams;
import com.kaihei.esportingplus.api.params.BaojiDanRangeBatchParams;
import com.kaihei.esportingplus.api.vo.BaojiDanRangeVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 免费车队配置 fallback
 * @author liangyi
 */
@Component
public class BaojiDanRangeClientFallbackFactory implements FallbackFactory<BaojiDanRangeServiceClient> {

    private static final Logger logger = LoggerFactory
            .getLogger(BaojiDanRangeClientFallbackFactory.class);

    @Override
    public BaojiDanRangeServiceClient create(Throwable throwable) {
        return new BaojiDanRangeServiceClient() {
            @Override
            public ResponsePacket<PagingResponse<BaojiDanRangeVO>> getBaojiDanRangeByPage(
                    BaojiDanRangeBackendQueryParams baojiDanRangeBackendQueryParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> addBaojiDanRangeByGame(
                    BaojiDanRangeBatchParams baojiDanRangeBatchParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> updateBaojiDanRangeByGame(
                    BaojiDanRangeBatchParams baojiDanRangeBatchParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> deleteBaojiDanRange(List<Integer> baojiDanRangeIdList) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

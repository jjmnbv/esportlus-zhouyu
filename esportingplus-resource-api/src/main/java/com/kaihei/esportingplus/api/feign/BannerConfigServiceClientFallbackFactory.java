package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.params.freeteam.BannerSaveParams;
import com.kaihei.esportingplus.api.params.freeteam.BannerUpdateParams;
import com.kaihei.esportingplus.api.vo.freeteam.BannerConfigAppVo;
import com.kaihei.esportingplus.api.vo.freeteam.BannerDictConfigVo;
import com.kaihei.esportingplus.api.vo.freeteam.BannerConfigVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BannerConfigServiceClientFallbackFactory implements
        FallbackFactory<BannerConfigServiceClient> {

    @Override
    public BannerConfigServiceClient create(Throwable throwable) {
        return new BannerConfigServiceClient() {


            @Override
            public ResponsePacket<PagingResponse<BannerConfigVo>> findBannerConfig(Integer userType,
                    String position, Integer offset, Integer limit) {
                return ResponsePacket.onHystrix(throwable);
            }

            @Override
            public ResponsePacket<BannerConfigVo> findBannerConfigByBannerId(Integer bannerId) {
                return ResponsePacket.onHystrix(throwable);
            }

            @Override
            public ResponsePacket<BannerConfigAppVo> findCarouselBannerConfig(String token,
                    Integer userType, String position) {
                return ResponsePacket.onHystrix(throwable);
            }

            @Override
            public ResponsePacket<Void> saveBannerConfig(BannerSaveParams params) {
                return ResponsePacket.onHystrix(throwable);
            }

            @Override
            public ResponsePacket<Void> updateBannerConfig(BannerUpdateParams params) {
                return ResponsePacket.onHystrix(throwable);
            }

            @Override
            public ResponsePacket<Void> deleteBannerConfig(Integer id) {
                return ResponsePacket.onHystrix(throwable);
            }

            @Override
            public ResponsePacket<BannerDictConfigVo> findBannerDictConfigVo() {
                return ResponsePacket.onHystrix(throwable);
            }

            @Override
            public ResponsePacket<Void> updateBannerDictConfigVo(BannerDictConfigVo vo) {
                return ResponsePacket.onHystrix(throwable);
            }
        };
    }
}

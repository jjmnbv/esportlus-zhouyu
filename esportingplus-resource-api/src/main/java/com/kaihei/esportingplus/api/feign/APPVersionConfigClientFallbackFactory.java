package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.APPVersionConfigVO;
import com.kaihei.esportingplus.api.vo.APPVersionDetailConfigVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 客户端版本配置 fallback
 * @author liangyi
 */
@Component
public class APPVersionConfigClientFallbackFactory
        implements FallbackFactory<APPVersionConfigServiceClient> {

    private static final Logger logger = LoggerFactory
            .getLogger(APPVersionConfigClientFallbackFactory.class);

    @Override
    public APPVersionConfigServiceClient create(Throwable throwable) {

        return new APPVersionConfigServiceClient() {

            @Override
            public ResponsePacket<APPVersionConfigVO> getLatestVersion(String x) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<APPVersionDetailConfigVO>> getVersionList() {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> addOrUpdateVersion(
                    APPVersionDetailConfigVO appVersionDetailConfigVO) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<APPVersionConfigVO> getVersionById(Integer id) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> deleteVersionById(Integer id) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.params.freeteam.FreeTeamTypeAppQueryParams;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamTypeVO;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamTypeDetailVO;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamTypeSimpleVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 免费车队类型 fallback
 * @author liangyi
 */
@Component
public class FreeTeamTypeClientFallbackFactory
        implements FallbackFactory<FreeTeamTypeServiceClient> {

    private static final Logger logger = LoggerFactory
            .getLogger(FreeTeamTypeClientFallbackFactory.class);

    @Override
    public FreeTeamTypeServiceClient create(Throwable throwable) {

        return new FreeTeamTypeServiceClient() {

            @Override
            public ResponsePacket<FreeTeamTypeDetailVO> getFreeTeamTypeById(Integer teamTypeId) {
                return ResponsePacket.onHystrix(throwable);
            }

            @Override
            public ResponsePacket<FreeTeamTypeDetailVO> getFreeTeamTypeDetail(
                    String token, FreeTeamTypeAppQueryParams freeTeamTypeAppQueryParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<FreeTeamTypeSimpleVO>> getAllFreeTeamType() {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<FreeTeamTypeSimpleVO>> getAllFreeTeamTypeByBaojiIdentity(
                    String token, FreeTeamTypeAppQueryParams freeTeamTypeAppQueryParams) {
                return ResponsePacket.onHystrix();
            }


            @Override
            public ResponsePacket<Void> addFreeTeamType(FreeTeamTypeVO freeTeamTypeVO) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> updateFreeTeamType(FreeTeamTypeVO freeTeamTypeVO) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> deleteFreeTeamType(Integer freeTeamTypeId) {
                return ResponsePacket.onHystrix();
            }

        };
    }
}

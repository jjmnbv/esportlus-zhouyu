package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.freeteam.DictFreeTeamConfigVO;
import com.kaihei.esportingplus.api.vo.freeteam.TeamUserWhiteListVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingRequest;
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
public class FreeTeamConfigClientFallbackFactory
        implements FallbackFactory<FreeTeamConfigServiceClient> {

    private static final Logger logger = LoggerFactory
            .getLogger(FreeTeamConfigClientFallbackFactory.class);

    @Override
    public FreeTeamConfigServiceClient create(Throwable throwable) {

        return new FreeTeamConfigServiceClient() {

            @Override
            public ResponsePacket<?> getFreeTeamConfig() {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> updateFreeTeamConfig(DictFreeTeamConfigVO dictFreeTeamConfigVO) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PagingResponse<TeamUserWhiteListVO>> getAllFreeTeamUserWhiteList(
                    PagingRequest pagingRequest) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Integer> addBatchFreeTeamUserWhiteList(List<String> uidList) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> deleteFreeTeamUserWhiteList(Integer freeTeamUserWhiteListId) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Boolean> checkUserInFreeTeamUserWhiteList(String uid) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

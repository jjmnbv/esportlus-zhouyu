package com.kaihei.esportingplus.riskrating.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.riskrating.api.vo.FreeTeamResponse;
import com.kaihei.esportingplus.riskrating.api.vo.PageInfo;
import com.kaihei.esportingplus.riskrating.api.vo.TransferBlackListVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class TransferBlackFallbackFactory implements FallbackFactory<TransferBlackClient> {
    @Override
    public TransferBlackClient create(Throwable throwable) {
        return new TransferBlackClient(){
            @Override
            public ResponsePacket<PageInfo<TransferBlackListVo>> getTransferBlackList(String userId, String page, String size) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket insertTransferBlack(TransferBlackListVo transferVo) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket deleteTransferBlack(TransferBlackListVo transferVo) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<FreeTeamResponse> checkTransfer(String userId) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

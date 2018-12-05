package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.params.ComplainCreateParam;
import com.kaihei.esportingplus.api.params.ComplaintQueryParam;
import com.kaihei.esportingplus.api.params.StudioComplainQueryParams;
import com.kaihei.esportingplus.api.vo.ComplaintDetailVo;
import com.kaihei.esportingplus.api.vo.ComplaintListVo;
import com.kaihei.esportingplus.api.vo.StudioComplaintListVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CompaintClientFallbackFactory implements FallbackFactory<CompaintServiceClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompaintClientFallbackFactory.class);

    @Override
    public CompaintServiceClient create(Throwable throwable) {
        return new CompaintServiceClient() {
            @Override
            public ResponsePacket<Void> createComplaint(String token, ComplainCreateParam complainCreateParam) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PagingResponse<ComplaintListVo>> listComplaint(ComplaintQueryParam complainQueryParam) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<ComplaintDetailVo> getComplaintDetail(Integer oid) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PagingResponse<StudioComplaintListVo>> studiolist(
                    StudioComplainQueryParams studioComplainQueryParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<String>> checkOrderBeComplainted(
                    List<String> orderSequeues) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Integer> checkUserBeComplained(String uid) {
                return null;
            }
        };
    }
}

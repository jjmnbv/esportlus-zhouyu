package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.user.api.params.UserPointExchangeParams;
import com.kaihei.esportingplus.user.api.vo.MembersUserPointItemVo;
import com.kaihei.esportingplus.user.api.vo.UserPointItemsQueryVo;
import com.kaihei.esportingplus.user.api.vo.UserPointQueryVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/9 12:10
 */
@Component
public class UserPointServiceClientFallbackFactory implements
        FallbackFactory<UserPointServiceClient> {

    @Override
    public UserPointServiceClient create(Throwable throwable) {
        return new UserPointServiceClient() {
            @Override
            public ResponsePacket<UserPointQueryVo> get(String userId) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Boolean> exchange(UserPointExchangeParams params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PagingResponse<UserPointItemsQueryVo>> listItems(String userId,Integer offset,Integer limit) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<UserPointItemsQueryVo> selectByUserIdAndUid(String slug) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> incrPoint(Integer incrPointAmount, Integer itemType, String slug) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> incrPoint(String uid, Integer incrPointAmount, Integer itemType, String slug) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<MembersUserPointItemVo> incrPoint(Integer userId,
                    Integer incrPointAmount, Integer itemType, Integer operationUserId,
                    String slug) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

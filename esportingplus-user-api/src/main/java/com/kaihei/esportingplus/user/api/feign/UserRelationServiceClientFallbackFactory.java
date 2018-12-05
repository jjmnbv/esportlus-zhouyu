package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.user.api.params.RelationUnreadClearParams;
import com.kaihei.esportingplus.user.api.params.UserFollowParams;
import com.kaihei.esportingplus.user.api.params.UserRelationPageParams;
import com.kaihei.esportingplus.user.api.params.UserRelationUnReadVo;
import com.kaihei.esportingplus.user.api.vo.UserRelationVo;
import feign.hystrix.FallbackFactory;


/**
 * @author liuyang
 * @Description
 * @Date 2018/11/24 14:29
 **/
public class UserRelationServiceClientFallbackFactory implements FallbackFactory<UserRelationServiceClient> {
    @Override
    public UserRelationServiceClient create(Throwable cause) {
        return new UserRelationServiceClient() {
            @Override
            public ResponsePacket<Boolean> follow(UserFollowParams params) {
                return ResponsePacket.onHystrix(cause);
            }

            @Override
            public ResponsePacket<Boolean> unfollow(UserFollowParams params) {
                return ResponsePacket.onHystrix(cause);
            }

            @Override
            public ResponsePacket<Integer> relation(String sourceId, String targetId) {
                return ResponsePacket.onHystrix(cause);
            }

            @Override
            public ResponsePacket<Long> followsCount(String uid) {
                return ResponsePacket.onHystrix(cause);
            }

            @Override
            public ResponsePacket<Long> fansCount(String uid) {
                return ResponsePacket.onHystrix(cause);
            }

            @Override
            public ResponsePacket<Long> friendCount(String uid) {
                return ResponsePacket.onHystrix(cause);
            }

            @Override
            public ResponsePacket<PagingResponse<UserRelationVo>> follows(UserRelationPageParams params) {
                return ResponsePacket.onHystrix(cause);
            }

            @Override
            public ResponsePacket<PagingResponse<UserRelationVo>> fans(UserRelationPageParams page) {
                return ResponsePacket.onHystrix(cause);
            }

            @Override
            public ResponsePacket<PagingResponse<UserRelationVo>> friends(UserRelationPageParams page) {
                return ResponsePacket.onHystrix(cause);
            }

            @Override
            public ResponsePacket<UserRelationUnReadVo> unReadNum(String uid) {
                return ResponsePacket.onHystrix(cause);
            }

            @Override
            public ResponsePacket<Boolean> clearUnread(RelationUnreadClearParams params) {
                return ResponsePacket.onHystrix(cause);
            }
        };
    }
}

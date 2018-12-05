package com.kaihei.esportingplus.core.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.core.api.params.UserBlackParam;
import com.kaihei.esportingplus.core.api.params.UserBlockParam;
import com.kaihei.esportingplus.core.api.params.MsgUserParam;
import com.kaihei.esportingplus.core.api.params.UserTagParam;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author liuyang
 * @Description 用户服务断路器
 * @Date 2018/10/26 11:27
 **/
@Component
public class MsgUserServiceClientFallback implements FallbackFactory<MsgUserServiceClient> {
    @Override
    public MsgUserServiceClient create(Throwable throwable) {
        return new MsgUserServiceClient() {
            @Override
            public ResponsePacket<String> getToken(MsgUserParam param) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<Boolean> updateUser(MsgUserParam param) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<Boolean> blockUser(UserBlockParam blockUserParam) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<Boolean> unBlockUser(List<String> userIds) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<Boolean> addUserToBlacklist(UserBlackParam blackUserParam) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<Boolean> removeUserFromBlacklist(UserBlackParam blackUserParam) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<String[]> queryUsersBlacklist(String uid) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<String> queryRonyunUid(String uid) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Boolean> setTag(UserTagParam userTagParam) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.user.api.params.UpdateAvatarParams;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UserServiceClientFallbackFactory implements
        FallbackFactory<UserServiceClient> {

    @Override
    public UserServiceClient create(Throwable throwable) {
        return new UserServiceClient() {
            @Override
            public ResponsePacket<List<UserSessionContext>> getUserInfosByUids(List<String> uids) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<UserSessionContext> getUserInfosByUid(String uid) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<String> changeAndUpdateAvatar(UpdateAvatarParams params) {
                return ResponsePacket.onError();
            }


            @Override
            public ResponsePacket<String> getAvatarLink(String uid) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<String> getAvatarLinkAndNotifyUpdateAvatar(String uid) {
                return ResponsePacket.onError();
            }
        };
    }
}

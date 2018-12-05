package com.kaihei.esportingplus.core.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.core.api.params.GroupDismissParam;
import com.kaihei.esportingplus.core.api.params.GroupJoinParam;
import com.kaihei.esportingplus.core.api.params.GroupQuitParam;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @Author liuyang
 * @Description
 * @Date 2018/10/26 16:24
 **/
@Component
public class MsgUserGroupServiceClientFallback implements FallbackFactory<MsgUserGroupServiceClient> {
    @Override
    public MsgUserGroupServiceClient create(Throwable throwable) {
        return new MsgUserGroupServiceClient() {
            @Override
            public ResponsePacket<Boolean> createGroup(GroupJoinParam groupCreateParam) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Boolean> joinGroup(GroupJoinParam groupCreateParam) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Boolean> leaveGroup(GroupQuitParam groupQuitParam) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Boolean> dismissGroup(GroupDismissParam groupDismissParam) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

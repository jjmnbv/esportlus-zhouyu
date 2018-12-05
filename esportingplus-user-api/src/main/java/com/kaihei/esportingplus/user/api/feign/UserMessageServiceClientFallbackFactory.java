package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.vo.SendFreeTeamMessageVo;
import com.kaihei.esportingplus.user.api.vo.SendMessageVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author zhaozhenlin
 * @description:
 * @date: 2018/10/10 17:52
 */
@Component
public class UserMessageServiceClientFallbackFactory implements FallbackFactory<UserMessageServiceClient> {

    @Override
    public UserMessageServiceClient create(Throwable cause) {
        return new UserMessageServiceClient() {

            @Override
            public ResponsePacket<Void> sendMessage(SendMessageVo messageVo) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> SendFreeTeamMessage(SendFreeTeamMessageVo messageVo) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

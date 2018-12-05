package com.kaihei.esportingplus.marketing.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.marketing.api.event.CoinConsumeEvent;
import com.kaihei.esportingplus.marketing.api.event.UserRegistEvent;
import com.kaihei.esportingplus.marketing.api.vo.ShareVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-15 15:01
 * @Description:
 */
@Component
public class UserInvitShareServiceClientFallbackFactory implements FallbackFactory<UserInvitShareServiceClient> {

    @Override
    public UserInvitShareServiceClient create(Throwable cause) {
        return new UserInvitShareServiceClient() {

            @Override
            public ResponsePacket shareTask(UserRegistEvent event) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket awardTask(CoinConsumeEvent event) {
                return  ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<ShareVo> sharePoint(String uid, String shareuid, String type) {
                return  ResponsePacket.onHystrix();
            }
        };
    }
}

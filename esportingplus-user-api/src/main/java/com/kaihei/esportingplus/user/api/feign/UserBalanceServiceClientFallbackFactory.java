package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.vo.UserBalanceResutVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;


/**
 * @author zl.zhao
 * @description:
 * @date: 2018/10/22 17:33
 */
@Component
public class UserBalanceServiceClientFallbackFactory implements FallbackFactory<UserBalanceServiceClient> {
    @Override
    public UserBalanceServiceClient create(Throwable throwable) {
        return new UserBalanceServiceClient() {
            @Override
            public ResponsePacket<UserBalanceResutVo> getExchangeAuthority(String uid) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

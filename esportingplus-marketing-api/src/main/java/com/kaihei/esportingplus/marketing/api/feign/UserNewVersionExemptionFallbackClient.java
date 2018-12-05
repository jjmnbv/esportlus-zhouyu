package com.kaihei.esportingplus.marketing.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.marketing.api.event.UserExemptionEvent;
import com.kaihei.esportingplus.marketing.api.vo.ExemptionTaskVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author zl.zhao
 * @description:
 * @date: 2018/10/25 17:44
 */
@Component
public class UserNewVersionExemptionFallbackClient implements FallbackFactory<UserNewVersionExemptionClient> {
    @Override
    public UserNewVersionExemptionClient create(Throwable throwable) {
        return new UserNewVersionExemptionClient() {
            @Override
            public ResponsePacket<ExemptionTaskVo> exemptionTask(UserExemptionEvent event) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

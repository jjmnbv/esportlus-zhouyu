package com.kaihei.esportingplus.payment.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.vo.AccountInfoVo;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @program: esportingplus
 * @description: 附件服务熔断
 * @author: xusisi
 * @create: 2018-09-30 16:27
 **/
@Component
public class AttachClientFallbackFactory implements FallbackFactory<AttachServiceClient> {


    private static final Logger logger = LoggerFactory.getLogger(AttachClientFallbackFactory.class);

    @Override
    public AttachServiceClient create(Throwable throwable) {
        return new AttachServiceClient() {
            @Override
            public ResponsePacket<AccountInfoVo> checkAccount(String userId, String accountType, Integer amount) throws Exception {
                return ResponsePacket.onHystrix();
            }
        };
    }
}

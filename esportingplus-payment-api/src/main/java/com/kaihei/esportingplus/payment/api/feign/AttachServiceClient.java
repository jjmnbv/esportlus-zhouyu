package com.kaihei.esportingplus.payment.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.vo.AccountInfoVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: esportingplus
 * @description: 附件服务接口
 * @author: xusisi
 * @create: 2018-09-30 16:07
 **/
@FeignClient(name = "esportingplus-payment-service", path = "/attach", fallbackFactory = AttachClientFallbackFactory.class)
public interface AttachServiceClient {

    @RequestMapping(value = "/account/{userId}", method = RequestMethod.GET)
    public ResponsePacket<AccountInfoVo> checkAccount(@PathVariable String userId, @RequestParam String accountType, @RequestParam Integer amount) throws Exception;

}

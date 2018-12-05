package com.kaihei.esportingplus.trade.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.vo.ExternalPaymentOrderVo;
import com.kaihei.esportingplus.payment.api.vo.ExternalRefundOrderVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 基于feign实现远程车队服务接口调用<br/> 1. esportingplus-trade-service为服务名 <br/> 2.
 * fallbackFactory指定断路器实现类<br/>
 */
@FeignClient(name = "esportingplus-trade-service", path = "/pay", fallbackFactory = PayClientFallbackFactory.class)
public interface PayClient {

    @PostMapping("/payNofity")
    ResponsePacket payNofity(@RequestBody ExternalPaymentOrderVo payPacket);

    @PostMapping("/refundNofity")
    ResponsePacket refundNofity(@RequestBody ExternalRefundOrderVo refundPacket);

}

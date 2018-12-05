package com.kaihei.esportingplus.payment.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.params.GCoinRefundParams;
import com.kaihei.esportingplus.payment.api.vo.GCoinRefundPreVo;
import com.kaihei.esportingplus.payment.api.vo.GCoinRefundVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: esportingplus
 * @description: 暴鸡币退款
 * @author: xusisi
 * @create: 2018-09-25 10:13
 **/

/**
 * 基于feign实现远程客服服务接口调用<br/> 1. esportingplus-customer-center-service为服务名 <br/> 2.
 * fallbackFactory指定断路器实现类<br/>
 */
@FeignClient(name = "esportingplus-payment-service", path = "/gcoin", fallbackFactory = GCoinRefundClientFallbackFactory.class)
public interface GCoinRefundServiceClient {


    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public ResponsePacket<GCoinRefundPreVo> createOrderInfo(@RequestBody GCoinRefundParams gcoinRefundParams);


    @RequestMapping(value = "/refund", method = RequestMethod.GET)
    public ResponsePacket<GCoinRefundVo> getRefundInfo(@RequestParam(required = false) String orderId, @RequestParam(required = false) String orderType, @RequestParam(required = false) String outRefundNo);
}

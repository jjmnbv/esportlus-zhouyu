package com.kaihei.esportingplus.payment.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.params.GCoinRechargeCreateParams;
import com.kaihei.esportingplus.payment.api.params.GCoinRechargeUpdateParams;
import com.kaihei.esportingplus.payment.api.vo.GCoinRechargePreVo;
import com.kaihei.esportingplus.payment.api.vo.GCoinRechargeVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: esportingplus
 * @description: 暴鸡币充值
 * @author: xusisi
 * @create: 2018-09-25 10:13
 **/

/**
 * 基于feign实现远程客服服务接口调用<br/> 1. esportingplus-customer-center-service为服务名 <br/> 2.
 * fallbackFactory指定断路器实现类<br/>
 */
@FeignClient(name = "esportingplus-payment-service", path = "/gcoin", fallbackFactory = GCoinRechargeClientFallbackFactory.class)
public interface GCoinRechargeServiceClient {


    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    public ResponsePacket<GCoinRechargePreVo> createOrderInfo(@RequestBody GCoinRechargeCreateParams rechargeCreateParams);


    @RequestMapping(value = "/recharge/{orderId}", method = RequestMethod.PUT)
    public ResponsePacket<GCoinRechargeVo> updateOrderInfo(@PathVariable String orderId, @RequestBody GCoinRechargeUpdateParams rechargeUpdateParams);


    @RequestMapping(value = "/recharge/{orderId}", method = RequestMethod.GET)
    public ResponsePacket<GCoinRechargeVo> getRechargeDetail(@PathVariable String orderId, @RequestParam String userId);
}

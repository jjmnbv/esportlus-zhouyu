package com.kaihei.esportingplus.payment.api.feign;


import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.params.GCoinPaymentCreateParams;
import com.kaihei.esportingplus.payment.api.params.GCoinPaymentUpdateParams;
import com.kaihei.esportingplus.payment.api.vo.GCoinPaymentPreVo;
import com.kaihei.esportingplus.payment.api.vo.GCoinPaymentVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 基于feign实现远程客服服务接口调用<br/> 1. esportingplus-customer-center-service为服务名 <br/> 2.
 * fallbackFactory指定断路器实现类<br/>
 */
@FeignClient(name = "esportingplus-payment-service", path = "/gcoin", fallbackFactory = GCoinPaymentClientFallbackFactory.class)
public interface GCoinPaymentServiceClient {


    /**
     * @Description:创建支付订单
     */
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    ResponsePacket<GCoinPaymentPreVo> createPrePaymentInfo(@RequestBody GCoinPaymentCreateParams gcoinPaymentCreateParams);


    /**
     * @Description:更新支付订单
     */
    @RequestMapping(value = "/payment/{orderId}", method = RequestMethod.PUT)
    ResponsePacket<GCoinPaymentVo> updatePaymentInfo(@PathVariable(name = "orderId") String orderId, @RequestBody GCoinPaymentUpdateParams gcoinPaymentUpdateParams);

    /**
     * @Description:查询支付详情
     */
    @RequestMapping(value = "/payment", method = RequestMethod.GET)
    ResponsePacket<GCoinPaymentVo> getPaymentInfo(@RequestParam(required = false) String orderType, @RequestParam(required = false) String orderId, @RequestParam(required = false) String outTradeNo);


}

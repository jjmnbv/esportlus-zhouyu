package com.kaihei.esportingplus.payment.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.params.CloseOrCancelPayOrderParams;
import com.kaihei.esportingplus.payment.api.params.PayOrderParams;
import com.kaihei.esportingplus.payment.api.params.RefundOrderParams;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 暴鸡支付服务API
 *
 * @author haycco
 */
@FeignClient(name = "esportingplus-payment-service",
        path = "/trade", fallbackFactory = TradeClientFallbackFactory.class)
public interface TradeServiceClient {

    /***
     * 创建支付订单
     * eg : ..../trade/pay?appId={appId}&channelTag={channelTag}
     */
    @PostMapping("/pay")
    ResponsePacket<Map<String, String>> create(@RequestBody PayOrderParams payOrderParams,
                                               @RequestParam(value = "appId", required = true) String appId,
                                               @RequestParam(value = "channelTag", required = true) String channelTag);

    /**
     * 取消支付订单
     * eg : ..../trade/cancel
     */
    @PutMapping("/cancel")
    ResponsePacket cancel(@RequestBody CloseOrCancelPayOrderParams closeOrCancelPayOrderParams);

    /**
     * 关闭支付订单
     * eg : ..../trade/close
     */
    @PutMapping("/close")
    ResponsePacket close(@RequestBody CloseOrCancelPayOrderParams closeOrCancelPayOrderParams);

    /**
     * 交易查询
     * eg : ..../trade/query/{outTradeNo}?orderType={orderType}
     */
    @GetMapping("query/{outTradeNo}")
    ResponsePacket query(@PathVariable("outTradeNo") String outTradeNo,
                         @RequestParam(value = "orderType", required = true) String orderType);

    /***
     * 交易退款 - 创建退款订单
     * eg: ..../trade/refund
     */
    @PostMapping("/refund")
    ResponsePacket refund(@RequestBody RefundOrderParams refundOrderParams);

    /**
     * 交易退款查询
     * eg: ..../trade/refund/{outRefundNo}
     */
    @GetMapping("/refund/{outRefundNo}")
    ResponsePacket refundQuery(@PathVariable("outRefundNo") String outRefundNo);

    /**
     * eg: /payTest?appId={appId}&channelTag={channelTag}
     *
     * @param : [payOrderParams, appId, channelTag]
     * @Author : xusisi
     **/
    @PostMapping("/payTest")
    Object createTest(@RequestBody PayOrderParams payOrderParams,
                      @RequestParam(value = "appId", required = true) String appId,
                      @RequestParam(value = "channelTag", required = true) String channelTag);

}

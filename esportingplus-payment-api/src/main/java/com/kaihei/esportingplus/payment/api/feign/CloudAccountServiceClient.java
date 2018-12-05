package com.kaihei.esportingplus.payment.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.params.CloudAccountOrderParams;
import com.kaihei.esportingplus.payment.api.vo.CloudAccountDealerInfoVo;
import com.kaihei.esportingplus.payment.api.vo.CloudAccountRespVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 云账户提现Feign类
 * @author chenzhenjun
 */
@FeignClient(name = "esportingplus-payment-service",
        path = "/cloud", fallbackFactory = CloudAccountClientFallbackFactory.class)
public interface CloudAccountServiceClient {

    /**
     * 创建提现订单
     * @param orderParams
     * @param appId
     * @param tag
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/pay/{appId}/{tag}")
    public ResponsePacket<CloudAccountRespVo> create(@RequestBody CloudAccountOrderParams orderParams,
                                                     @PathVariable String appId, @PathVariable String tag,
                                                     HttpServletRequest httpServletRequest);

    /**
     * 查询商户余额
     * @param appId
     * @param tag
     * @return
     */
    @GetMapping("/account/{appId}/{tag}")
    public ResponsePacket<List<CloudAccountDealerInfoVo>> queryAccount(@PathVariable String appId, @PathVariable String tag);

    /**
     * 主动查询订单
     * @param appId
     * @param tag
     * @param outTradeNo
     * @return
     */
    @GetMapping("/order/{appId}/{tag}")
    public ResponsePacket queryOrder(@PathVariable String appId, @PathVariable String tag,
            @RequestParam(value = "out_trade_no", required = true) String outTradeNo);

}

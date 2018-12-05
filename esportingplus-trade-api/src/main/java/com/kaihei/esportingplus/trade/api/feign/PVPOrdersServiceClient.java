package com.kaihei.esportingplus.trade.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.trade.api.params.PVPInComeParams;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 基于feign实现远程车队服务接口调用<br/> 1. esportingplus-trade-service为服务名 <br/> 2.
 * fallbackFactory指定断路器实现类<br/>
 */
@FeignClient(name = "esportingplus-trade-service", path = "/orders/pvp", fallbackFactory = PVPOrdersClientFallbackFactory.class)
public interface PVPOrdersServiceClient {

    /**
     *@Description: 校验队员(老板)是否已支付
     *@param: [order_sequence]
     *@return: com.kaihei.esportingplus.common.ResponsePacket<java.lang.Void>
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/8/8 20:15
     */
    @GetMapping("/order/checkTeamMemberPayed/{orderSequence}")
    ResponsePacket checkTeamMemberPayed(@PathVariable("orderSequence") String orderSequence);

    /**
     *@Description: 预计收益
     *@param: []
     *@return: com.kaihei.esportingplus.common.ResponsePacket
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/11/26 10:59
    */
    @PostMapping("/order/preIncome")
    ResponsePacket preIncome(@RequestBody PVPInComeParams pvpInComeParams);
}

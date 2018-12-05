package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.params.BaojiPricingConfigParams;
import com.kaihei.esportingplus.api.vo.PVPBaojiGameDanIncomeVO;
import com.kaihei.esportingplus.api.vo.PVPBaojiPricingConfigVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <pre>
 *  基于 feign 实现远程服务接口调用
 *  1. esportingplus-resource-service为服务名
 *  2. fallbackFactory指定断路器实现类<br/>
 *  -- 暴鸡计价配置 api
 * </pre>
 * @author liangyi
 */
@FeignClient(name = "esportingplus-resource-service",
        path = "/baoji/pricing/config",
        fallbackFactory = PVPBaojiPricingConfigServiceClientFallbackFactory.class)
public interface PVPBaojiPricingConfigServiceClient {


    /**
     * 查询暴鸡计价配置
     * @param baojiPricingConfigParams
     * @return
     */
    @PostMapping("/detail")
    ResponsePacket<PVPBaojiPricingConfigVO> getBaojiPricingConfigDetail(
            @RequestBody BaojiPricingConfigParams baojiPricingConfigParams);

    /**
     * 内部服务调用--查询暴鸡计价配置
     * @param baojiPricingConfigParams
     * @return
     */
    @PostMapping("/income")
    ResponsePacket<List<PVPBaojiGameDanIncomeVO>> getBaojiGameDanIncome(
            @RequestBody BaojiPricingConfigParams baojiPricingConfigParams);


}

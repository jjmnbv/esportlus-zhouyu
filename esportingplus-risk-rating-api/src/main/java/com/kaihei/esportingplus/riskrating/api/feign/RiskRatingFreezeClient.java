package com.kaihei.esportingplus.riskrating.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.riskrating.api.params.UserRechargeFreezeParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "esportingplus-risk-rating-service",
        path = "/freezz", fallbackFactory = RiskRatingFreezeFallbackFactory.class)
public interface RiskRatingFreezeClient {

    @PostMapping
    ResponsePacket<Boolean> freezeUserRecharge(@RequestBody
            UserRechargeFreezeParams userRechargeFreezeParams);

    @ApiOperation("解除用户充值冻结")
    @DeleteMapping("{uid}")
    ResponsePacket<Boolean> unfreezeUserRecharge(@PathVariable String uid);
}

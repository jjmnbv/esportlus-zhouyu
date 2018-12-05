package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.vo.UserBalanceResutVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zl.zhao
 * @description: 用户暴击值控制Feign
 * @date: 2018/10/22 17:31
 */
@FeignClient(name = "esportingplus-user-service", path = "/balance", fallbackFactory = UserBalanceServiceClientFallbackFactory.class)
public interface UserBalanceServiceClient {

    /**
     * 获取用户暴击值兑换权限
     * @param uid
     * @return
     */
    @GetMapping("/exchange/authority")
    public ResponsePacket<UserBalanceResutVo> getExchangeAuthority(@RequestParam(value = "uid", required = true) String uid);
}

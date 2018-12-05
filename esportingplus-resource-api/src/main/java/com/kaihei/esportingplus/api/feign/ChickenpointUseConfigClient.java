package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.freeteam.ChickenpointUseConfigVO;
import com.kaihei.esportingplus.api.vo.freeteam.DictionaryVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "esportingplus-resource-service", path = "dictionary", fallbackFactory = ChickenpointUseConfigClientFallbackFactory.class)
public interface ChickenpointUseConfigClient {

    /**
     * 鸡分使用配置
     */
    @GetMapping("/category/chicken_use_config/dictionary/chicken_use_config_value")
    ResponsePacket<DictionaryVO<ChickenpointUseConfigVO>> getChickenpointUseConfigVo();

}

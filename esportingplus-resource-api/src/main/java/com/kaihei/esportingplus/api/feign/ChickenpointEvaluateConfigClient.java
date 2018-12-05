package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.freeteam.ChickenpointEvaluateConfigVO;
import com.kaihei.esportingplus.api.vo.freeteam.DictionaryVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 获取鸡分评价配置
 */
@FeignClient(name = "esportingplus-resource-service", path = "dictionary")
public interface ChickenpointEvaluateConfigClient {

    /**
     * 获取鸡分评价配置
     */
    @GetMapping("category/chicken_evaluate_gainpoint_config")
    ResponsePacket<List<DictionaryVO<ChickenpointEvaluateConfigVO>>> getChickenpointEvaluateConfigVo();
}

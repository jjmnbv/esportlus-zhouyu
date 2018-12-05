package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.ChickenConfigVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "esportingplus-resource-service",
        path = "/chicken/config", fallbackFactory = ChickenConfigClientFallbackFactory.class)
public interface ChickenConfigServiceClient {

    /**
     * 获取全部小鸡配置
     */
    @GetMapping("/all")
    public ResponsePacket<List<ChickenConfigVo>> findChickConfig();

    /**
     * 根据类型获取小鸡配置
     */
    @GetMapping("/type/{type}")
    public ResponsePacket<ChickenConfigVo> findChickConfigByType(
            @PathVariable("type") Integer type);

    /**
     * APP调用-获取小鸡配置
     */
    @GetMapping("/app")
    public ResponsePacket<ChickenConfigVo> findChickConfigForApp(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(value = "type", required = true) Integer type);

    /**
     * 保存小鸡配置
     */
    @PostMapping("/save")
    public ResponsePacket<Void> saveChickConfig(@RequestBody ChickenConfigVo vo);
}

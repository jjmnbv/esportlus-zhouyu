package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.freeteam.FootTabItemConfigVO;
import com.kaihei.esportingplus.api.vo.freeteam.FootTabItemVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "esportingplus-resource-service", path = "foottabitem", fallbackFactory = FootTabFallbackFactory.class)
public interface FootTabItemClient {

    @ApiOperation("通过分类Code查询底部所有tab的所有服务配置")
    @GetMapping("/category/{categoryCode}/insideList")
    ResponsePacket<List<?>> findTabsInsideList(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable String categoryCode);

    @ApiOperation("批量插入底部Tab内部服务")
    @PostMapping("/inside/batchInsert")
    ResponsePacket<List<?>> batchInsert(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody List<FootTabItemVO> footTabItems);

    @ApiOperation("批量更新底部Tab内部服务")
    @PostMapping("/inside/batchUpdate")
    ResponsePacket<List<?>> batchUpdate(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody List<FootTabItemVO> footTabItems);

    @ApiOperation("批量更新底部Tab内部服务内容配置")
    @PostMapping("/inside/config/batchUpdate")
    ResponsePacket<List<?>> batchConfigUpdate(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody List<FootTabItemConfigVO> footTabItemConfigs);
}

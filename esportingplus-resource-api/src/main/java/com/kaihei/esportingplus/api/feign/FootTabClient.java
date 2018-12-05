package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.freeteam.FootTabConfigVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import io.swagger.annotations.ApiOperation;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "esportingplus-resource-service", path = "foottab", fallbackFactory = FootTabFallbackFactory.class)
public interface FootTabClient {

    @ApiOperation("APP调用-通过分类Code查询底部所有tab,并且带tab下的所有服务配置")
    @GetMapping("category/{categoryCode}")
    ResponsePacket<List<?>> findTabsByCategoryCodeForAPP(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable String categoryCode);

    @ApiOperation("后台管理调用-通过分类Code查询底部所有tab,并且带tab下的所有服务配置")
    @GetMapping("category/{categoryCode}/tabsWithInsideList")
    ResponsePacket<List<?>> findTabsByCategoryCodeWithInsideList(@PathVariable String categoryCode);

    @ApiOperation("后台管理调用-通过分类Code单独查询底部所有tab,并且不带tab下的服务配置")
    @GetMapping("/category/{categoryCode}/tabsWithoutInsideList")
    ResponsePacket<List<?>> findTabsByCategoryCode(@PathVariable String categoryCode);

    @ApiOperation("批量插入底部Tab")
    @PostMapping("/batchInsert")
    ResponsePacket<List<?>> batchInsert(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody List<FootTabConfigVO> footTabConfigs);

    @ApiOperation("批量更新底部Tab")
    @PostMapping("/batchUpdate")
    ResponsePacket<List<?>> batchUpdate(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody List<FootTabConfigVO> footTabConfigs);
}

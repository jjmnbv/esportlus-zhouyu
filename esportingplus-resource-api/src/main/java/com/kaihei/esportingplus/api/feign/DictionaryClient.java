package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.DictBaseVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "esportingplus-resource-service", path = "dictionary", fallbackFactory = DictionaryClientFallbackFactory.class)
public interface DictionaryClient {

    @ApiOperation("通过分类Code和字典Code查询字典")
    @GetMapping("category/{categoryCode}/dictionary/{code}")
    ResponsePacket<DictBaseVO<Object>> findByCodeAndCategoryCode(
            @PathVariable("categoryCode") String categoryCode,
            @PathVariable("code") String code,
            @RequestParam("status") Byte status);

    @ApiOperation("通过分类Code查询字典")
    @GetMapping("category/{categoryCode}")
    ResponsePacket<List<DictBaseVO<Object>>> findByCategoryCode(
            @PathVariable("categoryCode") String categoryCode,
            @RequestParam("status") Byte status);

    @ApiOperation("通过id查询字典")
    @GetMapping("{id}")
    ResponsePacket<DictBaseVO<Object>> findById(@PathVariable("id") Integer id);

    @ApiOperation("通过字典父Id 字典分类Code查询字典")
    @GetMapping("dictionaryparent/{pid}/category/{categoryCode}")
    ResponsePacket<List<DictBaseVO<Object>>> findByDictcionayPidAndCategoryCode(
            @PathVariable("pid") Integer pid, @PathVariable("categoryCode") String categoryCode);
}

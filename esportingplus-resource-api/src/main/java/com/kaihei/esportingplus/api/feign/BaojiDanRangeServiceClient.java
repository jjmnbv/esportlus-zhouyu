package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.params.BaojiDanRangeBackendQueryParams;
import com.kaihei.esportingplus.api.params.BaojiDanRangeBatchParams;
import com.kaihei.esportingplus.api.vo.BaojiDanRangeVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <pre>
 *  基于 feign 实现远程服务接口调用
 *  1. esportingplus-resource-service为服务名
 *  2. fallbackFactory指定断路器实现类<br/>
 *  -- 暴鸡接单范围 api
 * </pre>
 * @author liangyi
 */
@FeignClient(name = "esportingplus-resource-service",
        path = "/baoji/dan/range/pvp", fallbackFactory = BaojiDanRangeClientFallbackFactory.class)
public interface BaojiDanRangeServiceClient {


    /**
     * Python后台调用--分页查询所有暴鸡接单范围
     * @param baojiDanRangeBackendQueryParams
     * @return
     */
    @PostMapping("/backend/page")
    ResponsePacket<PagingResponse<BaojiDanRangeVO>> getBaojiDanRangeByPage(
            @RequestBody BaojiDanRangeBackendQueryParams baojiDanRangeBackendQueryParams);

    /**
     * 新增暴鸡接单范围
     * @param baojiDanRangeBatchParams 暴鸡接单范围参数
     * @return
     */
    @PostMapping("/backend/add")
    ResponsePacket<Void> addBaojiDanRangeByGame(
            @RequestBody BaojiDanRangeBatchParams baojiDanRangeBatchParams);

    /**
     * 修改暴鸡接单范围
     * @param baojiDanRangeBatchParams 暴鸡接单范围参数
     * @return
     */
    @PostMapping("/backend/update")
    ResponsePacket<Void> updateBaojiDanRangeByGame(
            @RequestBody BaojiDanRangeBatchParams baojiDanRangeBatchParams);

    /**
     * 删除暴鸡接单范围
     * @param baojiDanRangeIdList 暴鸡接单范围 id 集合
     * @return
     */
    @PostMapping("/backend/delete")
    ResponsePacket<Void> deleteBaojiDanRange(
            @RequestBody List<Integer> baojiDanRangeIdList);

}

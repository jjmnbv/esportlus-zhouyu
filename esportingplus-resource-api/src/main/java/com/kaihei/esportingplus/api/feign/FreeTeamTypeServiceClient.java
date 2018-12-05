package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.params.freeteam.FreeTeamTypeAppQueryParams;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamTypeVO;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamTypeDetailVO;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamTypeSimpleVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * <pre>
 *  基于feign实现远程车队服务接口调用
 *  1. esportingplus-resource-service为服务名
 *  2. fallbackFactory指定断路器实现类<br/>
 *  -- 免费车队类型相关 api
 * </pre>
 * @author liangyi
 */
@FeignClient(name = "esportingplus-resource-service",
        path = "/freeteam/type/pvp", fallbackFactory = FreeTeamTypeClientFallbackFactory.class)
public interface FreeTeamTypeServiceClient {

    /**
     * 管理后台调用--根据 id 查询免费车队类型
     * @param freeTeamTypeId
     * @return
     */
    @GetMapping("/backend/{freeTeamTypeId}")
    ResponsePacket<FreeTeamTypeDetailVO> getFreeTeamTypeById(
            @PathVariable("freeTeamTypeId") Integer freeTeamTypeId);

    /**
     * APP调用--根据 id 查询免费车队类型
     * @param token
     * @param freeTeamTypeAppQueryParams
     * @return
     */
    @PostMapping("/detail")
    ResponsePacket<FreeTeamTypeDetailVO> getFreeTeamTypeDetail(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody FreeTeamTypeAppQueryParams freeTeamTypeAppQueryParams);

    /**
     * 管理后台调用--查询所有免费车队类型
     * @return
     */
    @GetMapping("/backend/all")
    ResponsePacket<List<FreeTeamTypeSimpleVO>> getAllFreeTeamType();

    /**
     * APP调用--根据用户身份查询所有免费车队类型
     * @param token
     * @param freeTeamTypeAppQueryParams
     * @return
     */
    @PostMapping("/list")
    ResponsePacket<List<FreeTeamTypeSimpleVO>> getAllFreeTeamTypeByBaojiIdentity(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody FreeTeamTypeAppQueryParams freeTeamTypeAppQueryParams);

    /**
     * 管理后台调用--新增免费车队类型
     * @param freeTeamTypeVO
     * @return
     */
    @PostMapping("/backend/add")
    ResponsePacket<Void> addFreeTeamType(@RequestBody FreeTeamTypeVO freeTeamTypeVO);

    /**
     * 管理后台调用--修改免费车队类型
     * @param freeTeamTypeVO
     * @return
     */
    @PostMapping("/backend/update")
    ResponsePacket<Void> updateFreeTeamType(@RequestBody FreeTeamTypeVO freeTeamTypeVO);

    /**
     * 管理后台调用--根据 id删除车队类型, 其实是下架处理
     * @param freeTeamTypeId
     * @return
     */
    @GetMapping("/backend/delete/{freeTeamTypeId}")
    ResponsePacket<Void> deleteFreeTeamType(@PathVariable("freeTeamTypeId") Integer freeTeamTypeId);

}

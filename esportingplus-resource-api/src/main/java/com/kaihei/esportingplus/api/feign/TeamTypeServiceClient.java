package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.params.freeteam.TeamTypeAppQueryParams;
import com.kaihei.esportingplus.api.params.freeteam.TeamTypeBackendQueryParams;
import com.kaihei.esportingplus.api.params.freeteam.TeamTypeParams;
import com.kaihei.esportingplus.api.vo.freeteam.CreateTeamGameTeamTypeVO;
import com.kaihei.esportingplus.api.vo.freeteam.TeamTypeDetailVO;
import com.kaihei.esportingplus.api.vo.freeteam.TeamTypeSimpleVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import io.swagger.annotations.ApiParam;
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
 *  -- 车队类型相关 api
 * </pre>
 * @author liangyi
 */
@FeignClient(name = "esportingplus-resource-service",
        path = "/teamtype", fallbackFactory = TeamTypeClientFallbackFactory.class)
public interface TeamTypeServiceClient {

    /**
     * Python后台调用--根据 id 查询车队类型
     * @param teamTypeId 车队类型 id
     * @return
     */
    @GetMapping("/teamgame/{teamTypeId}")
    ResponsePacket<CreateTeamGameTeamTypeVO> getCreateTeamGameTeamTypeVOById(
            @PathVariable("teamTypeId") Integer teamTypeId);

    /**
     * Python后台调用--根据 id 查询车队类型
     *
     * @param teamTypeId 车队类型 id
     */
    @GetMapping("/backend/{teamTypeId}")
    ResponsePacket<TeamTypeDetailVO> getTeamTypeById(
            @ApiParam(value = "车队类型id", required = true)
            @PathVariable("teamTypeId") Integer teamTypeId);
    /**
     * APP调用--根据 id 查询车队类型
     * @param token
     * @param teamTypeAppQueryParams 类型 app 端查询参数
     * @return
     */
    @PostMapping("/detail")
    ResponsePacket<TeamTypeDetailVO> getTeamTypeDetail(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody TeamTypeAppQueryParams teamTypeAppQueryParams);

    /**
     * Python后台调用--分页查询所有车队类型
     * @param teamTypeBackendQueryParams
     * @return
     */
    @PostMapping("/backend/page")
    ResponsePacket<PagingResponse<TeamTypeSimpleVO>> getTeamTypeByPage(
            @RequestBody TeamTypeBackendQueryParams teamTypeBackendQueryParams);

    /**
     * APP调用--查询所有车队类型
     * @param token
     * @param teamTypeAppQueryParams
     * @return
     */
    @PostMapping("/list")
    ResponsePacket<List<TeamTypeSimpleVO>> getAllTeamTypeByBaojiIdentity(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody TeamTypeAppQueryParams teamTypeAppQueryParams);

    /**
     * 新增车队类型
     * @param teamTypeParams 车队类型参数
     * @return
     */
    @PostMapping("/backend/add")
    ResponsePacket<Void> addTeamType(@RequestBody TeamTypeParams teamTypeParams);

    /**
     * 修改车队类型
     * @param teamTypeParams 车队类型参数
     * @return
     */
    @PostMapping("/backend/update")
    ResponsePacket<Void> updateTeamType(@RequestBody TeamTypeParams teamTypeParams);

    /**
     * 删除车队类型
     * @param teamTypeId 车队类型 id
     * @return
     */
    @GetMapping("/backend/delete/{teamTypeId}")
    ResponsePacket<Void> deleteTeamType(@PathVariable("teamTypeId") Integer teamTypeId);

}

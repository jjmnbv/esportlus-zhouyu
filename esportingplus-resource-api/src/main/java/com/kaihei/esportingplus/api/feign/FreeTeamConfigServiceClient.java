package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.freeteam.DictFreeTeamConfigVO;
import com.kaihei.esportingplus.api.vo.freeteam.TeamUserWhiteListVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingRequest;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <pre>
 *  基于feign实现远程车队服务接口调用
 *  1. esportingplus-resource-service为服务名
 *  2. fallbackFactory指定断路器实现类<br/>
 *  -- 免费车队配置相关 api
 * </pre>
 * @author liangyi
 */
@FeignClient(name = "esportingplus-resource-service",
        path = "/freeteam", fallbackFactory = FreeTeamConfigClientFallbackFactory.class)
public interface FreeTeamConfigServiceClient {

    /**
     * 获取免费车队配置
     * @return
     */
    @GetMapping("/pvp/config/backend/info")
    ResponsePacket<?> getFreeTeamConfig();

    /**
     * 更新免费车队配置
     * @param dictFreeTeamConfigVO
     * @return
     */
    @PostMapping("/pvp/config/backend/update")
    ResponsePacket<Void> updateFreeTeamConfig(
            @RequestBody DictFreeTeamConfigVO dictFreeTeamConfigVO);

    /**
     * 分页查询所有白名单用户
     * @param pagingRequest
     * @return
     */
    @PostMapping("/userWhiteList/backend/page")
    ResponsePacket<PagingResponse<TeamUserWhiteListVO>> getAllFreeTeamUserWhiteList(
            @RequestBody PagingRequest pagingRequest);

    /**
     * 批量添加用户 uid 加入白名单
     * @param uidList
     * @return
     */
    @PostMapping("/userWhiteList/backend/add")
    ResponsePacket<Integer> addBatchFreeTeamUserWhiteList(@RequestBody List<String> uidList);

    /**
     * 从白名单中删除单个用户
     * @param freeTeamUserWhiteListId
     * @return
     */
    @GetMapping("/userWhiteList/backend/delete/{freeTeamUserWhiteListId}")
    ResponsePacket<Void> deleteFreeTeamUserWhiteList(
            @PathVariable("freeTeamUserWhiteListId") Integer freeTeamUserWhiteListId);

    /**
     * 校验用户是否在免费车队白名单中
     * @param uid 要校验的用户 uid
     * @return
     */
    @GetMapping("/userWhiteList/backend/check/{uid}")
    ResponsePacket<Boolean> checkUserInFreeTeamUserWhiteList(@PathVariable("uid") String uid);
}

package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.user.api.params.UserPointExchangeParams;
import com.kaihei.esportingplus.user.api.vo.MembersUserPointItemVo;
import com.kaihei.esportingplus.user.api.vo.UserPointItemsQueryVo;
import com.kaihei.esportingplus.user.api.vo.UserPointQueryVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 用户鸡分服务client接口
 *
 * @author xiekeqing
 * @date 2018/10/9 11:29
 * @version: 1.0
 */

@FeignClient(name = "esportingplus-user-service", path = "/point", fallbackFactory = UserPointServiceClientFallbackFactory.class)
public interface UserPointServiceClient {

    /**
     * 查询用户鸡分
     *
     * @param uid 用户ID
     * @return ResponsePacket<UserPointQueryVo>
     */
    @GetMapping("/{uid}")
    ResponsePacket<UserPointQueryVo> get(@PathVariable("uid") String uid);

    /**
     * 鸡分兑换暴击值
     *
     * @param params 兑换鸡分参数
     * @return ResponsePacket<Void>
     */
    @PostMapping("/exchange")
    ResponsePacket<Boolean> exchange(@RequestBody UserPointExchangeParams params);

    /**
     * 查询鸡分明细
     *
     * @param uid 用户ID
     * @param offset 分页请求对象
     * @return ResponsePacket<PagingResponse       <                                                                                                                               UserPointItemsQueryVo>>
     */
    @GetMapping("/{uid}/items")
    ResponsePacket<PagingResponse<UserPointItemsQueryVo>> listItems(
            @PathVariable("uid") String uid,
            @RequestParam(value = "offset", required = true) Integer offset,
            @RequestParam(value = "limit", required = true) Integer limit);

    /**
     * 查询车队获得鸡分
     *
     * @param slug 用户ID
     * @return ResponsePacket<PagingResponse>                                                                                                                           UserPointItemsQueryVo>>
     */
    @GetMapping("/items/detail")
    ResponsePacket<UserPointItemsQueryVo> selectByUserIdAndUid(@RequestParam(value = "slug", required = true) String slug);

    /**
     * 给指定用户增加鸡分
     *
     * @param incrPointAmount 待增加的鸡分值（可为负数）
     * @param itemType 明细类型，0：兑换暴鸡值；1：开免费车队；2：获得评分
     * @param slug
     * @return void
     */
    @PostMapping("/add")
    ResponsePacket<Void> incrPoint(@RequestParam(value = "incrPointAmount", required = true) Integer incrPointAmount,
                                   @RequestParam(value = "itemType", required = true) Integer itemType,
                                   @RequestParam(value = "slug", required = true) String slug);
    /**
     * 给指定用户增加鸡分
     *
     * @param uid 用户id
     * @param incrPointAmount 待增加的鸡分值（可为负数）
     * @param itemType 明细类型，0：兑换暴鸡值；1：开免费车队；2：获得评分
     * @param slug
     * @return void
     */
    @PostMapping("/increase")
    ResponsePacket<Void> incrPoint(@RequestParam(value = "uid", required = true) String uid,
                                   @RequestParam(value = "incrPointAmount", required = true) Integer incrPointAmount,
                                   @RequestParam(value = "itemType", required = true) Integer itemType,
                                   @RequestParam(value = "slug", required = true) String slug);

    @PostMapping("/point/incr")
    ResponsePacket<MembersUserPointItemVo> incrPoint(@RequestParam(value = "userId", required = true) Integer userId,
            @RequestParam(value = "incrPointAmount", required = true) Integer incrPointAmount,
            @RequestParam(value = "itemType", required = true) Integer itemType,
            @RequestParam(value = "operationUserId", required = true) Integer operationUserId,
            @RequestParam(value = "slug", required = true) String slug);
}

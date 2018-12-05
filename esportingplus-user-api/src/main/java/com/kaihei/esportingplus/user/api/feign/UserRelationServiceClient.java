package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.user.api.params.RelationUnreadClearParams;
import com.kaihei.esportingplus.user.api.params.UserFollowParams;
import com.kaihei.esportingplus.user.api.params.UserRelationPageParams;
import com.kaihei.esportingplus.user.api.params.UserRelationUnReadVo;
import com.kaihei.esportingplus.user.api.vo.UserRelationVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户关系client
 *
 * @author liuyang
 * @Description
 * @Date 2018/11/24 14:29
 **/
@FeignClient(name = "esportingplus-user-service", path = "/relation", fallbackFactory = UserRelationServiceClientFallbackFactory.class)
public interface UserRelationServiceClient {


    /**
     * 关注
     *
     * @param params
     * @return
     */
    @PostMapping("/follow")
    ResponsePacket<Boolean> follow(@RequestBody UserFollowParams params);

    /**
     * 取消关注
     *
     * @param params
     * @return
     */
    @PostMapping("/unfollow")
    ResponsePacket<Boolean> unfollow(@RequestBody UserFollowParams params);

    /**
     * 判断两人关系
     *
     * @return
     */
    @PostMapping("/relationship")
    ResponsePacket<Integer> relation(@RequestParam(value = "source_id") String sourceId, @RequestParam(value = "target_id") String targetId);

    /**
     * 关注人数
     *
     * @param uid
     * @return
     */
    @PostMapping("/follow/count")
    ResponsePacket<Long> followsCount(@PathVariable String uid);

    /**
     * 粉丝人数
     *
     * @param uid
     * @return
     */
    @PostMapping("/fans/count")
    ResponsePacket<Long> fansCount(@PathVariable String uid);

    /**
     * 好友人数
     *
     * @param uid
     * @return
     */
    @PostMapping("/friend/count")
    ResponsePacket<Long> friendCount(@PathVariable String uid);


    /**
     * 查询关注uid列表
     *
     * @param params
     * @return
     */
    @PostMapping("/follows")
    ResponsePacket<PagingResponse<UserRelationVo>> follows(UserRelationPageParams params);

    /**
     * 查询粉丝
     *
     * @param params
     * @return
     */
    @PostMapping("/fans")
    ResponsePacket<PagingResponse<UserRelationVo>> fans(UserRelationPageParams params);

    /**
     * 查询好友
     *
     * @param params
     * @return
     */
    @PostMapping("/friends")
    ResponsePacket<PagingResponse<UserRelationVo>> friends(UserRelationPageParams params);

    /**
     * 查询未读粉丝，关注， 好友数
     *
     * @param uid
     * @return
     */
    @GetMapping("/unread/{uid}")
    ResponsePacket<UserRelationUnReadVo> unReadNum(@PathVariable String uid);

    /**
     * 清空未读数
     *
     * @param params
     * @return
     */
    @PostMapping("/unread/clear")
    ResponsePacket<Boolean> clearUnread(@RequestBody RelationUnreadClearParams params);

}

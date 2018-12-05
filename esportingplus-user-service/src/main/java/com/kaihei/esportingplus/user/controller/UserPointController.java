package com.kaihei.esportingplus.user.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingRequest;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.user.api.feign.UserPointServiceClient;
import com.kaihei.esportingplus.user.api.params.UserPointExchangeParams;
import com.kaihei.esportingplus.user.api.vo.*;
import com.kaihei.esportingplus.user.data.manager.MembersUserManager;
import com.kaihei.esportingplus.user.domain.service.MembersUserPointService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户鸡分controller类，处理用户鸡分相关请求
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/9 14:19
 */
@RestController
@RequestMapping("/point")
public class UserPointController implements UserPointServiceClient {

    @Autowired
    private MembersUserPointService membersUserPointService;

    @Autowired
    private MembersUserManager membersUserManager;

    /**
     * 查询用户当前鸡分
     */
    @Override
    public ResponsePacket<UserPointQueryVo> get(@ApiParam(value = "用户uid", required = true) @PathVariable("uid") String uid) {
        return ResponsePacket
                .onSuccess(membersUserPointService.queryUserPoint(uid));
    }

    /**
     * 鸡分兑换暴击值
     */
    @Override
    public ResponsePacket<Boolean> exchange(@RequestBody UserPointExchangeParams params) {
        return ResponsePacket.onSuccess(membersUserPointService
                .exchangeScore(params.getUid(), params.getExchangeAmount()));
    }

    /**
     * 查询鸡分明细
     */
    @Override
    public ResponsePacket<PagingResponse<UserPointItemsQueryVo>> listItems(
            @ApiParam(value = "用户uid", required = true) @PathVariable("uid") String uid,
            @RequestParam(value = "offset", required = true) Integer offset,
            @RequestParam(value = "limit", required = true) Integer limit) {
        PagingRequest pagingRequest = new PagingRequest();
        pagingRequest.setOffset(offset);
        pagingRequest.setLimit(limit);
        PagingResponse<UserPointItemsQueryVo> pagingResponse = membersUserPointService
                .listUserPointItems(uid, pagingRequest);
        
        return ResponsePacket.onSuccess(pagingResponse);
    }


    /**
     * 查询车队获得积分
     */
    @Override
    public ResponsePacket<UserPointItemsQueryVo> selectByUserIdAndUid(@RequestParam(value = "slug", required = true) String slug) {
        return ResponsePacket.onSuccess(membersUserPointService
                .getUserPointItem(1, slug));//默认来源 1.免费车队
    }



    /**
     * 给指定用户增加鸡分
     *
     * @param incrPointAmount 待增加的鸡分值（可为负数）
     * @param itemType 明细类型，0：兑换暴鸡值；1：开免费车队；2：获得评分
     * @param slug
     * @return void
     */
    @Override
    public ResponsePacket<Void> incrPoint(@RequestParam(value = "incrPointAmount", required = true) Integer incrPointAmount,
                                          @RequestParam(value = "itemType", required = true) Integer itemType,
                                          @RequestParam(value = "slug", required = true) String slug){
        Integer userId = UserSessionContext.getUser().getId();
        membersUserPointService.incrPoint(userId, incrPointAmount, itemType, slug);
        return ResponsePacket.onSuccess();
    }

    /**
     * 给指定用户增加鸡分
     *
     * @param uid 用户uid
     * @param incrPointAmount 待增加的鸡分值（可为负数）
     * @param itemType 明细类型，0：兑换暴鸡值；1：开免费车队；2：获得评分
     * @param slug
     * @return void
     */
    @Override
    public ResponsePacket<Void> incrPoint(@RequestParam(value = "uid", required = true) String uid,
                                          @RequestParam(value = "incrPointAmount", required = true) Integer incrPointAmount,
                                          @RequestParam(value = "itemType", required = true) Integer itemType,
                                          @RequestParam(value = "slug", required = true) String slug){
        UserInfoVo userInfoVo = membersUserManager.getUserByUid(uid);
        membersUserPointService.incrPoint(Integer.valueOf(userInfoVo.getChicken_id()), incrPointAmount, itemType, slug);
        return ResponsePacket.onSuccess();
    }

    @Override
    @PostMapping("/point/incr")
    public ResponsePacket<MembersUserPointItemVo> incrPoint(@RequestParam(value = "userId", required = true) Integer userId,
            @RequestParam(value = "incrPointAmount", required = true) Integer incrPointAmount,
            @RequestParam(value = "itemType", required = true) Integer itemType,
            @RequestParam(value = "operationUserId", required = true) Integer operationUserId,
            @RequestParam(value = "slug", required = true) String slug) {
        membersUserPointService.incrPoint(userId, incrPointAmount, itemType, operationUserId, slug);
        return ResponsePacket.onSuccess();
    }
}

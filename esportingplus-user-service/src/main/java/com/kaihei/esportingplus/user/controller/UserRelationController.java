package com.kaihei.esportingplus.user.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.user.api.feign.UserRelationServiceClient;
import com.kaihei.esportingplus.user.api.params.RelationUnreadClearParams;
import com.kaihei.esportingplus.user.api.params.UserFollowParams;
import com.kaihei.esportingplus.user.api.params.UserRelationPageParams;
import com.kaihei.esportingplus.user.api.params.UserRelationUnReadVo;
import com.kaihei.esportingplus.user.api.vo.UserRelationVo;
import com.kaihei.esportingplus.user.data.manager.UserRelationManager;
import com.kaihei.esportingplus.user.domain.service.UserRelationService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXB;
import java.util.HashMap;
import java.util.List;

/**
 * @author liuyang
 * @Description
 * @Date 2018/11/24 16:34
 **/
@RestController
@RequestMapping("/relation")
@Api(tags = "用户关系")
public class UserRelationController implements UserRelationServiceClient {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private UserRelationManager userRelationManager;

    @Override
    public ResponsePacket<Boolean> follow(@RequestBody UserFollowParams params) {
        logger.debug("cmd=UserRelationController.follow | params= {}", JacksonUtils.toJson(params));
        return ResponsePacket.onSuccess(userRelationService.follow(params));
    }

    @Override
    public ResponsePacket<Boolean> unfollow(@RequestBody UserFollowParams params) {
        logger.debug("cmd=UserRelationController.unfollow | params= {}", JacksonUtils.toJson(params));
        return ResponsePacket.onSuccess(userRelationService.unfollow(params));
    }

    @Override
    public ResponsePacket<Integer> relation(@RequestParam(value = "source_id") String sourceId, @RequestParam(value = "target_id") String targetId) {
        logger.debug("cmd=UserRelationController.relation | sourceId= {}, targetId = {}", sourceId, targetId);
        return ResponsePacket.onSuccess(userRelationService.relation(sourceId, targetId));
    }

    @Override
    public ResponsePacket<Long> followsCount(String uid) {
        logger.debug("cmd=UserRelationController.followsCount | uid= {}", uid);
        return ResponsePacket.onSuccess(userRelationService.followsCount(uid));
    }

    @Override
    public ResponsePacket<Long> fansCount(String uid) {
        logger.debug("cmd=UserRelationController.fansCount | uid= {}", uid);
        return ResponsePacket.onSuccess(userRelationService.fansCount(uid));
    }

    @Override
    public ResponsePacket<Long> friendCount(String uid) {
        logger.debug("cmd=UserRelationController.friendCount | uid= {}", uid);
        return ResponsePacket.onSuccess(userRelationService.friendCount(uid));
    }

    @Override
    public ResponsePacket<PagingResponse<UserRelationVo>> follows(@RequestBody UserRelationPageParams params) {
        logger.debug("cmd=UserRelationController.follow | params= {}", JacksonUtils.toJson(params));
        PagingResponse<UserRelationVo> follows = userRelationService.follows(params);
        return ResponsePacket.onSuccess(follows);
    }

    @Override
    public ResponsePacket<PagingResponse<UserRelationVo>> fans(@RequestBody UserRelationPageParams params) {
        logger.debug("cmd=UserRelationController.follow | params= {}", JacksonUtils.toJson(params));
        PagingResponse<UserRelationVo> fans = userRelationService.fans(params);
        return ResponsePacket.onSuccess(fans);
    }

    @Override
    public ResponsePacket<PagingResponse<UserRelationVo>> friends(@RequestBody UserRelationPageParams params) {
        logger.debug("cmd=UserRelationController.follow | params= {}", JacksonUtils.toJson(params));
        PagingResponse<UserRelationVo> friends = userRelationService.friends(params);
        return ResponsePacket.onSuccess(friends);
    }

    @Override
    public ResponsePacket<UserRelationUnReadVo> unReadNum(String uid) {
        String unreadNum = userRelationManager.getUnreadNum(uid);
        HashMap hashMap = JacksonUtils.toBean(unreadNum, HashMap.class);
        UserRelationUnReadVo vo = new UserRelationUnReadVo(uid, Integer.valueOf(hashMap.get("fans").toString()), (Integer) hashMap.get("friend"));
        return ResponsePacket.onSuccess(vo);
    }

    @Override
    public ResponsePacket<Boolean> clearUnread(@RequestBody RelationUnreadClearParams params) {
        userRelationManager.clearNum(params.getUid(), params.getType());
        return ResponsePacket.onSuccess(true);
    }
}

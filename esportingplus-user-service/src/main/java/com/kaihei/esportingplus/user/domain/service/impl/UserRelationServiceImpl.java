package com.kaihei.esportingplus.user.domain.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.user.api.params.UserFollowParams;
import com.kaihei.esportingplus.user.api.params.UserRelationPageParams;
import com.kaihei.esportingplus.user.api.vo.UserInfoVo;
import com.kaihei.esportingplus.user.api.vo.UserRelationVo;
import com.kaihei.esportingplus.user.data.manager.MembersUserManager;
import com.kaihei.esportingplus.user.data.manager.UserRelationManager;
import com.kaihei.esportingplus.user.data.repository.UserFansResponsitory;
import com.kaihei.esportingplus.user.data.repository.UserFollowResponsitory;
import com.kaihei.esportingplus.user.data.repository.UserFriendResponsitory;
import com.kaihei.esportingplus.user.domain.entity.MembersUser;
import com.kaihei.esportingplus.user.domain.entity.UserFans;
import com.kaihei.esportingplus.user.domain.entity.UserFollow;
import com.kaihei.esportingplus.user.domain.entity.UserFriend;
import com.kaihei.esportingplus.user.domain.service.MembersUserService;
import com.kaihei.esportingplus.user.domain.service.UserRelationService;
import com.kaihei.esportingplus.user.event.FollowEvent;
import com.kaihei.esportingplus.user.event.UnFollowEvent;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author liuyang
 * @Description
 * @Date 2018/11/27 14:23
 **/
@Service
@Transactional
public class UserRelationServiceImpl implements UserRelationService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserFollowResponsitory followResponsitory;

    @Autowired
    UserFansResponsitory fansResponsitory;

    @Autowired
    UserFriendResponsitory friendResponsitory;

    @Autowired
    MembersUserManager membersUserManager;

    @Autowired
    UserRelationManager userRelationManager;

    @Override
    public Boolean follow(UserFollowParams params) {
        logger.debug("cmd=UserRelationService.follow | params= {}", JacksonUtils.toJson(params));
        Date d = new Date();
        if (CollectionUtils.isEmpty(params.getFollowIds())) {
            return false;
        }

        List<UserFollow> followList = new ArrayList<>();
        List<UserFans> fansList = new ArrayList<>();
        for (String followId : params.getFollowIds()) {
            //不能关注自己， 自动去除
            if (params.getUid().equalsIgnoreCase(followId)){
                continue;
            }

            UserFollow dbuf = this.followResponsitory.selectOne(new UserFollow(params.getUid(), followId));
            if (dbuf == null){
                followList.add(new UserFollow(params.getUid(), followId, d));
                fansList.add(new UserFans(followId,params.getUid(), d));
            }
        }

        if (CollectionUtils.isEmpty(followList)){
            logger.debug("cmd=UserRelationService.follow | msg = 不能重复关注 |params= {}", JacksonUtils.toJson(params));
            return true;
        }

        followResponsitory.insertList(followList);

        //保存fans
        fansResponsitory.insertList(fansList);

        //判断是否好友
        // 查看followids 是否是uid的粉丝
        List<UserFans> userFans = fansResponsitory.fansIn(params.getUid(), params.getFollowIds());
        Map<String, Boolean> isFriend = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userFans)) {
            List<UserFriend> friendList = new ArrayList<>();
            for (UserFans uf : userFans) {
                isFriend.put(uf.getFansId(), true);
                friendList.add(new UserFriend(params.getUid(), uf.getFansId(), d));
            }

            friendResponsitory.insertList(friendList);
        }

        //更新未读数
        for (UserFollow uf : followList) {
            userRelationManager.follow(uf.getUid(), uf.getFollowId(), isFriend.get(uf.getFollowId()) == null ? false : true);
        }

        EventBus.post(new FollowEvent(params.getUid(), params.getFollowIds()));
        return true;
    }

    @Override
    public Boolean unfollow(UserFollowParams params) {
        logger.debug("cmd=UserRelationService.unfollow | params= " + JacksonUtils.toJson(params));
        if (CollectionUtils.isEmpty(params.getFollowIds())) {
            logger.debug("cmd=UserRelationService.unfollow | msg = followids is null return false");
            return false;
        }

        Date d = new Date();
        followResponsitory.deleteFollows(params.getUid(), params.getFollowIds());

        params.getFollowIds().forEach(s -> {
            //删除fans
            fansResponsitory.delete(new UserFans(s, params.getUid(), null));

            //删除好友
            int delete = friendResponsitory.delete(new UserFriend(params.getUid(), s, null));
            userRelationManager.unFollow(params.getUid(), s, delete == 1);
        });

        EventBus.post(new UnFollowEvent(params.getUid(), params.getFollowIds()));
        return true;
    }

    @Override
    public Integer relation(String sourceId, String targetId) {
        logger.debug("cmd=UserRelationService.relation | sourceId= {}  targetId={}", sourceId, targetId);
        Integer result = 0;
        UserFriend friend = friendResponsitory.selectOne(new UserFriend(sourceId, targetId,null));
        if (friend != null) {
            //好友
            result = 1;
        } else if (followResponsitory.selectOne(new UserFollow(sourceId, targetId, null)) != null) {
            //关注
            result = 2;
        } else if (fansResponsitory.selectOne(new UserFans(targetId, sourceId, null)) != null) {
            //粉丝
            result = 3;
        }

        return result;
    }

    @Override
    public Long followsCount(String uid) {
        logger.debug("cmd=UserRelationService.followsCount | uid= {}", uid);
        UserFollow ur = new UserFollow();
        ur.setUid(uid);
        int count = followResponsitory.selectCount(ur);

        return Long.valueOf(count);
    }

    @Override
    public Long fansCount(String uid) {
        logger.debug("cmd=UserRelationService.fansCount | uid= {}", uid);
        UserFans ur = new UserFans();
        ur.setUid(uid);
        int count = fansResponsitory.selectCount(ur);

        return Long.valueOf(count);
    }

    @Override
    public Long friendCount(String uid) {
        logger.debug("cmd=UserRelationService.friendCount | uid= {}", uid);
        return friendResponsitory.friendCount(uid);
    }

    @Override
    public PagingResponse<UserRelationVo> follows(UserRelationPageParams params) {
        logger.debug("cmd=UserRelationService.follows | params= "+ JacksonUtils.toJson(params));
        UserFollow ur = new UserFollow();
        ur.setUid(params.getUid());
        Page<UserFollow> urs = PageHelper.startPage(params.getPage(), params.getSize())
                .setOrderBy("create_time desc")
                .doSelectPage(() -> followResponsitory.select(ur));

        if (CollectionUtils.isEmpty(urs.getResult())) {
            return null;
        }

        List<UserRelationVo> result = new ArrayList<>();
        for (UserFollow uf : urs.getResult()) {
            UserInfoVo infoVo  = membersUserManager.getUserByUid(uf.getFollowId());
            result.add(convert(infoVo));
        }

        PagingResponse<UserRelationVo> pagingResponse = new PagingResponse<UserRelationVo>(
                urs.getPageNum(), urs.getPageSize(),
                urs.getTotal(), result);

        return pagingResponse;
    }

    @Override
    public PagingResponse<UserRelationVo> fans(UserRelationPageParams params) {
        logger.debug("cmd=UserRelationService.fans | params= "+ JacksonUtils.toJson(params));
        UserFans ur = new UserFans();
        ur.setUid(params.getUid());
        Page<UserFans> urs = PageHelper.startPage(params.getPage(), params.getSize())
                .setOrderBy("create_time desc")
                .doSelectPage(() -> fansResponsitory.select(ur));

        if (CollectionUtils.isEmpty(urs.getResult())) {
            return null;
        }

        List<UserRelationVo> result = new ArrayList<>();
        for (UserFans uf : urs.getResult()) {
            UserInfoVo infoVo  = membersUserManager.getUserByUid(uf.getFansId());
            result.add(convert(infoVo));
        }

        PagingResponse<UserRelationVo> pagingResponse = new PagingResponse<UserRelationVo>(
                urs.getPageNum(), urs.getPageSize(),
                urs.getTotal(), result);

        return pagingResponse;
    }

    @Override
    public PagingResponse<UserRelationVo> friends(UserRelationPageParams params) {
        logger.debug("cmd=UserRelationService.friends | params= "+ JacksonUtils.toJson(params));
        Page<UserFriend> friends = PageHelper.startPage(params.getPage(), params.getSize())
                .setOrderBy("create_time desc")
                .doSelectPage(() -> friendResponsitory.friends(params.getUid()));

        if (CollectionUtils.isEmpty(friends.getResult())) {
            return null;
        }

        List<UserRelationVo> result = new ArrayList<>();
        for (UserFriend uf : friends.getResult()) {
            UserInfoVo infoVo = null;
            if (uf.getUid().equalsIgnoreCase(params.getUid())) {
                infoVo  = membersUserManager.getUserByUid(uf.getFriendId());
            } else {
                infoVo  = membersUserManager.getUserByUid(uf.getUid());
            }

            result.add(convert(infoVo));
        }
        PagingResponse<UserRelationVo> pagingResponse = new PagingResponse<UserRelationVo>(
                friends.getPageNum(), friends.getPageSize(),
                friends.getTotal(), result);

        return pagingResponse;
    }

    private UserRelationVo convert(UserInfoVo infoVo) {
        UserRelationVo v = new UserRelationVo();
        v.setSex(infoVo.getSex());
        v.setUid(infoVo.getUid());
        v.setUserName(infoVo.getName());
        return v;
    }
}

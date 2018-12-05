package com.kaihei.esportingplus.user.domain.service;

import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.user.api.params.UserFollowParams;
import com.kaihei.esportingplus.user.api.params.UserRelationPageParams;
import com.kaihei.esportingplus.user.api.vo.UserRelationVo;
import com.kaihei.esportingplus.user.domain.entity.MembersUser;

import java.util.List;

/**
 * @author liuyang
 * @Description
 * @Date 2018/11/27 14:16
 **/
public interface UserRelationService {

    /**
     * 关注
     *
     * @param params
     * @return
     */
    Boolean follow(UserFollowParams params);

    /**
     * 取消关注
     *
     * @param params
     * @return
     */
    Boolean unfollow(UserFollowParams params);

    /**
     * 查询关注uid列表
     *
     * @param params
     * @return
     */
    PagingResponse<UserRelationVo> follows(UserRelationPageParams params);


    /**
     * 判断两人关系
     *
     * @return
     */
    Integer relation(String sourceId, String targetId);

    /**
     * 关注人数
     *
     * @param uid
     * @return
     */
    Long followsCount(String uid);

    /**
     * 粉丝人数
     *
     * @param uid
     * @return
     */
    Long fansCount(String uid);

    /**
     * 好友人数
     *
     * @param uid
     * @return
     */
    Long friendCount(String uid);


    /**
     * 查询粉丝
     *
     * @param params
     * @return
     */
    PagingResponse<UserRelationVo> fans(UserRelationPageParams params);

    /**
     * 查询好友
     *
     * @param params
     * @return
     */
    PagingResponse<UserRelationVo> friends(UserRelationPageParams params);

}

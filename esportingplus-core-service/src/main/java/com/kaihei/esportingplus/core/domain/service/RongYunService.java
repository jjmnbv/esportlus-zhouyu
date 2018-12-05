package com.kaihei.esportingplus.core.domain.service;

import com.kaihei.esportingplus.core.api.params.UserTagParam;

import java.util.List;

/**
 * @Description: 融云服务
 * @Author: yangshidong
 * @Date: 2018年9月11日 15:20
 */
 public interface RongYunService {

    /**
     * 获取融云token
     *
     * @param uid uid
     * @param userName userName
     * @param thumbnail 头像地址
     */
     String getToken(String uid, String userName, String thumbnail);

    /**
     * 刷新融云用户信息
     *
     * @param uid uid
     * @param userName userName
     * @param thumbnail 头像地址
     */
     boolean updateUser(String uid, String userName, String thumbnail);

    /**
     * 封禁用户
     *
     * @param uids uid
     * @param minute 封禁时长，单位为分钟，最大值为43200分钟
     */
     boolean blockUser(List<String> uids, int minute);

    /**
     * 解除用户封禁
     *
     * @param uids uid
     */
     boolean unBlockUser(List<String> uids);

    /**
     * 添加用户到黑名单
     *
     * @param uid uid
     * @param blackUserId 被加黑的用户Id
     */
     boolean addUserToBlacklist(String uid, List<String> blackUserId);

    /**
     * 移除黑名单中用户
     *
     * @param uid uid
     * @param blackUserId 被加黑的用户Id
     */
     boolean removeUserFromBlacklist(String uid, List<String> blackUserId);

    /**
     * 获取某用户黑名单列表
     *
     * @param uid uid
     */
     String[] queryUsersBlacklist(String uid);

    /**
     * 加入群组
     *
     * @param members 要加入群的用户uids
     * @param groupId 群组ID
     * @param groupName 群组ID对应的名称
     */
     boolean joinGroup(List<String> members, String groupId, String groupName);

    /**
     * 退出群组
     *
     * @param members 要退出群的用户uid
     * @param groupId 要退出的群组ID
     */
     boolean leaveGroup(List<String> members, String groupId);

    /**
     * 解散用户组
     * @param uid
     * @param groupId
     * @return
     */
     boolean dismissGroup(String uid, String groupId);


    /**
     * 给用户打标签
     * @param userTagParam
     * @return
     */
    boolean setTag(UserTagParam userTagParam);
}

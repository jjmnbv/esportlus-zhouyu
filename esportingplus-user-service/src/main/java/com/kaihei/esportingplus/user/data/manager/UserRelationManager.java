package com.kaihei.esportingplus.user.data.manager;

/**
 * @author liuyang
 * @Description
 * @Date 2018/12/3 10:31
 **/
public interface UserRelationManager {

    /**
     * 关注
     * @param uid
     * @param followId
     */
    void follow(String uid, String followId, Boolean isFriend);

    /**
     * 取消关注
     * @param uid
     * @param followId
     */
    void unFollow(String uid, String followId, Boolean isFriend);

    /**
     * 获取未读数
     * @param uid
     * @return
     */
    String getUnreadNum(String uid);

    void clearNum(String uid, Integer type);
}

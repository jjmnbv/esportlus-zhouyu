package com.kaihei.esportingplus.user.api.params;

import java.util.List;

/**
 * @author liuyang
 * @Description
 * @Date 2018/12/3 14:09
 **/
public class UserFollowParams {

    String uid;

    List<String> followIds;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getFollowIds() {
        return followIds;
    }

    public void setFollowIds(List<String> followIds) {
        this.followIds = followIds;
    }
}

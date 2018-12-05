package com.kaihei.esportingplus.user.api.vo;

/**
 * 用户关系实体类
 */
public class RelationVo{

    /**
     * 用户UID
     */
    private String uid;

    /**
     * 粉丝数量
     */
    protected int fans;

    /**
     * 好友数
     */
    protected int friend;

    /**
     * 关注数
     */
    protected int follow;

    /**
     * 用户关系
     */
    protected int relationship;

    /**
     * 是否黑名单
     */
    protected boolean isBlackList;

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getFriend() {
        return friend;
    }

    public void setFriend(int friend) {
        this.friend = friend;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public int getRelationship() {
        return relationship;
    }

    public void setRelationship(int relationship) {
        this.relationship = relationship;
    }

    public boolean isBlackList() {
        return isBlackList;
    }

    public void setBlackList(boolean blackList) {
        isBlackList = blackList;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

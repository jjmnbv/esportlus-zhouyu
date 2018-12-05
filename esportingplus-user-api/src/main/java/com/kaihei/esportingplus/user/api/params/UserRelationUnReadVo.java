package com.kaihei.esportingplus.user.api.params;

/**
 * @author liuyang
 * @Description
 * @Date 2018/12/1 18:48
 **/
public class UserRelationUnReadVo {

    private String uid;

    private Integer fansNum;

    private Integer friendNum;

    public UserRelationUnReadVo() {
    }

    public UserRelationUnReadVo(String uid, Integer fansNum, Integer friendNum) {
        this.uid = uid;
        this.fansNum = fansNum;
        this.friendNum = friendNum;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getFansNum() {
        return fansNum;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }

    public Integer getFriendNum() {
        return friendNum;
    }

    public void setFriendNum(Integer friendNum) {
        this.friendNum = friendNum;
    }

}

package com.kaihei.esportingplus.user.api.vo;

public class UserCardVo{

    private UserVo userinfo;

    private BaoJiVo baojiinfo;

    private RelationVo relationinfo;

    private UserOrderDateVo userorderdata;

    public UserVo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserVo userinfo) {
        this.userinfo = userinfo;
    }

    public BaoJiVo getBaojiinfo() {
        return baojiinfo;
    }

    public void setBaojiinfo(BaoJiVo baojiinfo) {
        this.baojiinfo = baojiinfo;
    }

    public RelationVo getRelationinfo() {
        return relationinfo;
    }

    public void setRelationinfo(RelationVo relationinfo) {
        this.relationinfo = relationinfo;
    }

    public UserOrderDateVo getUserorderdata() {
        return userorderdata;
    }

    public void setUserorderdata(UserOrderDateVo userorderdata) {
        this.userorderdata = userorderdata;
    }
}

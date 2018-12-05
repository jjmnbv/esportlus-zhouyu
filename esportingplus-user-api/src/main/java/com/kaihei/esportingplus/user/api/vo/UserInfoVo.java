package com.kaihei.esportingplus.user.api.vo;

public class UserInfoVo{
    /**
     * 用户uid
     */
    protected String uid;

    /**
     * 地区
     */
    protected String region;

    /**
     * 星座
     */
    protected String constellation;

    /**
     * 年龄
     */
    protected String age;

    /**
     * 个人简介
     */
    protected String desc;

    /**
     * 生日
     */
    protected String birthday;

    /**
     * 性别
     */
    protected int sex;

    /**
     * 用户名
     */
    protected String name;

    /**
     * 头像
     */
    protected String thumbnail;

    /**
     * 鸡牌号
     */
    protected String chicken_id;

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
     * 身份
     */
    protected int identity;

    /**
     * 融云的IMID
     */
    protected String rcUserid;

    /**
     * 用户关系
     */
    protected int relationship;

    /**
     * 是否黑名单
     */
    protected boolean is_black_list;

    /**
     * 是否是暴娘
     */
    protected boolean is_bn;

    /**
     * 是否是暴鸡
     */
    protected boolean is_baoji;

    public void setIs_baoji(boolean is_baoji) {
        this.is_baoji = is_baoji;
    }

    public boolean getIs_bn() {
        return is_bn;
    }

    public void setIs_bn(boolean is_bn) {
        this.is_bn = is_bn;
    }

    public boolean getIs_baoji() {
        return is_baoji;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getChicken_id() {
        return chicken_id;
    }

    public void setChicken_id(String chicken_id) {
        this.chicken_id = chicken_id;
    }

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

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public String getRcUserid() {
        return rcUserid;
    }

    public void setRcUserid(String rcUserid) {
        this.rcUserid = rcUserid;
    }

    public int getRelationship() {
        return relationship;
    }

    public void setRelationship(int relationship) {
        this.relationship = relationship;
    }

    public boolean isIs_black_list() {
        return is_black_list;
    }

    public void setIs_black_list(boolean is_black_list) {
        this.is_black_list = is_black_list;
    }

    public boolean isIs_bn() {
        return is_bn;
    }

    public boolean isIs_baoji() {
        return is_baoji;
    }
}

package com.kaihei.esportingplus.user.api.vo;

public class BaoJiCelebrityVo {
    /**
     * 用户名
     */
    private String username;

    /**
     * 个人简介
     */
    private String desc;

    /**
     * 暴鸡标签
     */
    private int baojiTag;

    /**
     * 封面
     */
    private String cover;

    /**
     * 用户uid
     */
    private String uid;

    /**
     * 订单数量
     */
    private int orderCount;

    /**
     * 融云Id
     */
    private String imId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getBaojiTag() {
        return baojiTag;
    }

    public void setBaojiTag(int baojiTag) {
        this.baojiTag = baojiTag;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public String getImId() {
        return imId;
    }

    public void setImId(String imId) {
        this.imId = imId;
    }
}

package com.kaihei.esportingplus.user.api.vo;

import java.util.List;

public class BaoJiVo{

    /**
     * UID
     */
    private String uid;

    /**
     * 暴鸡等级
     */
    private int baojiLevel;

    /**
     * 等级名称
     */
    private String levelName;

    /**
     * 身份
     */
    private int identity;

    /**
     * 暴娘照片列表
     */
    private List<String> bnPictures;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getBaojiLevel() {
        return baojiLevel;
    }

    public void setBaojiLevel(int baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public List<String> getBnPictures() {
        return bnPictures;
    }

    public void setBnPictures(List<String> bnPictures) {
        this.bnPictures = bnPictures;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}

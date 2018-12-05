package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;

/**
 * 用户订单计数
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/12/3 16:52
 */
public class UserOrderCountVo implements Serializable {

    private static final long serialVersionUID = 7264450965904648943L;

    /**
     * UID
     */
    private String uid;

    /**
     * 今日接单数
     */
    private int todayAccept;

    /**
     * 接单总数
     */
    private int acceptCount;

    /**
     * 今日接单数
     */
    private int todayPlace;

    /**
     * 接单总数
     */
    private int placeCount;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getTodayAccept() {
        return todayAccept;
    }

    public void setTodayAccept(int todayAccept) {
        this.todayAccept = todayAccept;
    }

    public int getAcceptCount() {
        return acceptCount;
    }

    public void setAcceptCount(int acceptCount) {
        this.acceptCount = acceptCount;
    }

    public int getTodayPlace() {
        return todayPlace;
    }

    public void setTodayPlace(int todayPlace) {
        this.todayPlace = todayPlace;
    }

    public int getPlaceCount() {
        return placeCount;
    }

    public void setPlaceCount(int placeCount) {
        this.placeCount = placeCount;
    }
}

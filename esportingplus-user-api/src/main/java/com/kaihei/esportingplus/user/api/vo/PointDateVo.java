package com.kaihei.esportingplus.user.api.vo;

/**
 * 鸡分数据
 */
public class PointDateVo {
    /**
     * 当日鸡分值
     */
    private int todayPoint;

    /**
     * 鸡分总值
     */
    private int pointTotal;

    /**
     * 当日接单数
     */
    private int todayOrder;

    public int getTodayPoint() {
        return todayPoint;
    }

    public void setTodayPoint(int todayPoint) {
        this.todayPoint = todayPoint;
    }

    public int getPointTotal() {
        return pointTotal;
    }

    public void setPointTotal(int pointTotal) {
        this.pointTotal = pointTotal;
    }

    public int getTodayOrder() {
        return todayOrder;
    }

    public void setTodayOrder(int todayOrder) {
        this.todayOrder = todayOrder;
    }
}

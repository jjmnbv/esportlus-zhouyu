package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;

/**
 * @author zl.zhao
 * @description:用户积分信息
 * @date: 2018/11/30 17:32
 */
public class UserPointItemsVo implements Serializable {

    private static final long serialVersionUID = 434015420433418049L;

    /**
     * 可兑换余额
     */
    private Integer pointAmount;


    /**
     * 今日获得鸡分
     */
    private Integer todayAccPointAmount;


    public Integer getPointAmount() {
        return pointAmount;
    }

    public void setPointAmount(Integer pointAmount) {
        this.pointAmount = pointAmount;
    }

    public Integer getTodayAccPointAmount() {
        return todayAccPointAmount;
    }

    public void setTodayAccPointAmount(Integer todayAccPointAmount) {
        this.todayAccPointAmount = todayAccPointAmount;
    }
}

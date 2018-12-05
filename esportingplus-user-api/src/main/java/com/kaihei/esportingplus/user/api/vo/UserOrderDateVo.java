package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;

/**
 * 用户订单数据汇总
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/12/3 17:06
 */
public class UserOrderDateVo implements Serializable {

    private static final long serialVersionUID = -5080427222453791175L;

    /**
     * 所有订单总数据
     */
    private UserOrderCountVo allOrderData;

    /**
     * 免费车队订单数据
     */
    private UserOrderCountVo freeOrderData;

    public UserOrderCountVo getAllOrderData() {
        return allOrderData;
    }

    public void setAllOrderData(UserOrderCountVo allOrderData) {
        this.allOrderData = allOrderData;
    }

    public UserOrderCountVo getFreeOrderData() {
        return freeOrderData;
    }

    public void setFreeOrderData(UserOrderCountVo freeOrderData) {
        this.freeOrderData = freeOrderData;
    }
}

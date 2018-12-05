package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;

/**
 * @author Administrator
 * @description:
 * @date: 2018/10/22 20:58
 */
public class UserBalanceResutVo implements Serializable {
    private static final long serialVersionUID = -7492077103183935271L;

    /**
     * 可兑换标识
     */
    private Boolean exchangeMark;

    public UserBalanceResutVo(){}

    public UserBalanceResutVo(Boolean exchangeMark){
        this.exchangeMark = exchangeMark;
    }

    public Boolean getExchangeMark() {
        return exchangeMark;
    }

    public void setExchangeMark(Boolean exchangeMark) {
        this.exchangeMark = exchangeMark;
    }
}

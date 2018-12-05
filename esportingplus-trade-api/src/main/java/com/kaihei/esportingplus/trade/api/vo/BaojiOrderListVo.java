package com.kaihei.esportingplus.trade.api.vo;


import java.io.Serializable;

/**
 * 暴鸡订单
 */
public class BaojiOrderListVo implements Serializable {

    private static final long serialVersionUID = 9068008189312935444L;

    private UserInfoVo userInfoVo;

    private BaojiOrderVO baojiOrderVO;

    public UserInfoVo getUserInfoVo() {
        return userInfoVo;
    }

    public void setUserInfoVo(UserInfoVo userInfoVo) {
        this.userInfoVo = userInfoVo;
    }

    public BaojiOrderVO getBaojiOrderVO() {
        return baojiOrderVO;
    }

    public void setBaojiOrderVO(BaojiOrderVO baojiOrderVO) {
        this.baojiOrderVO = baojiOrderVO;
    }

    @Override
    public String toString() {
        return "BaojiOrderListVo{" +
                "userInfoVo=" + userInfoVo +
                ", baojiOrderVO=" + baojiOrderVO +
                '}';
    }
}

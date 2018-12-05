package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;

public class BossOrderListVo implements Serializable {

    private static final long serialVersionUID = -2208736219779054033L;

    private UserInfoVo userInfoVo;

    private BossOrderVO bossOrderVO;

    public UserInfoVo getUserInfoVo() {
        return userInfoVo;
    }

    public void setUserInfoVo(UserInfoVo userInfoVo) {
        this.userInfoVo = userInfoVo;
    }

    public BossOrderVO getBossOrderVO() {
        return bossOrderVO;
    }

    public void setBossOrderVO(BossOrderVO bossOrderVO) {
        this.bossOrderVO = bossOrderVO;
    }

    @Override
    public String toString() {
        return "BossOrderListVo{" +
                "userInfoVo=" + userInfoVo +
                ", bossOrderVO=" + bossOrderVO +
                '}';
    }
}

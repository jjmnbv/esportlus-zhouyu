package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-15 15:24
 * @Description:
 */
public class InvitStatisticsVo implements Serializable {

    private static final long serialVersionUID = -5048369280710494205L;

    /**
     * 成功邀请人数
     */
    private Long invitAmount;

    /**
     * 暴击币奖励数量
     */
    private String coinAwardAmount;

    /**
     * 累计免单次数
     */
    private Long freeUsedAmount;

    /**
     * 可用免单次数
     */
    private Long freeAvaliableAmount;

    public Long getFreeUsedAmount() {
        return freeUsedAmount;
    }

    public void setFreeUsedAmount(Long freeUsedAmount) {
        this.freeUsedAmount = freeUsedAmount;
    }

    public Long getFreeAvaliableAmount() {
        return freeAvaliableAmount;
    }

    public void setFreeAvaliableAmount(Long freeAvaliableAmount) {
        this.freeAvaliableAmount = freeAvaliableAmount;
    }

    public Long getInvitAmount() {
        return invitAmount;
    }

    public void setInvitAmount(Long invitAmount) {
        this.invitAmount = invitAmount;
    }

    public String getCoinAwardAmount() {
        return coinAwardAmount;
    }

    public void setCoinAwardAmount(String coinAwardAmount) {
        this.coinAwardAmount = coinAwardAmount;
    }
}

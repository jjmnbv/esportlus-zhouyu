package com.kaihei.esportingplus.marketing.api.vo;

import java.io.Serializable;

/**
 * @author zl.zhao
 * @description:用户详细信息
 * @date: 2018/12/3 17:06
 */
public class UserFreeCouponsDtlVo implements Serializable {

    private static final long serialVersionUID = 300866823148682982L;

    /**
     * 每日赠送匹配次数
     */
    private Integer systemCount;

    /**
     * 邀请匹配次数
     */
    private Integer inviteCount;

    /**
     * 可用次数
     */
    private Integer availableCount;

    /**
     * 	成功邀请好友数量
     */
    private Integer invitedAmount;

    public Integer getSystemCount() {
        return systemCount;
    }

    public void setSystemCount(Integer systemCount) {
        this.systemCount = systemCount;
    }

    public Integer getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(Integer inviteCount) {
        this.inviteCount = inviteCount;
    }

    public Integer getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(Integer availableCount) {
        this.availableCount = availableCount;
    }

    public Integer getInvitedAmount() {
        return invitedAmount;
    }

    public void setInvitedAmount(Integer invitedAmount) {
        this.invitedAmount = invitedAmount;
    }
}

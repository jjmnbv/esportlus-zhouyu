package com.kaihei.esportingplus.marketing.event;

import com.kaihei.esportingplus.common.event.Event;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-17 16:46
 * @Description:
 */
public class FriendInvitFriendIMEvent implements Event {

    /**
     * 好友成功邀请数
     */
    private Integer invitAmount;

    /**
     * 奖励次数
     */
    private Integer freeAmount;

    /**
     * 邀请人userId
     */
    private Integer userId;

    /**
     * im通知者userId
     */
    private Integer imUserId;

    public FriendInvitFriendIMEvent() {
    }

    public FriendInvitFriendIMEvent(Integer invitAmount, Integer freeAmount, Integer userId,
            Integer imUserId) {
        this.invitAmount = invitAmount;
        this.freeAmount = freeAmount;
        this.userId = userId;
        this.imUserId = imUserId;
    }

    public Integer getImUserId() {
        return imUserId;
    }

    public void setImUserId(Integer imUserId) {
        this.imUserId = imUserId;
    }

    public Integer getInvitAmount() {
        return invitAmount;
    }

    public void setInvitAmount(Integer invitAmount) {
        this.invitAmount = invitAmount;
    }

    public Integer getFreeAmount() {
        return freeAmount;
    }

    public void setFreeAmount(Integer freeAmount) {
        this.freeAmount = freeAmount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

package com.kaihei.esportingplus.marketing.event;

import com.kaihei.esportingplus.common.event.Event;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-17 16:46
 * @Description:
 */
public class FriendFinishedTeamIMEvent implements Event {


    /**
     * 奖励次数
     */
    private Integer freeAmount;

    /**
     * 完成者userId
     */
    private Integer userId;

    /**
     * im通知者userId
     */
    private Integer imUserId;

    public FriendFinishedTeamIMEvent() {
    }

    public FriendFinishedTeamIMEvent(Integer freeAmount, Integer userId,
            Integer imUserId) {
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

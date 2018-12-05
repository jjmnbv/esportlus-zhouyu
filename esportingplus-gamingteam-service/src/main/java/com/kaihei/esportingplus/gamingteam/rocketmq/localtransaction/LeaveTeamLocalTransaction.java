package com.kaihei.esportingplus.gamingteam.rocketmq.localtransaction;

import com.kaihei.esportingplus.gamingteam.event.rongcloud.RcLeaveTeamEvent;
import com.kaihei.esportingplus.gamingteam.event.weixin.WeXinOrderCancelEvent;
import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 离开车队(退出车队、踢出车队)时的本地事务
 * 包括: 异步推送融云和微信通知
 * @author liangyi
 */
public class LeaveTeamLocalTransaction implements Serializable {

    private static final long serialVersionUID = 3693330572075509668L;

    /**
     * 融云
     */
    RcLeaveTeamEvent rcLeaveTeamEvent;

    /**
     * 微信
     */
    WeXinOrderCancelEvent weXinOrderCancelEvent;

    public LeaveTeamLocalTransaction() {
    }

    public RcLeaveTeamEvent getRcLeaveTeamEvent() {
        return rcLeaveTeamEvent;
    }

    public void setRcLeaveTeamEvent(
            RcLeaveTeamEvent rcLeaveTeamEvent) {
        this.rcLeaveTeamEvent = rcLeaveTeamEvent;
    }

    public WeXinOrderCancelEvent getWeXinOrderCancelEvent() {
        return weXinOrderCancelEvent;
    }

    public void setWeXinOrderCancelEvent(
            WeXinOrderCancelEvent weXinOrderCancelEvent) {
        this.weXinOrderCancelEvent = weXinOrderCancelEvent;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

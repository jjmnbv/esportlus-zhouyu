package com.kaihei.esportingplus.gamingteam.event.rongcloud;

import com.kaihei.esportingplus.common.event.Event;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 老板支付成功事件通知
 * @author liangyi
 */
public class RcBossPaidSuccessEvent implements Event{

    /** 车队序列号, 作为融云群组 id */
    private String teamSequence;

    public RcBossPaidSuccessEvent() {
    }

    public String getTeamSequence() {
        return teamSequence;
    }

    public void setTeamSequence(String teamSequence) {
        this.teamSequence = teamSequence;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

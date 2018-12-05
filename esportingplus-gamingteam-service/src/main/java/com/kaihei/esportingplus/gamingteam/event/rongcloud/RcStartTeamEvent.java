package com.kaihei.esportingplus.gamingteam.event.rongcloud;

import com.kaihei.esportingplus.common.event.Event;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 立即开车事件
 * @author liangyi
 */
public class RcStartTeamEvent implements Event {

    /** 车队序列号, 用于融云群组 id */
    private String teamSequence;

    public RcStartTeamEvent() {
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

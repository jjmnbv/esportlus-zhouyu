package com.kaihei.esportingplus.gamingteam.rocketmq.localtransaction;

import com.kaihei.esportingplus.gamingteam.event.rongcloud.RcStartTeamEvent;
import com.kaihei.esportingplus.gamingteam.event.weixin.WeXinStartTeamEvent;
import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 立即开车时的本地事务
 * 包括: 修改车队状态、异步推送融云和微信通知
 * @author liangyi
 */
public class StartTeamLocalTransaction implements Serializable {

    private static final long serialVersionUID = -8087957581566697504L;

    /**
     * 车队序列号
     */
    private String teamSequence;

    /**
     * 车队状态
     */
    private Integer teamStatus;

    /**
     * 融云
     */
    private RcStartTeamEvent rcStartTeamEvent;

    /**
     * 微信
     */
    private WeXinStartTeamEvent weXinStartTeamEvent;

    public StartTeamLocalTransaction() {
    }

    public String getTeamSequence() {
        return teamSequence;
    }

    public void setTeamSequence(String teamSequence) {
        this.teamSequence = teamSequence;
    }

    public Integer getTeamStatus() {
        return teamStatus;
    }

    public void setTeamStatus(Integer teamStatus) {
        this.teamStatus = teamStatus;
    }

    public RcStartTeamEvent getRcStartTeamEvent() {
        return rcStartTeamEvent;
    }

    public void setRcStartTeamEvent(
            RcStartTeamEvent rcStartTeamEvent) {
        this.rcStartTeamEvent = rcStartTeamEvent;
    }

    public WeXinStartTeamEvent getWeXinStartTeamEvent() {
        return weXinStartTeamEvent;
    }

    public void setWeXinStartTeamEvent(
            WeXinStartTeamEvent weXinStartTeamEvent) {
        this.weXinStartTeamEvent = weXinStartTeamEvent;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

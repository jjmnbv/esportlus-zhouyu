package com.kaihei.esportingplus.gamingteam.event.weixin;

import com.kaihei.esportingplus.common.event.Event;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 车队订单取消时微信服务通知
 * @author liangyi
 */
public class WeXinOrderCancelEvent implements Event {

    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 副本名称
     */
    private String raidName;

    /**
     * 车队序列号
     */
    private String teamSequence;

    /**
     * 订单取消原因类型
     * 1:队长解散车队
     */
    private Integer reasonType;

    /**
     * 车队队员 uid
     */
    List<String> memberUidList;

    public WeXinOrderCancelEvent() {
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getRaidName() {
        return raidName;
    }

    public void setRaidName(String raidName) {
        this.raidName = raidName;
    }

    public String getTeamSequence() {
        return teamSequence;
    }

    public void setTeamSequence(String teamSequence) {
        this.teamSequence = teamSequence;
    }

    public Integer getReasonType() {
        return reasonType;
    }

    public void setReasonType(Integer reasonType) {
        this.reasonType = reasonType;
    }

    public List<String> getMemberUidList() {
        return memberUidList;
    }

    public void setMemberUidList(List<String> memberUidList) {
        this.memberUidList = memberUidList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

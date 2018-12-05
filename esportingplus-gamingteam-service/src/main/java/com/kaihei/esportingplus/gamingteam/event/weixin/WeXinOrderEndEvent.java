package com.kaihei.esportingplus.gamingteam.event.weixin;

import com.kaihei.esportingplus.common.event.Event;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 车队正常结束时微信服务通知
 * @author liangyi
 */
@AllArgsConstructor
@Builder
public class WeXinOrderEndEvent implements Event {

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
     * 队长名称
     */
    private String leaderName;

    /**
     * 车队队员 uid
     */
    List<String> memberUidList;

    public WeXinOrderEndEvent() {
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

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
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

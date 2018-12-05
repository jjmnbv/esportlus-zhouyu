package com.kaihei.esportingplus.gamingteam.event.weixin;

import com.kaihei.esportingplus.common.event.Event;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 立即开车时的微信服务通知
 * @author liangyi
 */
public class WeXinStartTeamEvent implements Event {

    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 副本名称
     */
    private String raidName;

    /**
     * 跨区名称
     */
    private String zoneAcrossName;

    /**
     * 车队序列号
     */
    private String teamSequence;

    /**
     * 队长 uid
     */
    private String leaderUid;

    /**
     * 其他队员 uid
     */
    List<String> otherUidList;

    public WeXinStartTeamEvent() {
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

    public String getZoneAcrossName() {
        return zoneAcrossName;
    }

    public void setZoneAcrossName(String zoneAcrossName) {
        this.zoneAcrossName = zoneAcrossName;
    }

    public String getTeamSequence() {
        return teamSequence;
    }

    public void setTeamSequence(String teamSequence) {
        this.teamSequence = teamSequence;
    }

    public String getLeaderUid() {
        return leaderUid;
    }

    public void setLeaderUid(String leaderUid) {
        this.leaderUid = leaderUid;
    }

    public List<String> getOtherUidList() {
        return otherUidList;
    }

    public void setOtherUidList(List<String> otherUidList) {
        this.otherUidList = otherUidList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

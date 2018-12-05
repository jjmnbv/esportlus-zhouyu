package com.kaihei.esportingplus.gamingteam.event.rongcloud;

import com.kaihei.esportingplus.common.event.Event;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 结束车队事件--解散车队、结束车队
 * @author liangyi
 */
@AllArgsConstructor
@Builder
public class RcEndTeamEvent implements Event {

    /** 是否是解散车队 true: 解散车队, false: 结束车队 */
    private Boolean dismissTeam;

    /** 车队序列号, 作为融云群组 id */
    private String teamSequence;

    /** 车队标题, 作为融云群组 name */
    private String teamTitle;

    /** 车队队长的 uid */
    private String leaderUid;

    /** 接收消息的用户 uid(不包括自己) */
    private List<String> otherUidList;

    public RcEndTeamEvent() {
    }

    public Boolean getDismissTeam() {
        return dismissTeam;
    }

    public void setDismissTeam(Boolean dismissTeam) {
        this.dismissTeam = dismissTeam;
    }

    public String getTeamSequence() {
        return teamSequence;
    }

    public void setTeamSequence(String teamSequence) {
        this.teamSequence = teamSequence;
    }

    public String getTeamTitle() {
        return teamTitle;
    }

    public void setTeamTitle(String teamTitle) {
        this.teamTitle = teamTitle;
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

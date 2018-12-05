package com.kaihei.esportingplus.gamingteam.event.rongcloud;

import com.kaihei.esportingplus.common.event.Event;
import com.kaihei.esportingplus.gamingteam.api.enums.ImMemberOutReasonEnum;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 离开车队事件--踢出车队、主动退出车队
 * @author liangyi
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RcLeaveTeamEvent implements Event {

    /** 是否被踢出 true: 被踢出, false: 主动退出 */
    private Boolean kickOut;

    /** 被踢出的时原因(系统踢出、被队长踢出) */
    private ImMemberOutReasonEnum outReason;

    /** 车队序列号, 作为融云群组 id */
    private String teamSequence;

    /** 车队标题, 作为融云群组 name */
    private String teamTitle;

    /** 车队队员 uid */
    private String uid;

    /** 车队队员昵称 */
    private String username;

    /** 接收消息的用户 uid(不包括自己) */
    private List<String> otherUidList;

    public Boolean getKickOut() {
        return kickOut;
    }

    public void setKickOut(Boolean kickOut) {
        this.kickOut = kickOut;
    }

    public ImMemberOutReasonEnum getOutReason() {
        return outReason;
    }

    public void setOutReason(ImMemberOutReasonEnum outReason) {
        this.outReason = outReason;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

package com.kaihei.esportingplus.gamingteam.api.vo.pvp;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 获取车队队员所在的车队信息
 * @author liangyi
 */
@Builder
@AllArgsConstructor
public class PVPFreeMemberInTeamVO implements Serializable {

    private static final long serialVersionUID = -5101500175306608486L;

    /**
     * 车队队员是否在车队
     * true: 在车队, 返回车队序列号
     * false: 不在车队, 其他字段返回 null
     */
    private Boolean memberInTeam;

    /** 车队序列号 */
    private String teamSequence;

    /** 车队状态，0：准备中 1：已发车(进行中) 2：已解散 3：已结束 */
    private Integer teamStatus;

    /** 车队当前的实际位置数 */
    private Integer actuallyPositionCount;

    /** 车队当前的队员数 */
    private Integer teamMemberSize;

    public PVPFreeMemberInTeamVO() {
    }

    public Boolean getMemberInTeam() {
        return memberInTeam;
    }

    public void setMemberInTeam(Boolean memberInTeam) {
        this.memberInTeam = memberInTeam;
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

    public Integer getActuallyPositionCount() {
        return actuallyPositionCount;
    }

    public void setActuallyPositionCount(Integer actuallyPositionCount) {
        this.actuallyPositionCount = actuallyPositionCount;
    }

    public Integer getTeamMemberSize() {
        return teamMemberSize;
    }

    public void setTeamMemberSize(Integer teamMemberSize) {
        this.teamMemberSize = teamMemberSize;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

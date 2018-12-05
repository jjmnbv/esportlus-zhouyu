package com.kaihei.esportingplus.gamingteam.api.vo.pvp;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 免费车队当前用户的匹配信息
 * @author liangyi
 */
@Builder
@AllArgsConstructor
public class PVPFreeTeamCurrentMatchingInfoVO implements Serializable {

    private static final long serialVersionUID = 1337112178378449528L;

    /**
     * 车队类型名称
     */
    private String teamCategoryName;

    /**
     * 当前用户匹配的游戏显示信息
     */
    private String userMatchingGameInfo;

    /**
     * 匹配从开始到现在过去的时间
     */
    private Integer matchingPastTime;

    /**
     * 当前用户头像
     */
    private String userAvatar;

    /**
     * 是否已经匹配成功 true: 匹配成功; false: 匹配失败
     */
    private Boolean matchedTeam;

    /**
     * 车队队员信息
     */
    List<PVPFreeTeamMemberVO> memberInfoList;

    public PVPFreeTeamCurrentMatchingInfoVO() {
    }

    public String getTeamCategoryName() {
        return teamCategoryName;
    }

    public void setTeamCategoryName(String teamCategoryName) {
        this.teamCategoryName = teamCategoryName;
    }

    public String getUserMatchingGameInfo() {
        return userMatchingGameInfo;
    }

    public void setUserMatchingGameInfo(String userMatchingGameInfo) {
        this.userMatchingGameInfo = userMatchingGameInfo;
    }

    public Integer getMatchingPastTime() {
        return matchingPastTime;
    }

    public void setMatchingPastTime(Integer matchingPastTime) {
        this.matchingPastTime = matchingPastTime;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public Boolean getMatchedTeam() {
        return matchedTeam;
    }

    public void setMatchedTeam(Boolean matchedTeam) {
        this.matchedTeam = matchedTeam;
    }

    public List<PVPFreeTeamMemberVO> getMemberInfoList() {
        return memberInfoList;
    }

    public void setMemberInfoList(
            List<PVPFreeTeamMemberVO> memberInfoList) {
        this.memberInfoList = memberInfoList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

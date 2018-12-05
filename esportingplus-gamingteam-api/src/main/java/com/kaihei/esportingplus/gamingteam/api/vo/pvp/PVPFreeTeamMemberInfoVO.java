package com.kaihei.esportingplus.gamingteam.api.vo.pvp;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 免费车队及队员基本信息
 * @author liangyi
 */
@Builder
@AllArgsConstructor
public class PVPFreeTeamMemberInfoVO implements Serializable {

    private static final long serialVersionUID = 5341640620101967419L;

    /**
     * 车队所属的游戏信息
     */
    PVPFreeTeamGameVO teamInfo;

    /**
     * 车队队员信息
     */
    List<PVPFreeTeamMemberVO> memberInfoList;

    public PVPFreeTeamMemberInfoVO() {
    }

    public PVPFreeTeamGameVO getTeamInfo() {
        return teamInfo;
    }

    public void setTeamInfo(PVPFreeTeamGameVO teamInfo) {
        this.teamInfo = teamInfo;
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

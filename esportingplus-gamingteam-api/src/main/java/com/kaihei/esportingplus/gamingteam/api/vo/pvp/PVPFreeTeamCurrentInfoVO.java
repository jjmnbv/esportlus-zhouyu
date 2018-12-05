package com.kaihei.esportingplus.gamingteam.api.vo.pvp;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 免费车队实时数据
 *
 * @author liangyi
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PVPFreeTeamCurrentInfoVO implements Serializable {

    private static final long serialVersionUID = 5292226336441795494L;

    /**
     * 车队分类名称
     */
    private String teamCategoryName;

    /**
     * 车队序列号
     */
    private String sequence;

    /**
     * 车队标题
     */
    private String teamTitle;

    /**
     * 车队房间号
     */
    private Integer roomNum;

    /**
     * 玩法模式 {@link com.kaihei.esportingplus.common.enums.PlayModeEnum}
     */
    private Integer playMode;

    /**
     * 车队游戏新 王者荣耀-上分一局·微信大区·倔强青铜~最强王者·1局
     */
    private String teamGameInfo;

    /**
     * 车队总位置数(初始位置数, 从资源服务获取)
     */
    private Integer originalPositionCount;

    /**
     * 车队状态 0：准备中 1：已发车(进行中) 2：已解散 3：已结束
     */
    private Integer teamStatus;

    /**
     * 车队队员数量
     */
    private Integer memberSize;

    /**
     * 当前车队队员的计价
     * 如果是暴鸡则为预计鸡分收益
     * 如果是老板则为 0
     */
    private Integer profit;

    /**
     * 当前车队队员身份，0：老板，1：暴鸡，2：暴娘，9：队长
     */
    private Integer userIdentity;

    /**
     * 当前车队队员的 uid
     */
    private String uid;

    /**
     * 当前车队的成员信息列表
     */
    List<PVPFreeTeamMemberVO> teamMemberList;

    public String getTeamCategoryName() {
        return teamCategoryName;
    }

    public void setTeamCategoryName(String teamCategoryName) {
        this.teamCategoryName = teamCategoryName;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getTeamTitle() {
        return teamTitle;
    }

    public void setTeamTitle(String teamTitle) {
        this.teamTitle = teamTitle;
    }

    public Integer getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(Integer roomNum) {
        this.roomNum = roomNum;
    }

    public Integer getPlayMode() {
        return playMode;
    }

    public void setPlayMode(Integer playMode) {
        this.playMode = playMode;
    }

    public String getTeamGameInfo() {
        return teamGameInfo;
    }

    public void setTeamGameInfo(String teamGameInfo) {
        this.teamGameInfo = teamGameInfo;
    }

    public Integer getOriginalPositionCount() {
        return originalPositionCount;
    }

    public void setOriginalPositionCount(Integer originalPositionCount) {
        this.originalPositionCount = originalPositionCount;
    }

    public Integer getTeamStatus() {
        return teamStatus;
    }

    public void setTeamStatus(Integer teamStatus) {
        this.teamStatus = teamStatus;
    }

    public Integer getMemberSize() {
        return memberSize;
    }

    public void setMemberSize(Integer memberSize) {
        this.memberSize = memberSize;
    }

    public Integer getProfit() {
        return profit;
    }

    public void setProfit(Integer profit) {
        this.profit = profit;
    }

    public Integer getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(Integer userIdentity) {
        this.userIdentity = userIdentity;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<PVPFreeTeamMemberVO> getTeamMemberList() {
        return teamMemberList;
    }

    public void setTeamMemberList(
            List<PVPFreeTeamMemberVO> teamMemberList) {
        this.teamMemberList = teamMemberList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

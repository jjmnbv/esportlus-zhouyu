package com.kaihei.esportingplus.gamingteam.api.vo.pvp;

import com.kaihei.esportingplus.gamingteam.api.vo.TeamStartOrderBaseVO;
import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author zhangfang
 */
public class PVPFreeTeamStartOrderVO extends TeamStartOrderBaseVO {

    private static final long serialVersionUID = 6633435102855178830L;
    /**
     * 游戏大区id
     */
    private Integer gameZoneId;
    /**
     * 游戏大区名称
     */
    private String gameZoneName;

    /**
     * 免费车队类型ID
     */
    private Integer freeTeamTypeId;

    /**
     * 免费车队类型显示名称
     */
    private String freeTeamTypeName;

    /** 玩法模式, 0:上分; 1:陪玩; */
    private Byte playMode;

    /** 结算类型, 1:局; 2:小时 */
    private Byte settlementType;
    /** 结算数量(保留1位有效数字) */
    private BigDecimal settlementNumber;

    /** 队员队员信息列表 */
    List<PVPFreeMemberInfoVO> memberInfos;




    public Integer getGameZoneId() {
        return gameZoneId;
    }

    public void setGameZoneId(Integer gameZoneId) {
        this.gameZoneId = gameZoneId;
    }

    public String getGameZoneName() {
        return gameZoneName;
    }

    public void setGameZoneName(String gameZoneName) {
        this.gameZoneName = gameZoneName;
    }


    public Integer getFreeTeamTypeId() {
        return freeTeamTypeId;
    }

    public void setFreeTeamTypeId(Integer freeTeamTypeId) {
        this.freeTeamTypeId = freeTeamTypeId;
    }

    public String getFreeTeamTypeName() {
        return freeTeamTypeName;
    }

    public void setFreeTeamTypeName(String freeTeamTypeName) {
        this.freeTeamTypeName = freeTeamTypeName;
    }

    public Byte getPlayMode() {
        return playMode;
    }

    public void setPlayMode(Byte playMode) {
        this.playMode = playMode;
    }

    public Byte getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(Byte settlementType) {
        this.settlementType = settlementType;
    }

    public BigDecimal getSettlementNumber() {
        return settlementNumber;
    }

    public void setSettlementNumber(BigDecimal settlementNumber) {
        this.settlementNumber = settlementNumber;
    }

    public List<PVPFreeMemberInfoVO> getMemberInfos() {
        return memberInfos;
    }

    public void setMemberInfos(
            List<PVPFreeMemberInfoVO> memberInfos) {
        this.memberInfos = memberInfos;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

package com.kaihei.esportingplus.gamingteam.api.vo.pvp;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 免费车队基本信息
 * @author liangyi
 */
@Builder
@AllArgsConstructor
public class PVPFreeTeamBaseVO implements Serializable {

    private static final long serialVersionUID = -8162963836270182828L;

    /**
     * 车队序列号
     */
    private String sequence;

    /**
     * 车队标题
     */
    private String title;

    /**
     * 车队房间号
     */
    private Integer roomNum;

    /**
     * 车队原始位置数
     */
    private Integer originalPositionCount;

    /**
     * 结算类型 {@link com.kaihei.esportingplus.common.enums.SettlementTypeEnum}
     */
    private Integer settlementType;

    /**
     * 结算数量
     */
    private BigDecimal settlementNumber;

    /**
     * 免费车队类型基本信息
     */
    private PVPFreeTeamGameVO gameInfo;

    public PVPFreeTeamBaseVO() {
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(Integer roomNum) {
        this.roomNum = roomNum;
    }

    public Integer getOriginalPositionCount() {
        return originalPositionCount;
    }

    public void setOriginalPositionCount(Integer originalPositionCount) {
        this.originalPositionCount = originalPositionCount;
    }

    public Integer getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(Integer settlementType) {
        this.settlementType = settlementType;
    }

    public BigDecimal getSettlementNumber() {
        return settlementNumber;
    }

    public void setSettlementNumber(BigDecimal settlementNumber) {
        this.settlementNumber = settlementNumber;
    }

    public PVPFreeTeamGameVO getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(PVPFreeTeamGameVO gameInfo) {
        this.gameInfo = gameInfo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

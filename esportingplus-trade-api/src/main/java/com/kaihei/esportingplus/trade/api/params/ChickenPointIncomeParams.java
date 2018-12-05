package com.kaihei.esportingplus.trade.api.params;

import com.kaihei.esportingplus.trade.api.vo.PVPFreeBaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

@Validated
@ApiModel(value = "鸡分收益参数", description = "鸡分收益参数")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class ChickenPointIncomeParams implements Serializable {

    private static final long serialVersionUID = 3499054332716113081L;
    @NotNull(message = "免费车队类型不能为空")
    @ApiModelProperty(value = "免费车队类型")
    private Integer freeTeamTypeId;

    @NotNull(message = "暴鸡等级不能为空")
    @ApiModelProperty(value = "暴鸡等级")
    private Integer baojiLevel;

    @NotNull(message = "游戏结果不能为空")
    @ApiModelProperty(value = "游戏结果")
    private Integer gameResultCode;

    @NotEmpty(message = "PVPFree老板基本信息列表不能为空")
    @ApiModelProperty(value = "PVPFree老板基本信息列表")
    private List<PVPFreeBaseVO> pvpFreeBossVOS;

    @ApiModelProperty(value = "车队状态：0=未开车，1=已开车, 2=已结束")
    private Integer teamStatus;

    public Integer getFreeTeamTypeId() {
        return freeTeamTypeId;
    }

    public void setFreeTeamTypeId(Integer freeTeamTypeId) {
        this.freeTeamTypeId = freeTeamTypeId;
    }

    public Integer getBaojiLevel() {
        return baojiLevel;
    }

    public void setBaojiLevel(Integer baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    public Integer getGameResultCode() {
        return gameResultCode;
    }

    public void setGameResultCode(Integer gameResultCode) {
        this.gameResultCode = gameResultCode;
    }

    public List<PVPFreeBaseVO> getPvpFreeBossVOS() {
        return pvpFreeBossVOS;
    }

    public void setPvpFreeBossVOS(
            List<PVPFreeBaseVO> pvpFreeBossVOS) {
        this.pvpFreeBossVOS = pvpFreeBossVOS;
    }

    public Integer getTeamStatus() {
        return teamStatus;
    }

    public void setTeamStatus(Integer teamStatus) {
        this.teamStatus = teamStatus;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

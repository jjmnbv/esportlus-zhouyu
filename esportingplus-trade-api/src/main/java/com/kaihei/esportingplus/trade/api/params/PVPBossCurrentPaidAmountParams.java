package com.kaihei.esportingplus.trade.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * 老板当前支付金额参数
 * @author liangyi
 */
@ApiModel("老板当前支付金额参数")
@Validated
@Builder
@AllArgsConstructor
public class PVPBossCurrentPaidAmountParams implements Serializable {

    private static final long serialVersionUID = -3709183774816279053L;

    @NotBlank(message = "订单序列号不能为空")
    @ApiModelProperty(value = "订单序列号", required = true, position = 1, example = "BJ2018")
    private String orderSequence;

    /**
     * 结算类型 {@link com.kaihei.esportingplus.common.enums.SettlementTypeEnum}
     */
    @ApiModelProperty(value = "结算类型", required = true, position = 2, example = "1")
    @NotNull(message = "结算类型不能为空")
    private Integer settlementType;

    @ApiModelProperty(value = "结算数量", required = true, position = 3, example = "1")
    @NotNull(message = "结算数量不能为空")
    private BigDecimal settlementNumber;

    @ApiModelProperty(value = "游戏id", required = true, position = 4, example = "1")
    @NotNull(message = "游戏id不能为空")
    private Integer gameId;

    @ApiModelProperty(value = "老板段位id", required = true, position = 5, example = "50")
    @NotNull(message = "老板段位id不能为空")
    private Integer bossGameDanId;

    @ApiModelProperty(value = "暴鸡等级code", required = true, position = 6, example = "[100,101]")
    @NotEmpty(message = "暴鸡等级code不能为空")
    private List<Integer> baojiLevelCodeList;

    public PVPBossCurrentPaidAmountParams() {
    }

    public String getOrderSequence() {
        return orderSequence;
    }

    public void setOrderSequence(String orderSequence) {
        this.orderSequence = orderSequence;
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

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getBossGameDanId() {
        return bossGameDanId;
    }

    public void setBossGameDanId(Integer bossGameDanId) {
        this.bossGameDanId = bossGameDanId;
    }

    public List<Integer> getBaojiLevelCodeList() {
        return baojiLevelCodeList;
    }

    public void setBaojiLevelCodeList(List<Integer> baojiLevelCodeList) {
        this.baojiLevelCodeList = baojiLevelCodeList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

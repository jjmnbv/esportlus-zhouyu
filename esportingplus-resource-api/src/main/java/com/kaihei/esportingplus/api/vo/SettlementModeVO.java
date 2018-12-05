package com.kaihei.esportingplus.api.vo;

import com.kaihei.esportingplus.common.enums.SettlementTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * 结算方式 VO
 * @author liangyi
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
@ApiModel(description = "结算方式")
public class SettlementModeVO implements Serializable {

    private static final long serialVersionUID = -5896564753529960395L;

    /**
     * 结算类型 {@link com.kaihei.esportingplus.common.enums.SettlementTypeEnum}
     */
    @NotNull(message = "结算类型不能为空")
    @ApiModelProperty(value = "结算类型",
            required = true, position = 1, example = "1")
    private Integer settlementType;

    /**
     * 结算数量
     */
    @NotNull(message = "结算数量不能为空")
    @ApiModelProperty(value = "结算数量",
            required = true, position = 2, example = "1")
    private BigDecimal settlementNumber;

    /**
     * 结算类型显示名称
     */
    private String settlementDisplay;

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

    public String getSettlementDisplay() {
        SettlementTypeEnum settlementTypeEnum = SettlementTypeEnum.getByCode(settlementType);
        return settlementNumber+settlementTypeEnum.getDesc();
    }

    public void setSettlementDisplay(String settlementDisplay) {
        this.settlementDisplay = settlementDisplay;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
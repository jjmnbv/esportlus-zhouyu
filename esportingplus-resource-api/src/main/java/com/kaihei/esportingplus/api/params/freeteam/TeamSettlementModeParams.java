package com.kaihei.esportingplus.api.params.freeteam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * 车队结算方式参数
 *
 * @author liangyi
 */
@Validated
@ApiModel(description = "车队结算方式")
@Builder
@AllArgsConstructor
public class TeamSettlementModeParams implements Serializable {

    private static final long serialVersionUID = 2451976693344588469L;

    /**
     * 结算类型 id,来自数据字典(小时/局数)
     */
    @NotNull(message = "结算类型id不能为空")
    @ApiModelProperty(value = "结算类型id",
            required = true, position = 1, example = "1")
    private Integer settlementTypeId;

    /**
     * 结算数量
     */
    @NotNull(message = "结算数量不能为空")
    @ApiModelProperty(value = "结算数量",
            required = true, position = 2, example = "1")
    private BigDecimal settlementNumber;

    public TeamSettlementModeParams() {
    }

    public Integer getSettlementTypeId() {
        return settlementTypeId;
    }

    public void setSettlementTypeId(Integer settlementTypeId) {
        this.settlementTypeId = settlementTypeId;
    }

    public BigDecimal getSettlementNumber() {
        return settlementNumber;
    }

    public void setSettlementNumber(BigDecimal settlementNumber) {
        this.settlementNumber = settlementNumber;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
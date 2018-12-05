package com.kaihei.esportingplus.api.vo.freeteam;

import com.kaihei.esportingplus.api.vo.DictBaseVO;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 结算方式 VO
 * @author liangyi
 */
@Builder
@AllArgsConstructor
public class TeamSettlementModeVO implements Serializable {

    private static final long serialVersionUID = 8829666112191994299L;

    /**
     * 结算类型,来自数据字典
     */
    private DictBaseVO settlementType;

    /**
     * 结算数量
     */
    private BigDecimal settlementNumber;


    public TeamSettlementModeVO() {
    }

    public DictBaseVO getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(DictBaseVO settlementType) {
        this.settlementType = settlementType;
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
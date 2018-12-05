package com.kaihei.esportingplus.payment.api.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 用户钱包余额 vo
 *
 * @author xiaolijun
 */
public class WalletsVO implements Serializable {

    private static final long serialVersionUID = -3739920356386483814L;

    /**
     * 暴鸡币余额
     */
    private BigDecimal gcoinAmount;

    /**
     * 暴击值余额
     */
    private BigDecimal starlightAmount;

    public WalletsVO() {
    }

    public BigDecimal getGcoinAmount() {
        return gcoinAmount;
    }

    public void setGcoinAmount(BigDecimal gcoinAmount) {
        this.gcoinAmount = gcoinAmount;
    }

    public BigDecimal getStarlightAmount() {
        return starlightAmount;
    }

    public void setStarlightAmount(BigDecimal starlightAmount) {
        this.starlightAmount = starlightAmount;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

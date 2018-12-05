package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class BaojiLevelRateVo implements Serializable {

    private static final long serialVersionUID = 7387289806731095720L;

    /**
     * 暴鸡等级
     */
    private Integer baojiLevel;
    /**
     * 暴鸡等级系数
     */
    private BigDecimal baojiLevelRate;

    public Integer getBaojiLevel() {
        return baojiLevel;
    }

    public void setBaojiLevel(Integer baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    public BigDecimal getBaojiLevelRate() {
        return baojiLevelRate;
    }

    public void setBaojiLevelRate(BigDecimal baojiLevelRate) {
        this.baojiLevelRate = baojiLevelRate;
    }
}

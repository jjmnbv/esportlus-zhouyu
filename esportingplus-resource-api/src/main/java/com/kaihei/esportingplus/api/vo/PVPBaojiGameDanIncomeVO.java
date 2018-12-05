package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 暴鸡段位计价配置 VO
 * @author liangyi
 */
@Builder
@AllArgsConstructor
public class PVPBaojiGameDanIncomeVO implements Serializable {

    private static final long serialVersionUID = 4153619194017191851L;

    /**
     * 暴鸡等级
     */
    private Integer baojiLevelCode;

    /**
     * 老板段位 id
     */
    private Integer bossGameDanId;

    /**
     * 原价
     */
    private Integer originalFee;

    /**
     * 折扣价
     */
    private Integer discountFee;


    public PVPBaojiGameDanIncomeVO() {
    }

    public Integer getBaojiLevelCode() {
        return baojiLevelCode;
    }

    public void setBaojiLevelCode(Integer baojiLevelCode) {
        this.baojiLevelCode = baojiLevelCode;
    }

    public Integer getBossGameDanId() {
        return bossGameDanId;
    }

    public void setBossGameDanId(Integer bossGameDanId) {
        this.bossGameDanId = bossGameDanId;
    }

    public Integer getOriginalFee() {
        return originalFee;
    }

    public void setOriginalFee(Integer originalFee) {
        this.originalFee = originalFee;
    }

    public Integer getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Integer discountFee) {
        this.discountFee = discountFee;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
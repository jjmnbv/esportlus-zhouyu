package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhangfang
 */
public class RedisGameRaid implements Serializable{

    private static final long serialVersionUID = -294285745053024835L;
    /** 副本code **/
    private Integer raidCode;
    /** 副本名称 **/
    private String raidName;
    /** 游戏副本排序 **/
    private Integer displayOrder;
    /** 副本原价 **/
    private Integer fee;
    /** 副本折扣价 **/
    private Integer discountFee;
    /** 副本最大人数 **/
    private Integer maxPositionCount;
    /**
     * 打手占比
     */
    private BigDecimal raidDpsRate;
    /**
     * 辅助占比
     */
    private BigDecimal raidAssistRate;
    /**
     * 认证副本code
     */
    private Integer certRaidCode;
    public Integer getRaidCode() {
        return raidCode;
    }

    public void setRaidCode(Integer raidCode) {
        this.raidCode = raidCode;
    }

    public String getRaidName() {
        return raidName;
    }

    public void setRaidName(String raidName) {
        this.raidName = raidName;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public Integer getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Integer discountFee) {
        this.discountFee = discountFee;
    }

    public Integer getMaxPositionCount() {
        return maxPositionCount;
    }

    public void setMaxPositionCount(Integer maxPositionCount) {
        this.maxPositionCount = maxPositionCount;
    }

    public BigDecimal getRaidDpsRate() {
        return raidDpsRate;
    }

    public void setRaidDpsRate(BigDecimal raidDpsRate) {
        this.raidDpsRate = raidDpsRate;
    }

    public BigDecimal getRaidAssistRate() {
        return raidAssistRate;
    }

    public void setRaidAssistRate(BigDecimal raidAssistRate) {
        this.raidAssistRate = raidAssistRate;
    }

    public Integer getCertRaidCode() {
        return certRaidCode;
    }

    public void setCertRaidCode(Integer certRaidCode) {
        this.certRaidCode = certRaidCode;
    }
}

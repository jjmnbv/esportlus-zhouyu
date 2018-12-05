package com.kaihei.esportingplus.resource.domain.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 暴鸡/暴娘计价配置
 *
 * @author liangyi
 */
@Table(name = "baoji_pricing_config_pvp")
@Builder
@AllArgsConstructor
public class PVPBaojiPricingConfig implements Serializable {

    private static final long serialVersionUID = 3832546847295659197L;

    /**
     * PVP暴鸡/暴娘计价配置主键ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 游戏ID, 来自数据字典
     */
    @Column(name = "game_id")
    private Integer gameId;

    /**
     * 游戏段位ID, 来自数据字典
     */
    @Column(name = "boss_game_dan_id")
    private Integer bossGameDanId;

    /**
     * 暴鸡等级ID, 来自数据字典
     */
    @Column(name = "baoji_level_id")
    private Integer baojiLevelId;

    /**
     * 暴鸡等级code, 100/101/102/300
     */
    @Column(name = "baoji_level_code")
    private Integer baojiLevelCode;

    /**
     * 原价
     */
    @Column(name = "original_fee")
    private Integer originalFee;

    /**
     * 折扣价
     */
    @Column(name = "discount_fee")
    private Integer discountFee;

    /**
     * 计价类型,1: 滴滴; 2: 车队
     */
    @Column(name = "pricing_type")
    private Byte pricingType;

    /**
     * 状态, 1:启用; 0:禁用
     */
    private Byte status;

    public PVPBaojiPricingConfig() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getBaojiLevelId() {
        return baojiLevelId;
    }

    public void setBaojiLevelId(Integer baojiLevelId) {
        this.baojiLevelId = baojiLevelId;
    }

    public Integer getBaojiLevelCode() {
        return baojiLevelCode;
    }

    public void setBaojiLevelCode(Integer baojiLevelCode) {
        this.baojiLevelCode = baojiLevelCode;
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

    public Byte getPricingType() {
        return pricingType;
    }

    public void setPricingType(Byte pricingType) {
        this.pricingType = pricingType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
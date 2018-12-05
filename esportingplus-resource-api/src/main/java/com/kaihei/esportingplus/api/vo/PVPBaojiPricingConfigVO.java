package com.kaihei.esportingplus.api.vo;

import com.kaihei.esportingplus.api.vo.freeteam.DanDictVo;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 暴鸡计价配置 VO
 * @author liangyi
 */
@Builder
@AllArgsConstructor
public class PVPBaojiPricingConfigVO implements Serializable {

    private static final long serialVersionUID = -6443647963649404908L;

    /**
     * 暴鸡计价配置 id
     */
    private Integer baojiPricingConfigId;

    /**
     * 所属游戏
     */
    private GameDictVO game;

    /**
     * 暴鸡等级
     */
    private DictBaseVO baojiLevel;

    /**
     * 游戏段位
     */
    private DanDictVo gameDan;

    /**
     * 原价
     */
    private Integer originalFee;

    /**
     * 折扣价
     */
    private Integer discountFee;

    /**
     * 计价类型--1: 滴滴; 2: 车队
     */
    private Integer pricingType;

    /**
     * 状态--1: 启用; 0: 禁用
     */
    private Integer status;

    public PVPBaojiPricingConfigVO() {
    }

    public Integer getBaojiPricingConfigId() {
        return baojiPricingConfigId;
    }

    public void setBaojiPricingConfigId(Integer baojiPricingConfigId) {
        this.baojiPricingConfigId = baojiPricingConfigId;
    }

    public GameDictVO getGame() {
        return game;
    }

    public void setGame(GameDictVO game) {
        this.game = game;
    }

    public DictBaseVO getBaojiLevel() {
        return baojiLevel;
    }

    public void setBaojiLevel(DictBaseVO baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    public DanDictVo getGameDan() {
        return gameDan;
    }

    public void setGameDan(DanDictVo gameDan) {
        this.gameDan = gameDan;
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

    public Integer getPricingType() {
        return pricingType;
    }

    public void setPricingType(Integer pricingType) {
        this.pricingType = pricingType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
package com.kaihei.esportingplus.resource.domain.entity;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "config_baoji_level")
public class ConfigBaojiLevel {
    /**
     * 佣金配置ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 暴鸡等级
     */
    @Column(name = "baoji_level")
    private Integer baojiLevel;

    /**
     * 暴鸡等级系统
     */
    @Column(name = "baoji_level_rate")
    private BigDecimal baojiLevelRate;

    public ConfigBaojiLevel(Long id, Integer baojiLevel, BigDecimal baojiLevelRate) {
        this.id = id;
        this.baojiLevel = baojiLevel;
        this.baojiLevelRate = baojiLevelRate;
    }

    public ConfigBaojiLevel() {
        super();
    }

    /**
     * 获取佣金配置ID
     *
     * @return id - 佣金配置ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置佣金配置ID
     *
     * @param id 佣金配置ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取暴鸡等级
     *
     * @return baoji_level - 暴鸡等级
     */
    public Integer getBaojiLevel() {
        return baojiLevel;
    }

    /**
     * 设置暴鸡等级
     *
     * @param baojiLevel 暴鸡等级
     */
    public void setBaojiLevel(Integer baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    /**
     * 获取暴鸡等级系统
     *
     * @return baoji_level_rate - 暴鸡等级系统
     */
    public BigDecimal getBaojiLevelRate() {
        return baojiLevelRate;
    }

    /**
     * 设置暴鸡等级系统
     *
     * @param baojiLevelRate 暴鸡等级系统
     */
    public void setBaojiLevelRate(BigDecimal baojiLevelRate) {
        this.baojiLevelRate = baojiLevelRate;
    }
}
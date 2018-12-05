package com.kaihei.esportingplus.trade.domain.entity;

import javax.persistence.*;
import lombok.Builder;

@Table(name = "trade_order_item_team_pvp_free_detail")
@Builder
public class OrderItemTeamPVPFreeDetail {
    /**
     * 老板uid
     */
    @Id
    @Column(name = "boss_uid")
    private String bossUid;

    /**
     * 暴鸡uid
     */
    @Id
    @Column(name = "baoji_uid")
    private String baojiUid;

    /*
     * 车队序列号
     */
    @Id
    @Column(name = "team_sequence")
    private String teamSequence;

    /**
     * 鸡分收益
     */
    private Integer income;

    public OrderItemTeamPVPFreeDetail(String bossUid, String baojiUid, String teamSequence, Integer income) {
        this.bossUid = bossUid;
        this.baojiUid = baojiUid;
        this.teamSequence = teamSequence;
        this.income = income;
    }

    public OrderItemTeamPVPFreeDetail() {
        super();
    }

    /**
     * @return boss_uid
     */
    public String getBossUid() {
        return bossUid;
    }

    /**
     * @param bossUid
     */
    public void setBossUid(String bossUid) {
        this.bossUid = bossUid == null ? null : bossUid.trim();
    }

    /**
     * @return baoji_uid
     */
    public String getBaojiUid() {
        return baojiUid;
    }

    /**
     * @param baojiUid
     */
    public void setBaojiUid(String baojiUid) {
        this.baojiUid = baojiUid == null ? null : baojiUid.trim();
    }

    /**
     * @return team_sequence
     */
    public String getTeamSequence() {
        return teamSequence;
    }

    /**
     * @param teamSequence
     */
    public void setTeamSequence(String teamSequence) {
        this.teamSequence = teamSequence == null ? null : teamSequence.trim();
    }

    /**
     * 获取鸡分收益
     *
     * @return income - 鸡分收益
     */
    public Integer getIncome() {
        return income;
    }

    /**
     * 设置鸡分收益
     *
     * @param income 鸡分收益
     */
    public void setIncome(Integer income) {
        this.income = income;
    }
}
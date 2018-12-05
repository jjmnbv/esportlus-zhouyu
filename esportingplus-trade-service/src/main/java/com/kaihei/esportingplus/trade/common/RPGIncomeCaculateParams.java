package com.kaihei.esportingplus.trade.common;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;

public final class RPGIncomeCaculateParams implements Serializable {

    //老板支付总金额
    private int sumAmout;
    //暴鸡所在位置
    private int raidLocation;
    //暴鸡等级
    private int baojiLevel;
    //dps占比
    private double raidDpsHiredRate;
    //辅助占比
    private double raidAssistHiredRate;
    //dps人数
    private int raidDpsNumbers;
    //辅助人数
    private int raidAssistNumbers;

    //普通暴鸡dps人数
    private int ordinaryBaojiDpsNumbers;
    //优选暴鸡dps人数
    private int excellentBaojiDpsNumbers;
    //超级暴鸡dps人数
    private int superBaojiDpsNumbers;

    //普通暴鸡辅助人数
    private int ordinaryBaojiAssistNumbers;
    //优选暴鸡辅助人数
    private int excellentBaojiAssistNumbers;
    //超级暴鸡辅助人数
    private int superBaojiAssistNumbers;

    //普通暴鸡等级系数
    private double ordinaryBaojiLevelRate;
    //优选暴鸡等级系数
    private double excellentBaojiLevelRate;
    //超级暴鸡等级系数
    private double superBaojiLevelRate;



    public int getBaojiLevel() {
        return baojiLevel;
    }

    public void setBaojiLevel(int baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    public int getSumAmout() {
        return sumAmout;
    }

    public void setSumAmout(int sumAmout) {
        this.sumAmout = sumAmout;
    }

    public double getRaidDpsHiredRate() {
        return raidDpsHiredRate;
    }

    public void setRaidDpsHiredRate(double raidDpsHiredRate) {
        this.raidDpsHiredRate = raidDpsHiredRate;
    }

    public double getRaidAssistHiredRate() {
        return raidAssistHiredRate;
    }

    public void setRaidAssistHiredRate(double raidAssistHiredRate) {
        this.raidAssistHiredRate = raidAssistHiredRate;
    }

    public double getOrdinaryBaojiLevelRate() {
        return ordinaryBaojiLevelRate;
    }

    public void setOrdinaryBaojiLevelRate(double ordinaryBaojiLevelRate) {
        this.ordinaryBaojiLevelRate = ordinaryBaojiLevelRate;
    }

    public double getExcellentBaojiLevelRate() {
        return excellentBaojiLevelRate;
    }

    public void setExcellentBaojiLevelRate(double excellentBaojiLevelRate) {
        this.excellentBaojiLevelRate = excellentBaojiLevelRate;
    }

    public double getSuperBaojiLevelRate() {
        return superBaojiLevelRate;
    }

    public void setSuperBaojiLevelRate(double superBaojiLevelRate) {
        this.superBaojiLevelRate = superBaojiLevelRate;
    }

    public int getRaidLocation() {
        return raidLocation;
    }

    public void setRaidLocation(int raidLocation) {
        this.raidLocation = raidLocation;
    }

    public int getRaidDpsNumbers() {
        return raidDpsNumbers;
    }

    public void setRaidDpsNumbers(int raidDpsNumbers) {
        this.raidDpsNumbers = raidDpsNumbers;
    }

    public int getRaidAssistNumbers() {
        return raidAssistNumbers;
    }

    public void setRaidAssistNumbers(int raidAssistNumbers) {
        this.raidAssistNumbers = raidAssistNumbers;
    }

    public int getOrdinaryBaojiDpsNumbers() {
        return ordinaryBaojiDpsNumbers;
    }

    public void setOrdinaryBaojiDpsNumbers(int ordinaryBaojiDpsNumbers) {
        this.ordinaryBaojiDpsNumbers = ordinaryBaojiDpsNumbers;
    }

    public int getExcellentBaojiDpsNumbers() {
        return excellentBaojiDpsNumbers;
    }

    public void setExcellentBaojiDpsNumbers(int excellentBaojiDpsNumbers) {
        this.excellentBaojiDpsNumbers = excellentBaojiDpsNumbers;
    }

    public int getSuperBaojiDpsNumbers() {
        return superBaojiDpsNumbers;
    }

    public void setSuperBaojiDpsNumbers(int superBaojiDpsNumbers) {
        this.superBaojiDpsNumbers = superBaojiDpsNumbers;
    }

    public int getOrdinaryBaojiAssistNumbers() {
        return ordinaryBaojiAssistNumbers;
    }

    public void setOrdinaryBaojiAssistNumbers(int ordinaryBaojiAssistNumbers) {
        this.ordinaryBaojiAssistNumbers = ordinaryBaojiAssistNumbers;
    }

    public int getExcellentBaojiAssistNumbers() {
        return excellentBaojiAssistNumbers;
    }

    public void setExcellentBaojiAssistNumbers(int excellentBaojiAssistNumbers) {
        this.excellentBaojiAssistNumbers = excellentBaojiAssistNumbers;
    }

    public int getSuperBaojiAssistNumbers() {
        return superBaojiAssistNumbers;
    }

    public void setSuperBaojiAssistNumbers(int superBaojiAssistNumbers) {
        this.superBaojiAssistNumbers = superBaojiAssistNumbers;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

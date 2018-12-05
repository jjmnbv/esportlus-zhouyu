package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 暴鸡订单
 */
public class BaojiOrderVO implements Serializable{

    private static final long serialVersionUID = 7050403878867150311L;

    /**
     * 订单状态，0：准备中(暴鸡队员）, 1：待付款（老板上车后的状态），2：已付款（老板付款后的状态），3：已取消，4：已完成
     */
    private Byte status;

    /**
     * 订单序列号
     */
    private String sequeue;

    /**
     * 订单创建时间
     */
    private Date gmtCreate;

    /**
     * 游戏名称
     */
    private String gameName;
    /**
     * 副本名称
     */
    private String raidName;
    /**
     * 跨区名称
     */
    private String zoneAcrossName;

    /**
     * 订单收益金额
     */
    private Integer price;

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getSequeue() {
        return sequeue;
    }

    public void setSequeue(String sequeue) {
        this.sequeue = sequeue;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getRaidName() {
        return raidName;
    }

    public void setRaidName(String raidName) {
        this.raidName = raidName;
    }

    public String getZoneAcrossName() {
        return zoneAcrossName;
    }

    public void setZoneAcrossName(String zoneAcrossName) {
        this.zoneAcrossName = zoneAcrossName;
    }

    @Override
    public String toString() {
        return "BaojiOrderVO{" +
                "status=" + status +
                ", sequeue='" + sequeue + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gameName='" + gameName + '\'' +
                ", raidName='" + raidName + '\'' +
                ", zoneAcrossName='" + zoneAcrossName + '\'' +
                ", price=" + price +
                '}';
    }
}

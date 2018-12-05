package com.kaihei.esportingplus.gamingteam.api.vo.rpg;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 车队基本基础信息
 * @author liangyi
 */
public class RPGRedisTeamBaseVO implements Serializable {

    private static final long serialVersionUID = -6745921848733985590L;

    /** 车队序列号 */
    private String sequence;

    /** 车队标题 */
    private String title;

    /** 车队房间号 */
    private Integer roomNum;

    /** 游戏 code */
    private Integer gameCode;

    /** 游戏名称 */
    private String gameName;

    /** 副本 code */
    private Integer raidCode;

    /** 副本名称 */
    private String raidName;

    /** 车队跨区 code */
    private Integer zoneAcrossCode;

    /** 车队跨区名称 */
    private String zoneAcrossName;

    /** 攻坚队名称 */
    private String assaultName;

    /** 游戏频道 */
    private String channel;

    /** 老板支付超时时间(秒) */
    private Integer paymentTimeout;

    /** 车队原价 */
    private Integer originalFee;

    /** 车队折扣价 */
    private Integer discountFee;

    public RPGRedisTeamBaseVO() {
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(Integer roomNum) {
        this.roomNum = roomNum;
    }

    public Integer getGameCode() {
        return gameCode;
    }

    public void setGameCode(Integer gameCode) {
        this.gameCode = gameCode;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

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

    public Integer getZoneAcrossCode() {
        return zoneAcrossCode;
    }

    public void setZoneAcrossCode(Integer zoneAcrossCode) {
        this.zoneAcrossCode = zoneAcrossCode;
    }

    public String getZoneAcrossName() {
        return zoneAcrossName;
    }

    public void setZoneAcrossName(String zoneAcrossName) {
        this.zoneAcrossName = zoneAcrossName;
    }

    public String getAssaultName() {
        return assaultName;
    }

    public void setAssaultName(String assaultName) {
        this.assaultName = assaultName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getPaymentTimeout() {
        return paymentTimeout;
    }

    public void setPaymentTimeout(Integer paymentTimeout) {
        this.paymentTimeout = paymentTimeout;
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

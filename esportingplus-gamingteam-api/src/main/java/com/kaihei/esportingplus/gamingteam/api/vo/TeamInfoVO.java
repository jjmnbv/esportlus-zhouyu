package com.kaihei.esportingplus.gamingteam.api.vo;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 车队详细信息 VO
 * @author liangyi
 */
public class TeamInfoVO implements Serializable{

    private static final long serialVersionUID = 524187632669457319L;

    /** 车队id */
    private Long id;

    /** 车队序列号 */
    private String sequence;

    /** 车队标题 */
    private String title;

    /** 车队房间号 */
    private Integer roomNum;

    /** 车队状态，0：准备中 1：已发车(进行中) 2：已解散 3：已结束 */
    private Integer status;

    /** 车队比赛结果，-1：准备中解散 0: 胜利 1：失败 2：未打 */
    private Integer gameResult;

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

    /** 车队原始位置数 */
    private Integer originalPositionCount;

    /** 车队当前的实际位置数 */
    private Integer actuallyPositionCount;

    /** 车队原价 */
    private Integer originalFee;

    /** 车队折扣价 */
    private Integer discountFee;

    public TeamInfoVO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getGameResult() {
        return gameResult;
    }

    public void setGameResult(Integer gameResult) {
        this.gameResult = gameResult;
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

    public Integer getOriginalPositionCount() {
        return originalPositionCount;
    }

    public void setOriginalPositionCount(Integer originalPositionCount) {
        this.originalPositionCount = originalPositionCount;
    }

    public Integer getActuallyPositionCount() {
        return actuallyPositionCount;
    }

    public void setActuallyPositionCount(Integer actuallyPositionCount) {
        this.actuallyPositionCount = actuallyPositionCount;
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
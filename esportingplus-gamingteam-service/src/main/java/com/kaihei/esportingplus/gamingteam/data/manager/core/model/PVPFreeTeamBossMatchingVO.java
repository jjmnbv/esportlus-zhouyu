package com.kaihei.esportingplus.gamingteam.data.manager.core.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 免费车队老板的匹配信息
 *
 * @author liangyi
 */
@NoArgsConstructor
@AllArgsConstructor
public class PVPFreeTeamBossMatchingVO implements Serializable{

    private static final long serialVersionUID = -4641339359366408928L;
    /**
     * 用户uid
     */
    private String uid;

    /**
     * 鸡牌号
     */
    private String chickenId;

    /**
     * 用户uid
     */
    private String avatar;

    /**
     * 用户
     */
    private String username;

    /**
     * 用户身份
     */
    private Integer userIdentity;

    /**
     * 结算服务类型
     */
    private Integer settlementType;
    /**
     * 结算服务类型中文名称
     */
    private String settlementName;
    /**
     * 免费车队类型Id
     */
    private Integer teamTypeId;

    /**
     * 免费车队类型 名称
     */
    private String teamTypeDisplayName;

    /**
     * 游戏ID
     */
    private Integer gameId;

    /**
     * 游戏大区 id
     */
    private Integer gameZoneId;

    /**
     * 游戏大区名称
     */
    private String gameZoneName;

    /**
     * 游戏段位 id, 陪玩车队暂时不传
     */
    private Integer gameDanId;
    /**
     * 游戏段位名称, 陪玩车队暂时不传
     */
    private String gameDanName;

    /**
     * 开始匹配时间
     */
    private LocalDateTime startMatchingTime = LocalDateTime.now();

    public Integer getSettlementType() {
        return settlementType;
    }

    public String getSettlementName() {
        return settlementName;
    }

    public void setSettlementName(String settlementName) {
        this.settlementName = settlementName;
    }

    public void setSettlementType(Integer settlementType) {
        this.settlementType = settlementType;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getTeamTypeId() {
        return teamTypeId;
    }

    public void setTeamTypeId(Integer teamTypeId) {
        this.teamTypeId = teamTypeId;
    }

    public String getTeamTypeDisplayName() {
        return teamTypeDisplayName;
    }

    public void setTeamTypeDisplayName(String teamTypeDisplayName) {
        this.teamTypeDisplayName = teamTypeDisplayName;
    }

    public Integer getGameZoneId() {
        return gameZoneId;
    }

    public void setGameZoneId(Integer gameZoneId) {
        this.gameZoneId = gameZoneId;
    }

    public String getGameZoneName() {
        return gameZoneName;
    }

    public void setGameZoneName(String gameZoneName) {
        this.gameZoneName = gameZoneName;
    }

    public Integer getGameDanId() {
        return gameDanId;
    }

    public void setGameDanId(Integer gameDanId) {
        this.gameDanId = gameDanId;
    }

    public String getGameDanName() {
        return gameDanName;
    }

    public void setGameDanName(String gameDanName) {
        this.gameDanName = gameDanName;
    }

    public LocalDateTime getStartMatchingTime() {
        return startMatchingTime;
    }

    public void setStartMatchingTime(LocalDateTime startMatchingTime) {
        this.startMatchingTime = startMatchingTime;
    }

    public Integer getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(Integer userIdentity) {
        this.userIdentity = userIdentity;
    }

    public String getChickenId() {
        return chickenId;
    }

    public void setChickenId(String chickenId) {
        this.chickenId = chickenId;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

package com.kaihei.esportingplus.user.domain.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "baoji_baoji")
public class UserBaoJi {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private String uid;

    private Short game;

    @Column(name = "game_zone")
    private Short gameZone;

    @Column(name = "game_dan")
    private Short gameDan;

    @Column(name = "dan_picture")
    private String danPicture;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "section_status")
    private Short sectionStatus;

    private Short status;

    @Column(name = "level_status")
    private Short levelStatus;

    @Column(name = "group_id")
    private Integer groupId;

    @Column(name = "tag_id")
    private Integer tagId;

    private String power;

    @Column(name = "verify_time")
    private Date verifyTime;

    @Column(name = "freeze_time")
    private Date freezeTime;

    private Integer section;

    @Column(name = "assistant_id")
    private String assistantId;

    @Column(name = "fail_reasons")
    private String failReasons;

    private String tracking;

    public UserBaoJi(Integer id, String uid, Short game, Short gameZone, Short gameDan, String danPicture, Date updateTime, Date createTime, Short sectionStatus, Short status, Short levelStatus, Integer groupId, Integer tagId, String power, Date verifyTime, Date freezeTime, Integer section, String assistantId, String failReasons, String tracking) {
        this.id = id;
        this.uid = uid;
        this.game = game;
        this.gameZone = gameZone;
        this.gameDan = gameDan;
        this.danPicture = danPicture;
        this.updateTime = updateTime;
        this.createTime = createTime;
        this.sectionStatus = sectionStatus;
        this.status = status;
        this.levelStatus = levelStatus;
        this.groupId = groupId;
        this.tagId = tagId;
        this.power = power;
        this.verifyTime = verifyTime;
        this.freezeTime = freezeTime;
        this.section = section;
        this.assistantId = assistantId;
        this.failReasons = failReasons;
        this.tracking = tracking;
    }

    public UserBaoJi() {
        super();
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid
     */
    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    /**
     * @return game
     */
    public Short getGame() {
        return game;
    }

    /**
     * @param game
     */
    public void setGame(Short game) {
        this.game = game;
    }

    /**
     * @return game_zone
     */
    public Short getGameZone() {
        return gameZone;
    }

    /**
     * @param gameZone
     */
    public void setGameZone(Short gameZone) {
        this.gameZone = gameZone;
    }

    /**
     * @return game_dan
     */
    public Short getGameDan() {
        return gameDan;
    }

    /**
     * @param gameDan
     */
    public void setGameDan(Short gameDan) {
        this.gameDan = gameDan;
    }

    /**
     * @return dan_picture
     */
    public String getDanPicture() {
        return danPicture;
    }

    /**
     * @param danPicture
     */
    public void setDanPicture(String danPicture) {
        this.danPicture = danPicture == null ? null : danPicture.trim();
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return section_status
     */
    public Short getSectionStatus() {
        return sectionStatus;
    }

    /**
     * @param sectionStatus
     */
    public void setSectionStatus(Short sectionStatus) {
        this.sectionStatus = sectionStatus;
    }

    /**
     * @return status
     */
    public Short getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Short status) {
        this.status = status;
    }

    /**
     * @return level_status
     */
    public Short getLevelStatus() {
        return levelStatus;
    }

    /**
     * @param levelStatus
     */
    public void setLevelStatus(Short levelStatus) {
        this.levelStatus = levelStatus;
    }

    /**
     * @return group_id
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * @param groupId
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    /**
     * @return tag_id
     */
    public Integer getTagId() {
        return tagId;
    }

    /**
     * @param tagId
     */
    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    /**
     * @return power
     */
    public String getPower() {
        return power;
    }

    /**
     * @param power
     */
    public void setPower(String power) {
        this.power = power == null ? null : power.trim();
    }

    /**
     * @return verify_time
     */
    public Date getVerifyTime() {
        return verifyTime;
    }

    /**
     * @param verifyTime
     */
    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    /**
     * @return freeze_time
     */
    public Date getFreezeTime() {
        return freezeTime;
    }

    /**
     * @param freezeTime
     */
    public void setFreezeTime(Date freezeTime) {
        this.freezeTime = freezeTime;
    }

    /**
     * @return section
     */
    public Integer getSection() {
        return section;
    }

    /**
     * @param section
     */
    public void setSection(Integer section) {
        this.section = section;
    }

    /**
     * @return assistant_id
     */
    public String getAssistantId() {
        return assistantId;
    }

    /**
     * @param assistantId
     */
    public void setAssistantId(String assistantId) {
        this.assistantId = assistantId == null ? null : assistantId.trim();
    }

    /**
     * @return fail_reasons
     */
    public String getFailReasons() {
        return failReasons;
    }

    /**
     * @param failReasons
     */
    public void setFailReasons(String failReasons) {
        this.failReasons = failReasons == null ? null : failReasons.trim();
    }

    /**
     * @return tracking
     */
    public String getTracking() {
        return tracking;
    }

    /**
     * @param tracking
     */
    public void setTracking(String tracking) {
        this.tracking = tracking == null ? null : tracking.trim();
    }
}
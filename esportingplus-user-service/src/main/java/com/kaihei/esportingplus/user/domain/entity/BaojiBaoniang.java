package com.kaihei.esportingplus.user.domain.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "baoji_baoniang")
public class BaojiBaoniang {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private String uid;

    private Short game;

    @Column(name = "game_picture")
    private String gamePicture;

    @Column(name = "personal_video")
    private String personalVideo;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "section_status")
    private Short sectionStatus;

    private Short status;

    @Column(name = "level_status")
    private Short levelStatus;

    private Integer power;

    @Column(name = "verify_time")
    private Date verifyTime;

    @Column(name = "tag_id")
    private Integer tagId;

    @Column(name = "game_dan")
    private Short gameDan;

    @Column(name = "game_zone")
    private Short gameZone;

    private Integer section;

    @Column(name = "assistant_id")
    private String assistantId;

    private String tracking;

    public BaojiBaoniang(Integer id, String uid, Short game, String gamePicture, String personalVideo, Date updateTime, Date createTime, Short sectionStatus, Short status, Short levelStatus, Integer power, Date verifyTime, Integer tagId, Short gameDan, Short gameZone, Integer section, String assistantId, String tracking) {
        this.id = id;
        this.uid = uid;
        this.game = game;
        this.gamePicture = gamePicture;
        this.personalVideo = personalVideo;
        this.updateTime = updateTime;
        this.createTime = createTime;
        this.sectionStatus = sectionStatus;
        this.status = status;
        this.levelStatus = levelStatus;
        this.power = power;
        this.verifyTime = verifyTime;
        this.tagId = tagId;
        this.gameDan = gameDan;
        this.gameZone = gameZone;
        this.section = section;
        this.assistantId = assistantId;
        this.tracking = tracking;
    }

    public BaojiBaoniang() {
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
     * @return game_picture
     */
    public String getGamePicture() {
        return gamePicture;
    }

    /**
     * @param gamePicture
     */
    public void setGamePicture(String gamePicture) {
        this.gamePicture = gamePicture == null ? null : gamePicture.trim();
    }

    /**
     * @return personal_video
     */
    public String getPersonalVideo() {
        return personalVideo;
    }

    /**
     * @param personalVideo
     */
    public void setPersonalVideo(String personalVideo) {
        this.personalVideo = personalVideo == null ? null : personalVideo.trim();
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
     * @return power
     */
    public Integer getPower() {
        return power;
    }

    /**
     * @param power
     */
    public void setPower(Integer power) {
        this.power = power;
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
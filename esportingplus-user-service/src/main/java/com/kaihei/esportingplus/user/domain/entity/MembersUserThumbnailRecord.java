package com.kaihei.esportingplus.user.domain.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "members_userthumbnailrecord")
public class MembersUserThumbnailRecord {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    private String thumbnail;

    @Column(name = "verify_result")
    private Short verifyResult;

    @Column(name = "verify_status")
    private Short verifyStatus;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "verify_tracking")
    private String verifyTracking;

    public MembersUserThumbnailRecord(Integer id, Integer userId, String thumbnail, Short verifyResult, Short verifyStatus, Date createTime, Date updateTime, String verifyTracking) {
        this.id = id;
        this.userId = userId;
        this.thumbnail = thumbnail;
        this.verifyResult = verifyResult;
        this.verifyStatus = verifyStatus;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.verifyTracking = verifyTracking;
    }

    public MembersUserThumbnailRecord() {
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
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return thumbnail
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * @param thumbnail
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail == null ? null : thumbnail.trim();
    }

    /**
     * @return verify_result
     */
    public Short getVerifyResult() {
        return verifyResult;
    }

    /**
     * @param verifyResult
     */
    public void setVerifyResult(Short verifyResult) {
        this.verifyResult = verifyResult;
    }

    /**
     * @return verify_status
     */
    public Short getVerifyStatus() {
        return verifyStatus;
    }

    /**
     * @param verifyStatus
     */
    public void setVerifyStatus(Short verifyStatus) {
        this.verifyStatus = verifyStatus;
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
     * @return verify_tracking
     */
    public String getVerifyTracking() {
        return verifyTracking;
    }

    /**
     * @param verifyTracking
     */
    public void setVerifyTracking(String verifyTracking) {
        this.verifyTracking = verifyTracking == null ? null : verifyTracking.trim();
    }
}
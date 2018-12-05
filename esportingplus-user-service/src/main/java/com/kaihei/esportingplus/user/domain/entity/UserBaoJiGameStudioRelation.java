package com.kaihei.esportingplus.user.domain.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "baoji_gamestudiobaoji")
public class UserBaoJiGameStudioRelation {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private String uid;

    @Column(name = "studio_id")
    private Integer studioId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    public UserBaoJiGameStudioRelation(Integer id, String uid, Integer studioId, Date createTime, Date updateTime) {
        this.id = id;
        this.uid = uid;
        this.studioId = studioId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public UserBaoJiGameStudioRelation() {
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
     * @return studio_id
     */
    public Integer getStudioId() {
        return studioId;
    }

    /**
     * @param studioId
     */
    public void setStudioId(Integer studioId) {
        this.studioId = studioId;
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
}
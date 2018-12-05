package com.kaihei.esportingplus.user.domain.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "members_user_freeteam_chances")
public class UserFreeTeamChances {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 用户uid
     */
    private String uid;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 可免费上车次数
     */
    private Integer count;

    /**
     * 是否禁用
     */
    private Boolean disable;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 删除时间
     */
    @Column(name = "delete_time")
    private Date deleteTime;

    /**
     * 已用免费上车次数
     */
    @Column(name = "used_count")
    private Integer usedCount;

    @Column(name = "last_notified_time")
    private Date lastNotifiedTime;

    public UserFreeTeamChances(Integer id, String uid, Integer userId, Integer count, Boolean disable, Date createTime, Date updateTime, Date deleteTime, Integer usedCount, Date lastNotifiedTime) {
        this.id = id;
        this.uid = uid;
        this.userId = userId;
        this.count = count;
        this.disable = disable;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.deleteTime = deleteTime;
        this.usedCount = usedCount;
        this.lastNotifiedTime = lastNotifiedTime;
    }

    public UserFreeTeamChances() {
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
     * 获取用户uid
     *
     * @return uid - 用户uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * 设置用户uid
     *
     * @param uid 用户uid
     */
    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    /**
     * 获取用户id
     *
     * @return user_id - 用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取可免费上车次数
     *
     * @return count - 可免费上车次数
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 设置可免费上车次数
     *
     * @param count 可免费上车次数
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 获取是否禁用
     *
     * @return disable - 是否禁用
     */
    public Boolean getDisable() {
        return disable;
    }

    /**
     * 设置是否禁用
     *
     * @param disable 是否禁用
     */
    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取删除时间
     *
     * @return delete_time - 删除时间
     */
    public Date getDeleteTime() {
        return deleteTime;
    }

    /**
     * 设置删除时间
     *
     * @param deleteTime 删除时间
     */
    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    /**
     * 获取已用免费上车次数
     *
     * @return used_count - 已用免费上车次数
     */
    public Integer getUsedCount() {
        return usedCount;
    }

    /**
     * 设置已用免费上车次数
     *
     * @param usedCount 已用免费上车次数
     */
    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    /**
     * @return last_notified_time
     */
    public Date getLastNotifiedTime() {
        return lastNotifiedTime;
    }

    /**
     * @param lastNotifiedTime
     */
    public void setLastNotifiedTime(Date lastNotifiedTime) {
        this.lastNotifiedTime = lastNotifiedTime;
    }
}